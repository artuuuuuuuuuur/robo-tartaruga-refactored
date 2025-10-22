package com.trabrobotartaruga.robo_tartaruga;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;

import com.trabrobotartaruga.robo_tartaruga.classes.Map;
import com.trabrobotartaruga.robo_tartaruga.classes.bot.Bot;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PlayerScreenController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public void playGame(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/trabrobotartaruga/robo_tartaruga/tabuleiro.fxml"));
        Parent root = loader.load();
        
        List<Bot> bots = new CopyOnWriteArrayList<>();
        bots.add(new Bot("blue", 4, 4));
        bots.add(new Bot("pink", 4, 4));
        
        Map map = new Map(4, 4, bots);
        
        TabletopController tabletopController = loader.getController();
        tabletopController.load(map);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
