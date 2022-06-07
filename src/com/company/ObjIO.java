package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ObjIO {
    private BufferedReader br = null;
    private List<String> fileContent = new ArrayList<String>();

    private List<List<Double>> vertexList = new ArrayList<List<Double>>();
    private List<List<SingleFace>> groupedFaceList = new ArrayList<List<SingleFace>>();
    private List<String> groupName = new ArrayList<String>();

    /**
     * getter method
     * @return
     */
    public List<List<Double>> getVertexList() {
        return vertexList;
    }

    public List<List<SingleFace>> getGroupedFaceList() {
        return groupedFaceList;
    }

    public List<String> getGroupName() {
        return groupName;
    }

    public List<Double> getVertexByIndex(int i){
        if(i <= 0 || i >= vertexList.size()){
            System.out.println("Invalid vertex index!");
            System.exit(1);
        }
        return vertexList.get(i);
    }



    /**
     * The constructor read the file path and open the specific .obj file,
     * then read all the content into a ArrayList.
     * An exception will be print out if an error occur in file reading.
     * @param filePath
     */
    public ObjIO(String filePath){
        try{
            br = new BufferedReader(new FileReader(filePath));
            String currLine = br.readLine();
            while(currLine != null){
                fileContent.add(currLine);
                currLine = br.readLine();
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    /**
     * recognize and read vertex from content.
     * Store vertex in vertexList.
     * the vertex in index 0 should not be used.
     */
    public void readVertex(){
        if(vertexList.size() == 0){
            List<Double> zeroV = new ArrayList<Double>();
            zeroV.add(0.0);
            zeroV.add(0.0);
            zeroV.add(0.0);
            vertexList.add(zeroV);
        }

        for(String currentLine:fileContent){
            if(currentLine.startsWith("v ")){
                String[] strArr = currentLine.split(" ");
                List<Double> currV = new ArrayList<Double>();
                currV.add(Double.parseDouble(strArr[1]));
                currV.add(Double.parseDouble(strArr[2]));
                currV.add(Double.parseDouble(strArr[3]));
                vertexList.add(currV);
            }
        }
    }

    /**
     * print vertex information
     */
    public void printAllVertex(){
        for(List<Double> currV:vertexList){
            System.out.println(currV.get(0)+", "+ currV.get(1)+", "+ currV.get(2));
        }
    }

    /**
     * read face from content, and will be grouped following the "g " tag.
     */
    public void readFace(){
        groupName.add("ungrouped");
        List<SingleFace> currFaceList = new ArrayList<SingleFace>();
        for(String currLine:fileContent){
            if(currLine.startsWith("g ")){
                List<SingleFace> temp = new ArrayList<SingleFace>();
                temp.addAll(currFaceList);
                groupedFaceList.add(temp);
                groupName.add(currLine.substring(2));
                currFaceList = new ArrayList<SingleFace>();
            }
            if(currLine.startsWith("f ")){
                SingleFace currFace = new SingleFace();
                String[] strArr = currLine.split(" ");
                for(int i = 1; i < strArr.length; i++){
                    currFace.addVertex(strArr[i]);
                }
                currFaceList.add(currFace);
            }
        }
        groupedFaceList.add(currFaceList);
    }

    /**
     * print the vertex indices of all faces
     * each line represent a single face
     */
    public void printAllFaceVertex(){
        for(int i = 0; i < groupedFaceList.size(); i++){
            System.out.println(groupName.get(i));
            for(SingleFace f:groupedFaceList.get(i)){
                f.printVertexIndex();
            }
        }
    }


}
