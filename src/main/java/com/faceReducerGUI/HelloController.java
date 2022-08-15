package com.faceReducerGUI;

import com.faceReducer.Config;
import com.faceReducer.ProcessMain;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private VBox rootLayout;
    @FXML
    private TextField filePathTF;
    @FXML
    private Label fileStatusLabel;
    @FXML
    private Button startButton;
    @FXML
    private ProgressBar progressBar1;


    void initialize(){
        Config.progressBar = progressBar1;
        fileStatusLabel.textProperty().bindBidirectional(Config.labelText);
    }

    @FXML
    protected void onChooseFileButtonClick() {
        Stage stage = (Stage) rootLayout.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("obj file", "*.obj"));
        File file = fileChooser.showOpenDialog(stage);
        if(file != null && file.getName().endsWith(".obj")){
            Config.srcModelFile = file;
            filePathTF.setText(Config.srcModelFile.getPath());
            Config.labelText.set("File loaded");
            startButton.setDisable(false);


        }else{
            Config.labelText.set("Invalid file path!");
        }
    }

    @FXML
    protected void onStartButtonClick(){
        ProcessMain processMain = new ProcessMain(fileStatusLabel);
        processMain.start();
    }


}