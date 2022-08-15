package com.faceReducer;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.ProgressBar;

public class ProgressDisplay {
    private final DoubleProperty progressNum = new SimpleDoubleProperty();
    public double faceSum = 0;
    private double counter = 0;
    public ProgressDisplay(){
        if(Config.progressBar==null){
            System.exit(11);
        }
        Config.progressBar.progressProperty().bind(progressNum);
    }


    public void updateCounter(){
        counter++;
//        System.out.println(Config.progressBar.getProgress());
        progressNum.set(counter/faceSum);
    }


}
