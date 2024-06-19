module com.example.sudokugame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.sudokugame to javafx.fxml;
    exports com.example.sudokugame;
    opens com.example.sudokugame.ui to javafx.fxml;
    exports com.example.sudokugame.ui;

}