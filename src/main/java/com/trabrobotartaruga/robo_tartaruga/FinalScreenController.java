package com.trabrobotartaruga.robo_tartaruga;

import java.io.IOException;
import java.util.List;

import com.trabrobotartaruga.robo_tartaruga.classes.bot.Bot;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class FinalScreenController {

    @FXML
    Label h1Label;
    @FXML
    Label h2Label;
    @FXML
    GridPane rankGridPane;

    public void build(@SuppressWarnings("exports") List<Bot> bots, @SuppressWarnings("exports") List<Bot> winnerBots) {
        int slot = 1;
        if (winnerBots.isEmpty()) {
            h1Label.setText("OOPS!");
            h1Label.setTextFill(Paint.valueOf("red"));
            h2Label.setText("Todos os robôs explodiram");
        } else {
            h1Label.setText("PARABÉNS!");
            h1Label.setTextFill(Paint.valueOf("green"));
            if (winnerBots.size() == 1) {
                h2Label.setText("O robô " + colorDecoder(winnerBots.get(0).getColor()) + " achou o alimento");
            } else {
                h2Label.setText("Todos os robôs acharam o alimento");
            }

            for (Bot winnerBot : winnerBots) {
                Label botType = (Label) rankGridPane.lookup("#botTypeLabel" + (slot));
                VBox botColorVBox = (VBox) rankGridPane.lookup("#botColorVBox" + (slot));
                Label botColor = (Label) rankGridPane.lookup("#botColorLabel" + (slot));
                Label validMoves = (Label) rankGridPane.lookup("#validMovesLabel" + (slot));
                Label invalidMoves = (Label) rankGridPane.lookup("#invalidMovesLabel" + (slot));
                Label rounds = (Label) rankGridPane.lookup("#roundsLabel" + (slot));
                Label foundFood = (Label) rankGridPane.lookup("#foundFoodLabel" + (slot));

                botType.setText(winnerBot.getType().replace("Robô ", ""));

                botColor.setText(colorDecoder(winnerBot.getColor()));
                botColorVBox.setBackground(Background.fill(Paint.valueOf(winnerBot.getColor())));
                validMoves.setText(String.valueOf(winnerBot.getValidMoves()));
                invalidMoves.setText(String.valueOf(winnerBot.getInvalidMoves()));
                rounds.setText(String.valueOf(winnerBot.getRounds()));
                foundFood.setText("Sim");
                slot++;
            }
        }

        if (slot <= 2) {
            for (Bot bot : bots) {
                boolean isWinner = false;
                for (Bot winnerBot : winnerBots) {
                    if (bot.equals(winnerBot)) {
                        isWinner = true;
                    }
                }
                if (!isWinner) {
                    Label botType = (Label) rankGridPane.lookup("#botTypeLabel" + (slot));
                    VBox botColorVBox = (VBox) rankGridPane.lookup("#botColorVBox" + (slot));
                    Label botColor = (Label) rankGridPane.lookup("#botColorLabel" + (slot));
                    Label validMoves = (Label) rankGridPane.lookup("#validMovesLabel" + (slot));
                    Label invalidMoves = (Label) rankGridPane.lookup("#invalidMovesLabel" + (slot));
                    Label rounds = (Label) rankGridPane.lookup("#roundsLabel" + (slot));
                    Label foundFood = (Label) rankGridPane.lookup("#foundFoodLabel" + (slot));

                    botType.setText(bot.getType().replace("Robô ", ""));

                    botColor.setText(colorDecoder(bot.getColor()));
                    botColorVBox.setBackground(Background.fill(Paint.valueOf(bot.getColor())));
                    validMoves.setText(String.valueOf(bot.getValidMoves()));
                    invalidMoves.setText(String.valueOf(bot.getInvalidMoves()));
                    rounds.setText(String.valueOf(bot.getRounds()));
                    foundFood.setText("Não");
                }
            }
        }
    }

    public void goToHome(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/trabrobotartaruga/robo_tartaruga/tela_inicial.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private String colorDecoder(String hexColor) {
        switch (hexColor) {
            case "0xffffffff" -> {
                return "branco";
            }
            case "0x000000ff" -> {
                return "preto";
            }
            case "0x00ff00ff" -> {
                return "verde";
            }
            case "0xff0000ff" -> {
                return "vermelho";
            }
            case "0xffff00ff" -> {
                return "amarelo";
            }
            case "0x0000ffff" -> {
                return "azul";
            }
            default -> {
                return hexColor;
            }
        }
    }
}
