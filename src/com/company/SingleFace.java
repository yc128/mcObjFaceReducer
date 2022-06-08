package com.company;

import java.util.ArrayList;
import java.util.List;

public class SingleFace {
    public List<Integer> vertexIndexList = new ArrayList<Integer>();
    public List<Integer> vertexTextureIndexList = new ArrayList<Integer>();
    public List<Integer> vertexNormalIndexList = new ArrayList<Integer>();

    private boolean isDeleted = false;

    public boolean isDeleted(){return isDeleted;}
    public void deleteFace(){isDeleted = true;}

    public SingleFace(){}

    public void addVertex(String vInfo){
        String[] strArr = vInfo.split("/");

        if(strArr.length > 0){
            vertexIndexList.add(Integer.parseInt(strArr[0]));
        }else{
            return;
        }

        if(strArr.length > 1){
            vertexTextureIndexList.add(Integer.parseInt(strArr[1]));
        }else{
            vertexTextureIndexList.add(-1);
        }

        if(strArr.length > 2){
            vertexNormalIndexList.add(Integer.parseInt(strArr[2]));
        }else{
            vertexNormalIndexList.add(-1);
        }

    }

    public void printVertexIndex(){
        for(int i:vertexIndexList){
            System.out.printf(i+", ");
        }
        System.out.println();
    }

    /**
     * replace the repeated vertex to other vertex in face merged.
     * @param face
     * @param obj
     */
    public void mergeFace(SingleFace face, ObjIO obj){
        List<Integer> deletedVertex = new ArrayList<>();
        //find the repeated vertex and store them.
        for(int i = 0; i < face.vertexIndexList.size(); i++){
            for(int j = 0; j < vertexIndexList.size(); j++){
                if(face.vertexIndexList.get(i) == vertexIndexList.get(j)){
                    deletedVertex.add(vertexIndexList.get(j));
                }
            }
        }

        for(int v: vertexIndexList){
            if(deletedVertex.contains(v)){
                for(int newV: face.vertexIndexList){
                    if(!deletedVertex.contains(newV)){
                        int numOfEqAxis = 0;
                        List<Double> vPos = obj.getVertexByIndex(v);
                        List<Double> newVPos = obj.getVertexByIndex(newV);
                        for(int i = 0; i < 3; i++){
                            if(Math.abs(vPos.get(i) - newVPos.get(i))<0.00001){
                                numOfEqAxis++;
                            }
                        }
                        System.out.println("numOfEqAx: "+ numOfEqAxis);
                        if(numOfEqAxis == 2){
                            int replaceIndex = vertexIndexList.indexOf(v);
                            vertexIndexList.set(replaceIndex, newV);
                        }
                    }
                }
            }
        }

        face.deleteFace();

    }

}
