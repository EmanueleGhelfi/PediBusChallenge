package parser;

import GraphCore.Vertex;
import GraphCore.VertexCouple;
import com.ampl.AMPL;
import com.ampl.Parameter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by emilianogagliardi on 20/01/2017.
 */
public class Parser {
    private AMPL ampl;

    public Parser (String datPath, String modelPath) {
        ampl = new AMPL();
        try {
            ampl.read(modelPath);
        } catch (IOException e) {
            System.out.println("unable to open fake model, needed to parse the .dat with AMPL");
            e.printStackTrace();
        }
        try {
            System.out.println("Opening "+datPath);
            ampl.readData(datPath);
        } catch (IOException e) {
            System.out.println("unable to find input path");
            e.printStackTrace();
        }
    }

    public double getAlpha() {
        Double d = (double) ampl.getValue("alpha");
        return d;
    }

    private int getN() {
        Double d = (double)ampl.getValue("n");
        return d.intValue();
    }

    public List<Vertex> getVertexListWithoutSchool () {
        List<Vertex> vertices = new ArrayList<>();
        int n = getN();
        Parameter coordX = ampl.getParameter("coordX");
        Parameter coordY = ampl.getParameter("coordY");
        for (int i = 1; i <= n; i ++) {
            Vertex v = new Vertex(i, (double) coordX.get(i), (double) coordY.get(i));
            vertices.add(v);
        }
        return vertices;
    }


    public Vertex getSchool(){
        Parameter coordX = ampl.getParameter("coordX");
        Parameter coordY = ampl.getParameter("coordY");
        Vertex v = new Vertex(0,(double) coordX.get(0),(double)coordY.get(0));
        return v;
    }

    /**
     * Returns an hashmap with vertexCouple and Double.
     * Double is the dangerousness of the arc that goes from source vertex to target vertex.
     * Source vertex is the nearest to school.
     * @return
     */
    public HashMap<VertexCouple,Double> getDangerousness(){
        HashMap<VertexCouple,Double> dangMap = new HashMap<>();
        // get all vertices and add school to them
        List<Vertex> vertexList = getVertexListWithoutSchool();
        vertexList.add(0,getSchool());
        // for each couple of verteces create a vertexCouple, parse the corresponding dang and add to hashmap
        for(int i = 0; i<vertexList.size();i++){
            for(int j = 0; j< vertexList.size();j++){
                VertexCouple vertexCouple = new VertexCouple(vertexList.get(j),vertexList.get(i));
                //parse dangerousness
                Parameter dangParam = ampl.getParameter("d");
                Double dang = (double) dangParam.get(i,j);
                dangMap.put(vertexCouple,dang);
            }
        }

        return dangMap;

    }

}
