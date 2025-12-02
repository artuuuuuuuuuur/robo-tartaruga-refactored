package com.trabrobotartaruga.robo_tartaruga;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;

import com.trabrobotartaruga.robo_tartaruga.classes.Map;
import com.trabrobotartaruga.robo_tartaruga.classes.bot.Bot;
import com.trabrobotartaruga.robo_tartaruga.classes.bot.RandomBot;
import com.trabrobotartaruga.robo_tartaruga.classes.bot.SmartBot;
import com.trabrobotartaruga.robo_tartaruga.classes.obstacle.Obstacle;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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

        showObjectsUI();
        playGame();
    }

    private void playGame() {
        Thread.ofVirtual().start(() -> {
            try {
                while (!map.isGameOver()) {
                    currentTurn++;
                    playRound();
                }

                Thread.sleep(1000);
                Platform.runLater(() -> goToFinalScreen(map.getBots(), map.getWinnerBots()));

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    private void playRound() {
        for (Bot bot : map.getBots()) {
            boolean goodMove = true;
            if (isGameOver(bot)) {
                continue;
            }

            Platform.runLater(() -> showBotTurnLabel(bot));

            goodMove = moveBot(bot, goodMove);

            lastPlayedBot = bot;

            if (goodMove) {
                bot.setValidMoves(bot.getValidMoves() + 1);
            }

            bot.setRounds(bot.getRounds() + 1);

            if (goodMove) {
                lastMoveLog(bot);
            }

            syncUpdate(() -> {
                map.updateBots();
                showObjectsUI();
            });

            if (map.isGameOver()) {
                break;
            }

            if (!map.getObstacles().isEmpty()) {
                executeObstaclesActions();
            }
            lastTurn = currentTurn;
        }
    }

    private boolean isGameOver(Bot bot) {
        return map.isGameOver() || !isBotActive(bot);
    }

    private void executeObstaclesActions() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException _) {
            showErrorPane("Erro ao pausar thread.");
        }

        syncUpdate(() -> {
            try {
                map.obstacleAction(this);
                map.updateBots();
                showObjectsUI();
            } catch (InvalidMoveException | InvalidInputException e) {
                e.printStackTrace();
            }
        });
    }

    private void lastMoveLog(Bot bot) {
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

        Platform.runLater(() -> createLogLabel(bot.getType() + " se moveu para " + lastMove));
    }

    private boolean moveBot(Bot bot, boolean goodMove) {
        try {
            Thread.sleep(600);

            switch (bot) {
                case RandomBot randomBot ->
                    randomBot.move(0);
                case SmartBot smartBot ->
                    smartBot.move(0);
                default -> {
                    handleInputMove(bot);
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

            Platform.runLater(() -> createLogLabel(bot.getType() + " fez um movimento inválido para " + lastMove));

            goodMove = false;
        } catch (InvalidInputException | InterruptedException e) {
            goodMove = false;
        }
        return goodMove;
    }

    private boolean isBotActive(Bot bot) {
        boolean othersInacive = true;
        for (Bot botCheck : map.getBots()) {
            if (!botCheck.equals(bot) && botCheck.isActive()) {
                othersInacive = false;
            }
        }
        if (bot.equals(lastPlayedBot) && lastTurn == currentTurn) {
            return false;
        }
        return !(((bot.equals(lastPlayedBot) && !othersInacive) && map.getBots().size() > 1) || !bot.isActive());
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

    private void handleInputMove(Bot bot) {
        String[] possibleInputs = {"up", "down", "left", "right", "1", "2", "3", "4"};
        int goodInput = -1;
        boolean moved = false;
        HBox playerHBox = (HBox) tabletopAnchorPane.lookup("#playerHBox");
        TextField moveTextField = (TextField) tabletopAnchorPane.lookup("#moveTextField");
        while (!moved) {
            playerHBox.setDisable(false);
            pause();

            goodInput = checkInput(possibleInputs, goodInput, moveTextField);

            if (goodInput != -1) {
                moved = TabletopController.this.move(bot, goodInput, playerHBox, moveTextField);
                continue;
            }
            Platform.runLater(() -> showErrorPane(new InvalidInputException().toString()));
        }

        playerHBox.setDisable(true);
        resume();
    }

    private boolean move(Bot bot, int goodInput, HBox playerHBox, TextField moveTextField) {
        boolean moved;
        try {
            bot.move(moveTextField.getText());
            moved = true;
        } catch (InvalidInputException ex) {
            moved = move(bot, goodInput);
            playerHBox.setDisable(true);
        } catch (InvalidMoveException e) {
            playerHBox.setDisable(true);
            Platform.runLater(() -> showErrorPane(e.toString()));
            moved = false;
        }
        return moved;
    }

    private boolean move(Bot bot, int goodInput) {
        boolean moved;
        try {
            bot.move(goodInput - 3);
            moved = true;
        } catch (InvalidMoveException | InvalidInputException e) {
            moved = false;
            Platform.runLater(() -> showErrorPane(e.toString()));
        }
        return moved;
    }

    private int checkInput(String[] possibleInputs, int goodInput, TextField moveTextField) {
        for (int i = 0; i < possibleInputs.length; i++) {
            if (moveTextField.getText().equals(possibleInputs[i])) {
                goodInput = i;
            }
        }
        return goodInput;
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
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/trabrobotartaruga/robo_tartaruga/tela_final.fxml"));
                Parent root = loader.load();
                Stage newStage = new Stage();
                FinalScreenController finalScreenController = loader.getController();
                finalScreenController.buildScreen(bots, winnerBot);
                newStage.setTitle("Resultado Final");
                newStage.setScene(new Scene(root));
                Stage currentstage = (Stage) ((Node) tabletopAnchorPane).getScene().getWindow();
                currentstage.close();
                Platform.runLater(() -> newStage.show());
            } catch (IOException _) {
                showErrorPane("Erro ao carregar a tela final.");
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
        } catch (IOException _) {
            Thread.currentThread().interrupt();
        }
    }

    private void showBotTurnLabel(Bot bot) {
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

    private void showObjectsUI() {
        String assetsPath = "/com/trabrobotartaruga/robo_tartaruga/assets/";
        for (int i = 0; i < map.getX(); i++) {
            for (int j = 0; j < map.getY(); j++) {
                showObjectsImage(i, j, assetsPath);
                styleBotsImage(i, j, assetsPath);
            }
        }
    }

    private void showObjectsImage(int i, int j, String assetsPath) {
        FlowPane gridCell = (FlowPane) gameGrid.lookup("#gridCell" + i + "" + j);
        gridCell.getChildren().clear();

        if (map.getFood().getPosX() == j && map.getFood().getPosY() == i) {
            ImageView foodImage = new ImageView(
                    new Image(getClass().getResourceAsStream(assetsPath + "food.png"), 100, 100, false, false));
            gridCell.getChildren().add(foodImage);
            return;
        }

        showObstaclesImage(i, j, assetsPath, gridCell);
    }

    private void showObstaclesImage(int i, int j, String assetsPath, FlowPane gridCell) {
        for (Obstacle obstacle : map.getObstacles()) {
            if (obstacle.getPosX() == j && obstacle.getPosY() == i) {
                ImageView obstacleImage = new ImageView(
                        new Image(getClass().getResourceAsStream(assetsPath + obstacle.getType() + ".png"), 100, 100,
                                false, false));
                gridCell.getChildren().add(obstacleImage);
            }
        }
    }

    private void styleBotsImage(int i, int j, String assetsPath) {
        if (!map.getPositions().get(i).get(j).getObjects().isEmpty()) {
            for (Object object : map.getPositions().get(i).get(j).getObjects()) {
                FlowPane gridCell = (FlowPane) gameGrid.lookup("#gridCell" + i + "" + j);
                DropShadow dropShadow = new DropShadow();
                ColorAdjust monochrome = new ColorAdjust();
                monochrome.setSaturation(-1);
                dropShadow.setRadius(1.0);
                dropShadow.setOffsetX(10.0);
                dropShadow.setOffsetY(10.0);
                if (object instanceof Bot bot) {
                    ImageView image = new ImageView(new Image(
                            getClass().getResourceAsStream(
                                    assetsPath + bot.getTypeSnakeCase() + ".png"),
                            70, 70, false, false));
                    dropShadow.setColor(Color.valueOf(bot.getColor()));
                    if (!bot.isActive()) {
                        dropShadow.setInput(monochrome);
                    }
                    image.setEffect(dropShadow);
                    gridCell.getChildren().add(image);
                }
            }
        }
    }
}
