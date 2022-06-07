package com.company;

import java.util.ArrayList;
import java.util.List;

public class SingleFace {
    List<Integer> vertexIndexList = new ArrayList<Integer>();
    List<Integer> vertexTextureIndexList = new ArrayList<Integer>();
    List<Integer> vertexNormalIndexList = new ArrayList<Integer>();

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

}
