package com.faceReducer;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class ProcessMain extends Thread{
    Label statusLabel;
    private int totalProgress = 5;
    private int currProgress = 1;

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
        setText("face reducing1..."+"("+currProgress+"/"+totalProgress+")");
        fro.faceReduce();
        currProgress++;
        setText("face reducing2..."+"("+currProgress+"/"+totalProgress+")");
        fro.faceReduce();
        currProgress++;
        setText("face reducing3..."+"("+currProgress+"/"+totalProgress+")");
        fro.faceReduce();
        currProgress++;
//        System.out.println("inner face removing...");
//        fro.innerFaceCheck();
        VertexReducerOperator vro = new VertexReducerOperator(test);
        setText("vertex reducing..."+"("+currProgress+"/"+totalProgress+")");
        vro.vertexReduction();
        currProgress++;

        if(Config.isDebug){
            System.out.println("face(reduced): ");
            test.printAllFaceVertex();
        }

        setText("file writing..."+"("+currProgress+"/"+totalProgress+")");
        test.outputFile();
        currProgress++;
        setText(Config.deleted+" faces is deleted");
        setText("completed"+"("+currProgress+"/"+totalProgress+")");

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
