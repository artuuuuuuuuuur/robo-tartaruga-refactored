package com.trabrobotartaruga.robo_tartaruga;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;

import com.trabrobotartaruga.robo_tartaruga.classes.Food;
import com.trabrobotartaruga.robo_tartaruga.classes.Map;
import com.trabrobotartaruga.robo_tartaruga.classes.bot.Bot;
import com.trabrobotartaruga.robo_tartaruga.classes.bot.RandomBot;
import com.trabrobotartaruga.robo_tartaruga.classes.bot.SmartBot;
import com.trabrobotartaruga.robo_tartaruga.classes.obstacle.Bomb;
import com.trabrobotartaruga.robo_tartaruga.classes.obstacle.Obstacle;
import com.trabrobotartaruga.robo_tartaruga.classes.obstacle.Stone;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PlayerScreenController implements Initializable {

    private final List<Bot> bots = new CopyOnWriteArrayList<>();
    private Food food;
    private final List<Obstacle> obstacles = new CopyOnWriteArrayList<>();

    @FXML
    GridPane previewGrid;
    @FXML
    AnchorPane gameModeAnchorPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::buildPreviewGrid);
        Platform.runLater(() -> {
            for (int i = 0; i < 2; i++) {
                ComboBox<String> chooseBotComboBox = (ComboBox) gameModeAnchorPane.lookup("#chooseBot" + (i + 1) + "ComboBox");
                chooseBotComboBox.getItems().add("Robô normal");
                chooseBotComboBox.getItems().add("Robô aleatório");
                chooseBotComboBox.getItems().add("Robô inteligente");
            }
        });
    }

    public void goBack(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/trabrobotartaruga/robo_tartaruga/tela_inicial.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void playGame(Event event) throws IOException {
        CheckBox oneWinnerCheckBox = (CheckBox) gameModeAnchorPane.lookup("#oneWinnerCheckBox");
        String botColor1 = "";
        String botColor2 = "";
        String botType1 = "";
        String botType2 = "";
        for (int i = 0; i < 2; i++) {
            ComboBox<String> chooseBotComboBox = (ComboBox) gameModeAnchorPane.lookup("#chooseBot" + (i + 1) + "ComboBox");
            ColorPicker colorBotPicker = (ColorPicker) gameModeAnchorPane.lookup("#colorBot" + (i + 1) + "Picker");

            if (i == 0) {
                if (chooseBotComboBox.getValue() == null || colorBotPicker.getValue() == null) {
                    errorMessage("Escolha uma cor e o tipo do robô " + (i + 1) + ".");
                    return;
                }
            }

            if (i == 0) {
                botColor1 = colorBotPicker.getValue().toString();
                botType1 = chooseBotComboBox.getValue();
            } else {
                botColor2 = colorBotPicker.getValue().toString();
                botType2 = chooseBotComboBox.getValue();
            }

        }

        if (botColor1.equals(botColor2)) {
            errorMessage("Os robôs precisam ter cores diferentes.");
            return;
        }

        if (food == null) {
            errorMessage("Insira o alimento em alguma posição.");
            return;
        }
        for (Obstacle obstacle1 : obstacles) {
            for (Obstacle obstacle2 : obstacles) {
                if (obstacles.indexOf(obstacle1) != obstacles.indexOf(obstacle2)) {
                    if (obstacle1.getPosX() == obstacle2.getPosX() && obstacle1.getPosY() == obstacle2.getPosY()) {
                        errorMessage("Não coloque 2 objetos na mesma posição. Limpe o preview e tente novamente.");
                        return;
                    }
                }
            }
            if (obstacle1.getPosX() == food.getPosX() && obstacle1.getPosY() == food.getPosY()) {
                errorMessage("Cada posição só pode ter 1 objeto. Limpe o preview e tente novamente.");
                return;
            }
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/trabrobotartaruga/robo_tartaruga/tabuleiro.fxml"));
        Parent root = loader.load();

        bots.add(botType1.equals("Robô normal") ? new Bot(botColor1, 4, 4) : (botType1.equals("Robô inteligente") ? new SmartBot(botColor1, 4, 4) : new RandomBot(botColor1, 4, 4)));

        if (botType2 != null) {
            bots.add(botType2.equals("Robô normal") ? new Bot(botColor2, 4, 4) : (botType2.equals("Robô inteligente") ? new SmartBot(botColor2, 4, 4) : new RandomBot(botColor2, 4, 4)));
        }

        Map map = new Map(4, 4, bots, food, obstacles, oneWinnerCheckBox.isSelected());

        TabletopController tabletopController = loader.getController();
        tabletopController.load(map);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void clearObjects() {
        if (!obstacles.isEmpty() || food != null) {
            obstacles.clear();
            food = null;

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    Button gridButton = (Button) previewGrid.lookup("#gridButton" + i + "" + j);
                    gridButton.setBackground(null);
                }
            }
        } else {
            errorMessage("O preview já está limpo.");
        }
    }

    private void errorMessage(String message) {
        try {
            Stage stage = new Stage();
            Scene scene;
            scene = new Scene(new FXMLLoader(App.class.getResource("error.fxml")).load());
            stage.initModality(Modality.APPLICATION_MODAL);
            Label warningLabel = (Label) scene.lookup("#warningLabel");
            warningLabel.setText(message);
            Button okButton = (Button) scene.lookup("#okButton");
            okButton.setOnAction(e -> {
                stage.close();
            });
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildPreviewGrid() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Button gridButton = (Button) previewGrid.lookup("#gridButton" + i + "" + j);
                final int i_temp = i;
                final int j_temp = j;
                if (i == 0 && j == 0) {
                    gridButton.setOnAction(eh -> {
                        errorMessage("Você não pode inserir objetos na origem.");
                    });
                } else {
                    gridButton.setOnAction(eh -> {
                        try {
                            Stage stage = new Stage();
                            Scene scene;
                            scene = new Scene(new FXMLLoader(App.class.getResource("choose_object.fxml")).load());
                            stage.initModality(Modality.APPLICATION_MODAL);

                            Button foodButton = (Button) scene.lookup("#foodButton");
                            Button stoneButton = (Button) scene.lookup("#stoneButton");
                            Button bombButton = (Button) scene.lookup("#bombButton");

                            BackgroundSize backgroundSize = new BackgroundSize(85, 85, false, false, false, false);
                            Background foodBackground = new Background(new BackgroundImage(
                                    new Image(getClass()
                                            .getResourceAsStream("/com/trabrobotartaruga/robo_tartaruga/assets/food.png")),
                                    null, null, null, backgroundSize));
                            Background stoneBackground = new Background(new BackgroundImage(
                                    new Image(getClass()
                                            .getResourceAsStream("/com/trabrobotartaruga/robo_tartaruga/assets/stone.png")),
                                    null, null, null, backgroundSize));
                            Background bombBackground = new Background(new BackgroundImage(
                                    new Image(getClass()
                                            .getResourceAsStream("/com/trabrobotartaruga/robo_tartaruga/assets/bomb.png")),
                                    null, null, null, backgroundSize));
                            foodButton.setOnAction(e -> {
                                if (food != null) {
                                    for (int k = 0; k < 4; k++) {
                                        for (int l = 0; l < 4; l++) {
                                            if (k == food.getPosX() && l == food.getPosY()) {
                                                Button lastFoodButton = (Button) previewGrid.lookup("#gridButton" + l + "" + k);
                                                lastFoodButton.setBackground(null);
                                            }
                                        }
                                    }
                                }
                                food = new Food(j_temp, i_temp);
                                gridButton.setBackground(foodBackground);
                                stage.close();
                            });
                            stoneButton.setOnAction(e -> {
                                int id = obstacles.size();
                                obstacles.add(new Stone(id, j_temp, i_temp));
                                gridButton.setBackground(stoneBackground);
                                stage.close();
                            });
                            bombButton.setOnAction(e -> {
                                int id = obstacles.size();
                                obstacles.add(new Bomb(id, j_temp, i_temp));
                                gridButton.setBackground(bombBackground);
                                stage.close();
                            });
                            foodButton.setBackground(foodBackground);
                            stoneButton.setBackground(stoneBackground);
                            bombButton.setBackground(bombBackground);

                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException ex) {
                        }
                    });
                }
            }
        }
    }
}
