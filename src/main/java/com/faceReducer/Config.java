package com.faceReducer;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;

import java.io.File;

public class Config {
    public static boolean isDebug = false;

    public static final DoubleProperty progressNum = new SimpleDoubleProperty();
    public static final StringProperty labelText = new SimpleStringProperty();


    public static long deleted = 0;


    public static File srcModelFile;

    public static ProgressBar progressBar;
}
