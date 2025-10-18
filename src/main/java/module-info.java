module com.trabrobotartaruga.robo_tartaruga {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.trabrobotartaruga.robo_tartaruga to javafx.fxml;
    exports com.trabrobotartaruga.robo_tartaruga;
}
