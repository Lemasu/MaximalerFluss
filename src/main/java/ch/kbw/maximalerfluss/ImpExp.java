package ch.kbw.maximalerfluss;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Diese Klasse basiert auf das Projekt der Gruppe "Zyklensuche".
 */
public class ImpExp {

    //Variablen für Import
    JSONParser parser = new JSONParser();
    Object obj;
    private static final String path = "src/main/resources/jsonfiles/";

    //Variablen für Export
    private static FileWriter writer;
    private JSONObject jsonObject = new JSONObject();
    private JSONArray adjazenmatrix = new JSONArray();
    private JSONArray points = new JSONArray();
    private String filename;

    public ImpExp() {
    }

    //method to create adjazenmtrix from JSONArray
    private byte[][] toAdjazenmatrix(JSONArray jsonArrayMatrix) {
        int matrixSize = jsonArrayMatrix.size();
        byte[][] adjazenmatrix = new byte[matrixSize][matrixSize];
        JSONArray array = new JSONArray();

        for (int i = 0; i < matrixSize; i++) {
            array = (JSONArray) jsonArrayMatrix.get(i);
            for (int j = 0; j < matrixSize; j++) {
                long temp = (Long) array.get(j);
                adjazenmatrix[i][j] = (byte) temp;
            }
        }
        return adjazenmatrix;
    }

    //method to create adjazenmtrix from filename
    public byte[][] getAdjazenmatrix(String filename) {
        byte[][] adjazenmatrix = null;
        try {
            //go to the right path
            obj = parser.parse(new FileReader(path + filename + ".json"));
            JSONObject jsonObject = (JSONObject) obj;
            //go to the element matrix and grab data
            JSONArray jArray = (JSONArray) jsonObject.get("matrix");
            adjazenmatrix = toAdjazenmatrix(jArray);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return adjazenmatrix;
    }

    //method to create points from filename
    public Point[] getPoints(String filename) {
        Point[] points = null;
        try {
            //go to the right path
            obj = parser.parse(new FileReader(path + filename + ".json"));
            JSONObject jsonObject = (JSONObject) obj;
            //go to the element points and grab data
            JSONArray jArray = (JSONArray) jsonObject.get("points");
            points = toPoints(jArray);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return points;
    }
    //method to create points from jsonArray
    private Point[] toPoints(JSONArray jsonArrayPoints) {
        int pointsSize = jsonArrayPoints.size();
        Point[] points = new Point[pointsSize];
        JSONArray array = new JSONArray();

        for (int i = 0; i < pointsSize; i++) {
            array = (JSONArray) jsonArrayPoints.get(i);
            points[i] = new Point(toInteger((Long) array.get(0)), toInteger((Long) array.get(1)));

        }
        return points;
    }
    //method to create integer from long
    private Integer toInteger(Long l) {
        return Integer.parseInt(l.toString());
    }

    //method which calls if points are set or not (in file)
    public boolean pointsAreSet(String filename) {
        boolean x = true;
        try {
            obj = parser.parse(new FileReader(path + filename + ".json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray jArray = (JSONArray) jsonObject.get("points");
        if (jArray == null) x = false;
        return x;
    }

    //method to create JSONFile from Adjazenmatrix
    public void createJSONFile(byte [][] matrixR, Point[] pointsR) throws IOException {

        //Create 2D Matrix and put it into JSONArray
        for (int i = 0; i < matrixR.length; i++) {
            JSONArray matrix2JSON = new JSONArray();
            for (int j = 0; j < matrixR.length; j++) {
                matrix2JSON.add(matrixR[i][j]);
            }
            adjazenmatrix.add(matrix2JSON);
        }




        //Create points Array and put it into JSONArray
        for (Point p : pointsR){
            JSONArray points2 = new JSONArray();
                points2.add(p.getX());
                points2.add(p.getY());
            points.add(points2);
        }


        //Put Objects in the right section
        jsonObject.put("matrix", adjazenmatrix);
        jsonObject.put("points", points);

        try {
            //create file
            writer = new FileWriter(path + chooseFilename());
            System.out.println(jsonObject.toJSONString());
            writer.write(jsonObject.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.flush();
            writer.close();
        }
    }


    //method to create filename
    public String chooseFilename(){
        //find the amount of files in ressources directory (ressources/jsonfiles)
        File[] f = new File(path).listFiles();
        String name = "default.json";
        if (f != null) {
            //if identical then choose othername
            for (File file : f){
                if (file.getName().equals("matrix" + (f.length+1))){
                    name = file.getName() + (f.length+1) + "a" + (f.length+1) + ".json";
                } else {
                    name = "matrix" + (f.length+1) + ".json";
                }
                setFilename(name);
                return name;
            }
        }
        setFilename(name);
        return name;
    }

    //Getter for Filename
    public String getFilename() {
        return filename;
    }
    //Setter for Filename
    public void setFilename(String filename) {
        this.filename = filename;
    }
}
