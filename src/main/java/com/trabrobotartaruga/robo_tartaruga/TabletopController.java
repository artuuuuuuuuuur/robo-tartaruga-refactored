package com.trabrobotartaruga.robo_tartaruga;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

import com.trabrobotartaruga.robo_tartaruga.classes.Map;
import com.trabrobotartaruga.robo_tartaruga.classes.bot.Bot;
import com.trabrobotartaruga.robo_tartaruga.classes.bot.RandomBot;
import com.trabrobotartaruga.robo_tartaruga.classes.bot.SmartBot;
import com.trabrobotartaruga.robo_tartaruga.classes.obstacle.Obstacle;
import com.trabrobotartaruga.robo_tartaruga.classes.obstacle.Stone;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidInputException;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidMoveException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TabletopController {

    private Map map;
    private final Semaphore semaphore = new Semaphore(0);
    private Bot lastPlayedBot;
    private int currentTurn = 0;
    private int lastTurn = 0;

    @FXML
    GridPane gameGrid;
    @FXML
    AnchorPane tabletopAnchorPane;

    public void load(@SuppressWarnings("exports") Map map) {
        this.map = map;

        showBots();
        play();
    }

    private void play() {
        new Thread(() -> {
            try {
                while (!map.isGameOver()) {
                    currentTurn++;
                    for (Bot bot : map.getBots()) {
                        if (map.isGameOver()) {
                            break;
                        }
                        boolean othersInacive = true;
                        for (Bot botCheck : map.getBots()) {
                            if (!botCheck.equals(bot)) {
                                if (botCheck.isActive()) {
                                    othersInacive = false;
                                }
                            }
                        }

                        if (bot.equals(lastPlayedBot) && lastTurn == currentTurn) {
                            continue;
                        }

                        if (((bot.equals(lastPlayedBot) && !othersInacive) && map.getBots().size() > 1) || !bot.isActive()) {
                            continue;
                        }

                        boolean goodMove = true;
                        Platform.runLater(() -> botTurn(bot));

                        try {
                            Thread.sleep(600);

                            switch (bot) {
                                case RandomBot randomBot ->
                                    randomBot.move("");
                                case SmartBot smartBot ->
                                    smartBot.move(0);
                                default -> {
                                    move(bot);
                                    pause();
                                }
                            }
                        } catch (InvalidMoveException e) {
                            bot.setInvalidMoves(bot.getInvalidMoves() + 1);
                            final String lastMove;
                            switch (bot.getLastMove()) {
                                case 1 ->
                                    lastMove = "cima";
                                case 2 ->
                                    lastMove = "baixo";
                                case 3 ->
                                    lastMove = "esquerda";
                                case 4 ->
                                    lastMove = "direita";
                                default ->
                                    lastMove = "";
                            }

                            switch (bot) {
                                case SmartBot smartBot ->
                                    Platform.runLater(() -> createLogLabel("Robô inteligente fez um movimento inválido para " + lastMove));
                                case RandomBot randomBot ->
                                    Platform.runLater(() -> createLogLabel("Robô aleatório fez um movimento inválido para " + lastMove));
                                case Bot currenBot ->
                                    Platform.runLater(() -> createLogLabel("Robô normal fez um movimento inválido para " + lastMove));
                            }

                            goodMove = false;
                        } catch (InvalidInputException | InterruptedException e) {
                            goodMove = false;
                        }

                        lastPlayedBot = bot;
                        if (goodMove) {
                            bot.setValidMoves(bot.getValidMoves() + 1);
                        }
                        bot.setRounds(bot.getRounds() + 1);

                        if(goodMove) {
                            final String lastMove;
                            switch (bot.getLastMove()) {
                                case 1 ->
                                    lastMove = "cima";
                                case 2 ->
                                    lastMove = "baixo";
                                case 3 ->
                                    lastMove = "esquerda";
                                case 4 ->
                                    lastMove = "direita";
                                default ->
                                    lastMove = "";
                            }
                            switch (bot) {
                                case SmartBot smartBot ->
                                    Platform.runLater(() -> createLogLabel("Robô inteligente se moveu para " + lastMove));
                                case RandomBot randomBot ->
                                    Platform.runLater(() -> createLogLabel("Robô aleatório se moveu para " + lastMove));
                                case Bot currenBot ->
                                    Platform.runLater(() -> createLogLabel("Robô normal se moveu para " + lastMove));
                            }
                        }

                        syncUpdate(() -> {
                            map.updateBots();
                            showBots();
                        });

                        if (map.isGameOver()) {
                            break;
                        }

                        if (!map.getObstacles().isEmpty()) {
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException ignored) {
                            }

                            syncUpdate(() -> {
                                try {
                                    map.obstacleAction(this);
                                    map.updateBots();
                                    showBots();
                                } catch (InvalidMoveException | InvalidInputException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                        lastTurn = currentTurn;
                    }
                }

                Thread.sleep(1000);
                Platform.runLater(() -> goToFinalScreen(map.getBots(), map.getWinnerBots()));

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void syncUpdate(Runnable action) {
        java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                action.run();
            } finally {
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void move(Bot bot) {
        String[] possibleInputs = {"up", "down", "left", "right", "1", "2", "3", "4"};
        int goodInput = -1;
        boolean moved = false;
        HBox playerHBox = (HBox) tabletopAnchorPane.lookup("#playerHBox");
        TextField moveTextField = (TextField) tabletopAnchorPane.lookup("#moveTextField");
        while (!moved) {
            playerHBox.setDisable(false);
            pause();

            for (int i = 0; i < possibleInputs.length; i++) {
                if (moveTextField.getText().equals(possibleInputs[i])) {
                    goodInput = i;
                }
            }

            if (goodInput != -1) {
                try {
                    bot.move(moveTextField.getText());
                    moved = true;
                } catch (InvalidMoveException e) {
                    playerHBox.setDisable(true);
                    Platform.runLater(() -> showErrorPane(e.toString()));
                    moved = false;
                } catch (InvalidInputException ex) {
                    try {
                        bot.move(goodInput - 3);
                        moved = true;
                    } catch (InvalidMoveException | InvalidInputException e) {
                        moved = false;
                        Platform.runLater(() -> showErrorPane(e.toString()));
                    }
                    playerHBox.setDisable(true);
                }
            } else {
                Platform.runLater(() -> showErrorPane(new InvalidInputException().toString()));
                moved = false;
            }
        }

        playerHBox.setDisable(true);
        resume();
    }

    public void resume() {
        semaphore.release();
    }

    private void pause() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void goToFinalScreen(List<Bot> bots, List<Bot> winnerBot) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/trabrobotartaruga/robo_tartaruga/tela_final.fxml"));
                Parent root = loader.load();
                Stage newStage = new Stage();
                FinalScreenController finalScreenController = loader.getController();
                finalScreenController.build(bots, winnerBot);
                newStage.setTitle("Resultado Final");
                newStage.setScene(new Scene(root));
                Stage currentstage = (Stage) ((Node) tabletopAnchorPane).getScene().getWindow();
                currentstage.close();
                Platform.runLater(() -> newStage.show());
            } catch (IOException e) {
            }
        });
    }

    private void showErrorPane(String message) {
        try {
            Stage stage = new Stage();
            Scene scene;
            scene = new Scene(new FXMLLoader(App.class.getResource("error.fxml")).load());
            stage.initModality(Modality.APPLICATION_MODAL);
            Label warningLabel = (Label) scene.lookup("#warningLabel");
            warningLabel.setText(message);
            Button okButton = (Button) scene.lookup("#okButton");
            okButton.setOnAction(eh -> {
                stage.close();
            });
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
        }
    }

    private void botTurn(Bot bot) {
        Label botTurnLabel = (Label) tabletopAnchorPane.lookup("#botTurnLabel");
        switch (bot) {
            case SmartBot smartBot -> {
                botTurnLabel.setText("Robô inteligente");
                botTurnLabel.setTextFill(Paint.valueOf(smartBot.getColor()));
            }
            case RandomBot randomBot -> {
                botTurnLabel.setText("Robô aleatório");
                botTurnLabel.setTextFill(Paint.valueOf(randomBot.getColor()));
            }
            case Bot currentBot -> {
                botTurnLabel.setText("Robô normal");
                botTurnLabel.setTextFill(Paint.valueOf(currentBot.getColor()));
            }
        }
    }

    public void createLogLabel(String message) {
        VBox moveLogsVBox = (VBox) tabletopAnchorPane.lookup("#moveLogsVBox");
        Label newLabel = new Label(message);
        newLabel.setTextFill(Paint.valueOf("white"));
        newLabel.setFont(new Font(15));
        moveLogsVBox.getChildren().add(newLabel);
    }

    private void showBots() {
        for (int i = 0; i < map.getX(); i++) {
            for (int j = 0; j < map.getY(); j++) {
                FlowPane gridCell = (FlowPane) gameGrid.lookup("#gridCell" + i + "" + j);
                gridCell.getChildren().clear();
                if (map.getFood().getPosX() == j && map.getFood().getPosY() == i) {
                    ImageView foodImage = new ImageView(new Image(getClass().getResourceAsStream("/com/trabrobotartaruga/robo_tartaruga/assets/food.png"), 100, 100, false, false));
                    gridCell.getChildren().add(foodImage);
                }
                for (Obstacle obstacle : map.getObstacles()) {
                    if (obstacle.getPosX() == j && obstacle.getPosY() == i) {
                        if (obstacle instanceof Stone) {
                            ImageView stoneImage = new ImageView(new Image(getClass().getResourceAsStream("/com/trabrobotartaruga/robo_tartaruga/assets/stone.png"), 100, 100, false, false));
                            gridCell.getChildren().add(stoneImage);
                        } else {
                            ImageView bombImage = new ImageView(new Image(getClass().getResourceAsStream("/com/trabrobotartaruga/robo_tartaruga/assets/bomb.png"), 100, 100, false, false));
                            gridCell.getChildren().add(bombImage);
                        }
                    }
                }
            }
        }
        for (int i = 0; i < map.getX(); i++) {
            for (int j = 0; j < map.getY(); j++) {
                if (!map.getPositions().get(i).get(j).getObjects().isEmpty()) {
                    for (Object object : map.getPositions().get(i).get(j).getObjects()) {
                        FlowPane gridCell = (FlowPane) gameGrid.lookup("#gridCell" + i + "" + j);
                        DropShadow dropShadow = new DropShadow();
                        ColorAdjust monochrome = new ColorAdjust();
                        monochrome.setSaturation(-1);
                        dropShadow.setRadius(1.0);
                        dropShadow.setOffsetX(10.0);
                        dropShadow.setOffsetY(10.0);
                        switch (object) {
                            case RandomBot randomBot -> {
                                ImageView image = new ImageView(new Image(
                                        getClass().getResourceAsStream(
                                                "/com/trabrobotartaruga/robo_tartaruga/assets/random_bot.png"),
                                        70, 70, false, false));
                                dropShadow.setColor(Color.valueOf(randomBot.getColor()));
                                if (!randomBot.isActive()) {
                                    dropShadow.setInput(monochrome);
                                }
                                image.setEffect(dropShadow);
                                gridCell.getChildren().add(image);
                            }
                            case SmartBot smartBot -> {
                                ImageView image = new ImageView(new Image(
                                        getClass().getResourceAsStream(
                                                "/com/trabrobotartaruga/robo_tartaruga/assets/smart_bot.png"),
                                        55, 55, false, false));
                                dropShadow.setColor(Color.valueOf(smartBot.getColor()));
                                if (!smartBot.isActive()) {
                                    dropShadow.setInput(monochrome);
                                }
                                image.setEffect(dropShadow);
                                gridCell.getChildren().add(image);
                            }
                            case Bot bot -> {
                                ImageView image = new ImageView(new Image(
                                        getClass().getResourceAsStream(
                                                "/com/trabrobotartaruga/robo_tartaruga/assets/bot.png"),
                                        55, 55, false, false));
                                dropShadow.setColor(Color.valueOf(bot.getColor()));
                                if (!bot.isActive()) {
                                    dropShadow.setInput(monochrome);
                                }
                                image.setEffect(dropShadow);
                                gridCell.getChildren().add(image);
                            }
                            default -> {
                            }
                        }
                    }

                }
            }
        }
    }
}
