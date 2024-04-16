package com.faceReducer;

import javafx.scene.control.ProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaceReducerOperator {
    ObjIO obj;
    ProgressDisplay progressDisplay;
    long sum = 0;
    long counter = 0;

    private Map<List<Double>, List<SingleFace>> vertexLinkedFaceMap = new HashMap<>();
    private List<List<Double>> usedVertexList = new ArrayList<>();

    public FaceReducerOperator(ObjIO inObj){
        obj = inObj;
        progressDisplay = new ProgressDisplay();
    }



    public void faceReduce(){
        progressDisplay.setProgressSum(obj);
        boolean isSlab = false;
        for(List<SingleFace> faceList: obj.getGroupedFaceList()){
            System.out.println("reducing: "+obj.getGroupName().get(obj.getGroupedFaceList().indexOf(faceList)));
            String name = obj.getGroupName().get(obj.getGroupedFaceList().indexOf(faceList));
            isSlab = name.contains("Slab")&&!name.contains("Double");
            faceReduceInSingleGroup2(faceList, isSlab);
        }
    }






    /**
     * calculate the number of vertex that is same in two faces
     * @param f1
     * @param f2
     * @return
     */
    private int numberOfSameVertex(SingleFace f1, SingleFace f2){
        int num = 0;
        for(int i:f1.vertexIndexList){
            if(f2.vertexIndexList.contains(i)){
                num++;
            }
        }
        return num;
    }


    /**
     * another attempt:
     * split faces into three subList through x, y or z. For example:
     * if all vertex of a face have the same value in x, it will be classified to x sublist.
     * then do searching only in every subList.
     * @param faceList
     */
    private void faceReduceInSingleGroup2(List<SingleFace> faceList, boolean isSlab){

        List<SingleFace> subX = new ArrayList<>();
        List<SingleFace> subY = new ArrayList<>();
        List<SingleFace> subZ = new ArrayList<>();
        for(SingleFace face:faceList){
            switch (faceClassifier(face)){
                case 0:
                    subX.add(face);
                    break;
                case 1:
                    subY.add(face);
                    break;
                case 2:
                    subZ.add(face);
                    break;
                default:
                    System.out.println(faceList.indexOf(face)+" can not be classified!");
            }
        }
        sum = (long) (subX.size()+1)*subX.size() + (long) (subY.size()+1) *subY.size() + (long) (subZ.size()+1)*subZ.size();
        sum = sum/2;
        counter = 0;
//        faceReduceInClassifiedList(subX, 0, isSlab);
//        faceReduceInClassifiedList(subY, 1, isSlab);
//        faceReduceInClassifiedList(subZ, 2, isSlab);

        sortFaceThroughCoordinate(subX, 0, isSlab);
        sortFaceThroughCoordinate(subY, 1, isSlab);
        sortFaceThroughCoordinate(subZ, 2, isSlab);

    }


    /**
     * sort the face facing same direction through their level
     * For example:
     * in sublist of x(subX), all vertex of two faces have value 4 in x, they will be grouped into a sublist.
     * Then do the searching in each sublist.
     * @param list
     * @param tag
     * @param isSlab
     */
    private void sortFaceThroughCoordinate(List<SingleFace> list, int tag, boolean isSlab){
        HashMap<Integer, Integer> coordinate2ListPos = new HashMap<>();
        List<List<SingleFace>> listTable = new ArrayList<>();
        for(SingleFace face:list){
            int faceCoordinate = (obj.getVertexByIndex(face.vertexIndexList.get(0)).get(tag)).intValue();
            if(coordinate2ListPos.get(faceCoordinate) != null){
                listTable.get(coordinate2ListPos.get(faceCoordinate)).add(face);
            }else{
                List<SingleFace> tempList = new ArrayList<>();
                tempList.add(face);
                coordinate2ListPos.put(faceCoordinate, listTable.size());
                listTable.add(tempList);
            }
        }

        for(List<SingleFace> singleFaceList: listTable){
            faceReduceInClassifiedList(singleFaceList, tag, isSlab);
        }
    }


    /**
     * Comparing all the faces one by one to figure out which two faces can be merged.
     * If able to merge, call the merging function on these faces.
     * Due to the difference between slab and normal block in mineways' output model,
     * here uses another way to comparing the slabs.
     * @param subList:face list
     * @param tag:indicates which axi are these faces on
     * @param isSlab:indicates whether these faces are slab
     */
    private void faceReduceInClassifiedList(List<SingleFace> subList, int tag, boolean isSlab){
        for(int i = 0; i < subList.size(); i++){
            SingleFace currFace = subList.get(i);
            if(currFace.isDeleted()){
                continue;
            }
            for(int j = i+1; j < subList.size(); j++){
                displayProcessPercentage();
                SingleFace comparedFace = subList.get(j);
                if(comparedFace.isDeleted()){continue;}

                if(isInSameFloor(currFace, comparedFace, tag) &&
                        numberOfSameVertexChooser(currFace, comparedFace, isSlab) >= 2){
                    currFace.mergeFace(comparedFace, obj, isSlab);
                    progressDisplay.updateCounter();
                }

            }

            progressDisplay.updateCounter();
        }
    }


    /**
     * Choose the right method for numberOfSameVertex though whether isSlab
     * Because slabs don't share the same vertex though two of the vertex are in the same pos.
     * @param face1
     * @param face2
     * @param isSlab
     * @return
     */
    private int numberOfSameVertexChooser(SingleFace face1, SingleFace face2, boolean isSlab){
        if(isSlab){
            return numberOfSameVertexStrict(face1, face2);
        }else{
            return numberOfSameVertex(face1, face2);
        }
    }

    private boolean isInSameFloor(SingleFace f1, SingleFace f2, int tag){
        double cord1 = obj.getVertexByIndex(f1.vertexIndexList.get(0)).get(tag);
        double cord2 = obj.getVertexByIndex(f2.vertexIndexList.get(0)).get(tag);
        return Math.abs(cord1-cord2)<0.0001;
    }

    /**
     *
     * @param face
     * @return the tag of face
     */
    private int faceClassifier(SingleFace face){
        boolean x = true;
        boolean y = true;
        boolean z = true;
        for(int i = 1; i < 3; i++){
            List<Double> currV = obj.getVertexByIndex(face.vertexIndexList.get(i));
            List<Double> prevV = obj.getVertexByIndex(face.vertexIndexList.get(i-1));
            x = x && Math.abs(currV.get(0)- prevV.get(0))<0.0001;
            y = y && Math.abs(currV.get(1)- prevV.get(1))<0.0001;
            z = z && Math.abs(currV.get(2)- prevV.get(2))<0.0001;
        }
        if(x){return 0;}
        if(y){return 1;}
        if(z){return 2;}
        return -1;
    }

    /**
     * this method is to compare all the coordinates value of each vertex instead of only compare the index of vertex
     * @return
     */
    private int numberOfSameVertexStrict(SingleFace f1, SingleFace f2){
        int num = 0;
        for(int vi1: f1.vertexIndexList){
            List<Double> v1 = obj.getVertexByIndex(vi1);
            for(int vi2: f2.vertexIndexList){
                List<Double> v2 = obj.getVertexByIndex(vi2);
                if(Math.abs(v1.get(0)-v2.get(0))<0.001 &&
                        Math.abs(v1.get(1)-v2.get(1))<0.001 &&
                        Math.abs(v1.get(2)-v2.get(2))<0.001 ){
                    num++;
                }
            }
        }
        return num;
    }


    /**
     * show the process
     */
    private void displayProcessPercentage(){
        counter++;
        if(counter*10/sum > (counter-1)*10/sum){
            System.out.println((counter*10/sum) +"0 %");
        }
    }


    //===========================================================================================
    //old method
//    //May cause some bugs?
//    public void innerFaceCheck(){
//        for(List<SingleFace> faceList:obj.getGroupedFaceList()){
//            String name = obj.getGroupName().get(obj.getGroupedFaceList().indexOf(faceList));
//            boolean isSlab = name.contains("Slab")&&!name.contains("Double");
//            if(isSlab){
//                removeInnerFace(faceList);
//            }
//        }
//    }
//    /**
//     * remove the faces which are 'inside the block'
//     * find the vertex which are only linked to one face, delete the face. (redundant face)
//     * @param faceList
//     */
//    private void removeInnerFace(List<SingleFace> faceList){
//        vertexLinkedFaceMap.clear();
//        usedVertexList.clear();
//        for(SingleFace face:faceList){
//            if(face.isDeleted()){continue;}
//            for(int vi: face.vertexIndexList){
//                List<Double> v = obj.getVertexByIndex(vi);
//                if(!usedVertexList.contains(v)){
//                    usedVertexList.add(v);
//                }
//                if(vertexLinkedFaceMap.get(v) == null){
//                    List<SingleFace> temp = new ArrayList<>();
//                    temp.add(face);
//                    vertexLinkedFaceMap.put(v, temp);
//                }else{
//                    vertexLinkedFaceMap.get(v).add(face);
//                }
//            }
//        }
//
//        for(List<Double> v:usedVertexList){
////            System.out.println(vertexLinkedFaceMap.get(vi).size());
//            if(vertexLinkedFaceMap.get(v).size() == 1){
//                vertexLinkedFaceMap.get(v).get(0).deleteFace();
//            }
//        }
//    }


//    /**
//     * Compare each faces in group to find faces available to merge.
//     * @param faceList
//     */
//    private void faceReduceInSingleGroup(List<SingleFace> faceList){
//        sum = (1+ faceList.size())*faceList.size()/2;
//        counter = 0;
//        for(int i = 0; i < faceList.size(); i++){
//            SingleFace currFace = faceList.get(i);
//            if(currFace.isDeleted()){continue;}
//            for(int j = i+1; j < faceList.size(); j++){
//                displayProcessPercentage();
//
//                SingleFace comparedFace = faceList.get(j);
//                if(comparedFace.isDeleted()){continue;}
////                System.out.println("comparing: "+isInSamePlane(currFace, comparedFace)+", "+numberOfSameVertex(currFace, comparedFace));
////                if(isInSamePlane(currFace, comparedFace)){
////                    System.out.printf("face("+i+"): ");
////                    currFace.printVertex(obj);
////                    System.out.printf("face("+j+"): ");
////                    comparedFace.printVertex(obj);
////                }
//
//                if(isInSamePlane(currFace, comparedFace) &&
//                        (numberOfSameVertex(currFace, comparedFace) >= 2)){
////                    System.out.println("face reducing: "+i+" merged "+j);
//                    currFace.mergeFace(comparedFace, obj, false);
//                }
//            }
//        }
//    }
//
//    /**
//     * this method is to ensure that two faces are in same surface.
//     * @param f1
//     * @param f2
//     * @return
//     */
//    private boolean isInSamePlane(SingleFace f1, SingleFace f2){
//        boolean x = true;
//        boolean y = true;
//        boolean z = true;
//        for(int i = 0; i < f1.vertexIndexList.size(); i++){
//            List<Double> v1 = obj.getVertexByIndex(f1.vertexIndexList.get(i));
//            for(int j = 0; j < f2.vertexIndexList.size(); j++){
//                List<Double> v2 = obj.getVertexByIndex(f2.vertexIndexList.get(j));
////                System.out.println(i+"]"+v1.get(0)+", "+v1.get(1)+", "+v1.get(2)+" with: "+j+"]"+v2.get(0)+", "+v2.get(1)+", "+v2.get(2)+", ");
//                x = x&&(Math.abs(v1.get(0) - v2.get(0))<0.0001);
//                y = y&&(Math.abs(v1.get(1) - v2.get(1))<0.0001);
//                z = z&&(Math.abs(v1.get(2) - v2.get(2))<0.0001);
//            }
//        }
////        System.out.println("func end:"+"x-"+x+" y-"+y+" z-"+z);
//        return x||y||z;
//    }
}
