package parser;

import GraphCore.Vertex;
import com.ampl.AMPL;
import com.ampl.Parameter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emilianogagliardi on 20/01/2017.
 */
public class Parser {
    private AMPL ampl;

    public Parser (String datPath) {
        ampl = new AMPL();
        try {
            ampl.read(Constants.MOD_PATH);
        } catch (IOException e) {
            System.out.println("unable to open fake model, needed to parse the .dat with AMPL");
            e.printStackTrace();
        }
        try {
            ampl.readData(datPath);
        } catch (IOException e) {
            System.out.println("unable to find input path");
            e.printStackTrace();
        }
    }

    public int getAlpha() {
        Double d = (double) ampl.getValue("alpha");
        return d.intValue();
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

    //TODO a method that parse dangerousness

}
