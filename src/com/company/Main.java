package com.company;

public class Main {

    public static void main(String[] args) {

        ObjIO test = new ObjIO("D:\\模型\\testObj.obj");
        test.readVertex();
        System.out.println("vertex: ");
        test.printAllVertex();
        test.readFace();
        System.out.println("face: ");
        test.printAllFaceVertex();

        FaceReducerOperator fro = new FaceReducerOperator(test);
        fro.faceReduce();
        fro.faceReduce();

        System.out.println("face(reduced): ");
        test.printAllFaceVertex();

        test.outputFile();

    }
}
