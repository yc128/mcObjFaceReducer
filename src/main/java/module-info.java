module com.example.facereducergui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;


    opens com.faceReducerGUI to javafx.fxml;
    exports com.faceReducerGUI;
}