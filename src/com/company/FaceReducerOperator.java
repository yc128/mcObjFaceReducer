package com.company;

import java.util.List;

public class FaceReducerOperator {
    ObjIO obj;
    public FaceReducerOperator(ObjIO inObj){
        obj = inObj;
    }

    public void faceReduce(){
        for(List<SingleFace> faceList: obj.getGroupedFaceList()){
            faceReduceInSingleGroup(faceList);
        }
    }

    /**
     * Compare each faces in group to find faces available to merge.
     * @param faceList
     */
    private void faceReduceInSingleGroup(List<SingleFace> faceList){
        for(int i = 0; i < faceList.size(); i++){
            SingleFace currFace = faceList.get(i);
            if(currFace.isDeleted()){continue;}
            for(int j = i+1; j < faceList.size(); j++){
                SingleFace comparedFace = faceList.get(j);
                if(comparedFace.isDeleted()){continue;}
                System.out.println("comparing: "+isInSamePlane(currFace, comparedFace)+", "+numberOfSameVertex(currFace, comparedFace));
                if(isInSamePlane(currFace, comparedFace) &&
                        (numberOfSameVertex(currFace, comparedFace) >= 2)){
                    System.out.println("face reducing: "+i+" merged "+j);
                    currFace.mergeFace(comparedFace, obj);
                }
            }
        }
    }

    /**
     * this method is to ensure that two faces are in same surface.
     * @param f1
     * @param f2
     * @return
     */
    private boolean isInSamePlane(SingleFace f1, SingleFace f2){
        boolean x = true;
        boolean y = true;
        boolean z = true;
        for(int i = 0; i < f1.vertexIndexList.size(); i++){
            List<Double> v1 = obj.getVertexByIndex(f1.vertexIndexList.get(i));
            for(int j = 0; j < f2.vertexIndexList.size(); j++){
                List<Double> v2 = obj.getVertexByIndex(f2.vertexIndexList.get(j));
//                System.out.println(i+"]"+v1.get(0)+", "+v1.get(1)+", "+v1.get(2)+" with: "+j+"]"+v2.get(0)+", "+v2.get(1)+", "+v2.get(2)+", ");
                x = x&&(Math.abs(v1.get(0) - v2.get(0))<0.000001);
                y = y&&(Math.abs(v1.get(1) - v2.get(1))<0.000001);
                z = z&&(Math.abs(v1.get(2) - v2.get(2))<0.000001);
            }
        }
//        System.out.println("func end:"+"x-"+x+" y-"+y+" z-"+z);
        return x||y||z;
    }




    /**
     * calculate the number of vertex that is same in two faces
     * @param f1
     * @param f2
     * @return
     */
    private int numberOfSameVertex(SingleFace f1, SingleFace f2){
        int num = 0;
        for(int i = 0; i < f1.vertexIndexList.size(); i++){
            for(int j = 0; j < f2.vertexIndexList.size(); j++){
                if(f1.vertexIndexList.get(i) == f2.vertexIndexList.get(j)){
                    num++;
                }
            }
        }
        return num;
    }
}
