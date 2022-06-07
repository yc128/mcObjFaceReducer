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

    public void faceReduceInSingleGroup(List<SingleFace> faceList){
        for(int i = 0; i < faceList.size(); i++){
            SingleFace currFace = faceList.get(i);
            for(int j = i+1; j < faceList.size(); j++){
                SingleFace comparedFace = faceList.get(j);
                if(isInSamePlane(currFace, comparedFace) &&
                        (numberOfSameVertex(currFace, comparedFace) >= 2)){
                    
                }
            }
        }
    }


    public boolean isInSamePlane(SingleFace f1, SingleFace f2){
        boolean x = true;
        boolean y = true;
        boolean z = true;
        for(int i = 0; i < f1.vertexIndexList.size(); i++){
            List<Double> v1 = obj.getVertexByIndex(f1.vertexIndexList.get(i));
            for(int j = 0; j < f2.vertexIndexList.size(); j++){
                List<Double> v2 = obj.getVertexByIndex(f2.vertexIndexList.get(j));
                x = x&&(v1.get(0) == v2.get(0));
                y = y&&(v1.get(1) == v2.get(1));
                z = z&&(v1.get(2) == v2.get(2));
            }
        }
        return x||y||z;
    }




    /**
     * calculate the number of vertex that is same in two faces
     * @param f1
     * @param f2
     * @return
     */
    public int numberOfSameVertex(SingleFace f1, SingleFace f2){
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
