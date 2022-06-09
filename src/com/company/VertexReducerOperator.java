package com.company;

import java.awt.desktop.SystemEventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VertexReducerOperator {
    ObjIO obj;

    private Map<Integer, Integer> usedVertexIndexMap = new HashMap<>();


    public VertexReducerOperator(ObjIO obj){
        this.obj = obj;
    }

    public void vertexReduction(){
        updateLinkedVertex();
        updateVertexList();
        updateVertexIndexOfFace();

    }

    /**
     * This function update vertex list, delete vertex no longer linked.
     * The function uses a hash map to store the indices of linked vertex
     */
    private void updateLinkedVertex(){
        usedVertexIndexMap.clear();
        List<List<SingleFace>> groupedFaceList = obj.getGroupedFaceList();
        for(List<SingleFace> faceList:groupedFaceList){
            for(SingleFace face:faceList){
                if(!face.isDeleted()){
                    for(int iv: face.vertexIndexList){
                        if(usedVertexIndexMap.get(iv) == null){
                            usedVertexIndexMap.put(iv,-1);
                        }
                    }
                }
            }
        }
    }

    /**
     * remove the unlinked vertex and update the new vertex index to the hash map.
     * After this function, the map should be: (previousIndex, currentIndex).
     */
    private void updateVertexList(){
        List<List<Double>> vertexList = obj.getVertexList();
        int removedCount = 0;
        for(int i = 1; i < vertexList.size(); i++){
            if(usedVertexIndexMap.get(i+removedCount) == null){
//                System.out.println("remove: "+i);
                vertexList.remove(i);
                removedCount++;
                i--;
            }else{
//                System.out.println("update index: "+ (i+removedCount)+" -> "+i);
                usedVertexIndexMap.put(i+removedCount, i);
            }
        }
    }


    /**
     * refresh the vertex indices of faces through the map.
     */
    private void updateVertexIndexOfFace(){
        List<List<SingleFace>> groupedFaceList = obj.getGroupedFaceList();
        for(List<SingleFace> faceList:groupedFaceList){
            for(SingleFace face:faceList){
                if(!face.isDeleted()){
                    for(int i = 0; i < face.vertexIndexList.size(); i++){
                        int vi = face.vertexIndexList.get(i);
                        if(usedVertexIndexMap.get(vi) == null || usedVertexIndexMap.get(vi) == -1){
                            System.out.println("vertex index not found");
                            System.exit(5);
                        }else{
                            face.vertexIndexList.set(i, usedVertexIndexMap.get(vi));
                        }
                    }
                }
            }
        }
    }
}
