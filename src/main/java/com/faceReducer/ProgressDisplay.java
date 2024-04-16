package com.faceReducer;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.ProgressBar;

import java.util.List;

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

    public void setProgressSum(ObjIO obj){
        faceSum = 0;
        counter = 0;
        for(List<SingleFace> faceList: obj.getGroupedFaceList()){
            for(SingleFace f:faceList){
                if(!f.isDeleted()){
                    faceSum++;
                }
            }
        }
    }


    public void updateCounter(){
        counter++;
//        System.out.println(Config.progressBar.getProgress());
        progressNum.set(counter/faceSum);
    }


}
