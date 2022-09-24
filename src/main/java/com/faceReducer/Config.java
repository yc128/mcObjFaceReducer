package com.faceReducer;

import javafx.beans.property.*;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;

import java.io.File;

public class Config {
    public static boolean isDebug = false;

    public static final DoubleProperty progressNum = new SimpleDoubleProperty();
    public static final StringProperty labelText = new SimpleStringProperty();
    public static final BooleanProperty isStartDisable = new SimpleBooleanProperty();
    public static boolean isFileChoose = false;
    public static boolean isOutputPathChoose = false;



    public static long deleted = 0;


    public static File srcModelFile;
    public static File outputPath;

    public static ProgressBar progressBar;



}
