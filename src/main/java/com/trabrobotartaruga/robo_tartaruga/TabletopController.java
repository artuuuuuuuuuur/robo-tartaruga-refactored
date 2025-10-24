package com.trabrobotartaruga.robo_tartaruga;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

import com.trabrobotartaruga.robo_tartaruga.classes.Map;
import com.trabrobotartaruga.robo_tartaruga.classes.bot.Bot;
import com.trabrobotartaruga.robo_tartaruga.classes.bot.RandomBot;
import com.trabrobotartaruga.robo_tartaruga.classes.bot.SmartBot;
import com.trabrobotartaruga.robo_tartaruga.classes.obstacle.Obstacle;
import com.trabrobotartaruga.robo_tartaruga.classes.obstacle.Stone;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidMoveException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class TabletopController {

    private Map map;
    private final Semaphore semaphore = new Semaphore(0);
    private Bot lastPlayedBot;

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
            while (!map.checkFoodFound()) {
                Platform.runLater(() -> map.updateBots());
                for (Bot bot : map.getBots()) {
                    if (bot.equals(lastPlayedBot)) {
                        continue;
                    }

                    try {
                        Thread.sleep(1500);
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
                        System.out.println(e.toString());
                    } catch (InterruptedException e) {
                    }

                    lastPlayedBot = bot;

                    Platform.runLater(() -> {
                        map.updateBots();
                        showBots();
                    });
                    if (map.checkFoodFound()) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(this::goToFinalScreen);
                    }
                }
            }
        }).start();
    }

    private void move(Bot bot) throws InvalidMoveException {

        HBox playerHBox = (HBox) tabletopAnchorPane.lookup("#playerHBox");
        TextField moveTextField = (TextField) tabletopAnchorPane.lookup("#moveTextField");
        playerHBox.setDisable(false);
        pause();

        try {
            bot.move(moveTextField.getText());
        } catch (InvalidMoveException e) {
            playerHBox.setDisable(true);
            throw e;
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

    private void goToFinalScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/trabrobotartaruga/robo_tartaruga/tela_final.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) tabletopAnchorPane).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showBots() {
        for (int i = 0; i < map.getX(); i++) {
            for (int j = 0; j < map.getY(); j++) {
                FlowPane gridCell = (FlowPane) gameGrid.lookup("#gridCell" + i + "" + j);
                gridCell.getChildren().clear();
                if (map.getFood().getPosX() == j && map.getFood().getPosY() == i) {
                    gridCell.setBackground(Background.fill(Paint.valueOf("green")));
                }
                for (Obstacle obstacle : map.getObstacles()) {
                    if (obstacle.getPosX() == j && obstacle.getPosY() == i) {
                        if (obstacle instanceof Stone) {
                            gridCell.setBackground(Background.fill(Paint.valueOf("gray")));
                        } else {
                            gridCell.setBackground(Background.fill(Paint.valueOf("red")));
                        }
                    }
                }
            }
        }
        for (int i = 0; i < map.getX(); i++) {
            for (int j = 0; j < map.getY(); j++) {
                if (!map.getPositions().get(i).get(j).getObjects().isEmpty()) {
                    for (Object object : map.getPositions().get(i).get(j).getObjects()) {
                        if (object instanceof Bot actualBot) {
                            FlowPane gridCell = (FlowPane) gameGrid.lookup("#gridCell" + i + "" + j);
                            gridCell.getChildren().add(new Circle(20, Paint.valueOf(actualBot.getColor())));
                        }
                    }

                }
            }
        }
    }
}
