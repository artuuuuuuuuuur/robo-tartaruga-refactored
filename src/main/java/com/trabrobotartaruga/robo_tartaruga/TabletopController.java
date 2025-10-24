package com.trabrobotartaruga.robo_tartaruga;

import java.util.concurrent.Semaphore;

import com.trabrobotartaruga.robo_tartaruga.classes.Map;
import com.trabrobotartaruga.robo_tartaruga.classes.bot.Bot;
import com.trabrobotartaruga.robo_tartaruga.exceptions.InvalidMoveException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class TabletopController {

    private Map map;
    private final Semaphore semaphore = new Semaphore(0);

    @FXML
    GridPane gameGrid;

    public void load(@SuppressWarnings("exports") Map map) {
        this.map = map;

        showBots();
        play();
    }

    private void play() {
        new Thread(() -> {
            while (!map.isFoodFound()) {
                Platform.runLater(() -> map.updateBots());
                for (Bot bot : map.getBots()) {
                    pause();
                    try {
                        bot.move(4);
                    } catch (InvalidMoveException e) {
                        e.printStackTrace();
                    }
                    Platform.runLater(() -> {
                        map.updateBots();
                        showBots();
                    });
                }
            }

        }).start();
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

    private void showBots() {
        for (int i = 0; i < map.getX(); i++) {
            for (int j = 0; j < map.getY(); j++) {
                FlowPane gridCell = (FlowPane) gameGrid.lookup("#gridCell" + i + "" + j);
                gridCell.getChildren().clear();
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
