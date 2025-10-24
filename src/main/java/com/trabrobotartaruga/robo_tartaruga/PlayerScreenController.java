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

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PlayerScreenController implements Initializable {

    private List<Bot> bots = new CopyOnWriteArrayList<>();
    private Food food;

    @FXML
    GridPane previewGrid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(this::buildPreviewGrid);
    }

    public void playGame(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/trabrobotartaruga/robo_tartaruga/tabuleiro.fxml"));
        Parent root = loader.load();

        bots.add(new SmartBot("blue", 4, 4));
        bots.add(new RandomBot("pink", 4, 4));

        Map map = new Map(4, 4, bots, food);

        TabletopController tabletopController = loader.getController();
        tabletopController.load(map);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void buildPreviewGrid() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Button gridButton = (Button) previewGrid.lookup("#gridButton" + i + "" + j);
                final int i_temp = i;
                final int j_temp = j;
                gridButton.setOnAction(eh -> {
                    try {
                        Stage stage = new Stage();
                        Scene scene;
                        scene = new Scene(new FXMLLoader(App.class.getResource("choose_object.fxml")).load());
                        stage.initModality(Modality.APPLICATION_MODAL);
                        
                        Button foodButton = (Button) scene.lookup("#foodButton");
                        Button stoneButton = (Button) scene.lookup("#stoneButton");
                        Button bombButton = (Button) scene.lookup("#bombButton");
                        
                        foodButton.setOnAction(e -> {
                            food = new Food(j_temp, i_temp);
                            stage.close();
                        });
                        stoneButton.setOnAction(e -> {
                        });
                        bombButton.setOnAction(e -> {
                        });
                        
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException ex) {
                    }
                });
            }
        }
    }
}
