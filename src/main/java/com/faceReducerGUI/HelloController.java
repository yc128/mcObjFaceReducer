package com.faceReducerGUI;

import com.faceReducer.Config;
import com.faceReducer.ProcessMain;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
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
    private TextField outputPathTF;
    @FXML
    private Label fileStatusLabel;
    @FXML
    private Button startButton;
    @FXML
    private ProgressBar progressBar1;


    void initialize(){
        Config.progressBar = progressBar1;
        fileStatusLabel.textProperty().bindBidirectional(Config.labelText);
        startButton.disableProperty().bind(Config.isStartDisable);
        Config.isStartDisable.set(true);

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
            Config.isFileChoose = true;
            Config.isStartDisable.set(!Config.isOutputPathChoose);
            Config.labelText.set("File loaded");
        }else{
            Config.labelText.set("Invalid file path!");
            Config.isFileChoose = false;
            Config.isStartDisable.set(true);
        }
    }

    @FXML
    protected void onChooseOutputPathClick(){
        Stage stage = (Stage) rootLayout.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Config.outputPath = directoryChooser.showDialog(stage);
        Config.isOutputPathChoose = true;
        Config.isStartDisable.set(!Config.isFileChoose);
        outputPathTF.setText(Config.outputPath.getPath());

    }

    @FXML
    protected void onStartButtonClick(){
        ProcessMain processMain = new ProcessMain(fileStatusLabel);
        processMain.start();
    }


}