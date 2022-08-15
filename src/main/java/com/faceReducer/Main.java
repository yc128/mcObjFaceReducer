package com.faceReducer;

public class Main {

    public static void main(String[] args) {

        ObjIO test = new ObjIO("D:\\MCTools\\MCModels\\N700SH.obj");
        test.readVertex();
        test.readFace();

        System.out.println("file loaded");

        FaceReducerOperator fro = new FaceReducerOperator(test);
        System.out.println("face reducing1...");
        fro.faceReduce();
        System.out.println("face reducing2...");
        fro.faceReduce();
        System.out.println("face reducing3...");
        fro.faceReduce();
//        System.out.println("face reducing4...");
//        fro.faceReduce();
//        System.out.println("inner face removing...");
//        fro.innerFaceCheck();
        VertexReducerOperator vro = new VertexReducerOperator(test);
        System.out.println("vertex reducing...");
        vro.vertexReduction();

        if(Config.isDebug){
            System.out.println("face(reduced): ");
            test.printAllFaceVertex();
        }

        System.out.println("file writing...");
        test.outputFile();
        System.out.println(Config.deleted+" faces is deleted");
        System.out.println("completed");

    }
}
