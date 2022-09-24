package com.faceReducer;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class ProcessMain extends Thread{
    Label statusLabel;

    public ProcessMain(Label l){
        statusLabel = l;
    }

    public void run(){
        if(statusLabel == null){
            System.out.println("statusLabel not found！");
            System.exit(6);
        }
        ObjIO test = new ObjIO(Config.srcModelFile.getPath());
        test.readVertex();
        test.readFace();


        FaceReducerOperator fro = new FaceReducerOperator(test);
        setText("face reducing1...");
        fro.faceReduce();
        setText("face reducing2...");
        fro.faceReduce();
        setText("face reducing3...");
        fro.faceReduce();
//        System.out.println("inner face removing...");
//        fro.innerFaceCheck();
        VertexReducerOperator vro = new VertexReducerOperator(test);
        setText("vertex reducing...");
        vro.vertexReduction();

        if(Config.isDebug){
            System.out.println("face(reduced): ");
            test.printAllFaceVertex();
        }

        setText("file writing...");
        test.outputFile();
        setText(Config.deleted+" faces is deleted");
        setText("completed");

    }

    private void setText(String s){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Config.labelText.set(s);
            }
        });
    }
}
