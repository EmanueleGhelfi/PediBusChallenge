import com.ampl.AMPL;
import com.ampl.Parameter;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emilianogagliardi on 20/01/2017.
 */
public class Parser {
    private AMPL ampl;

    public Parser (String datPath) throws IOException {
        ampl = new AMPL();
        ampl.read(Constants.MOD_PATH);
        ampl.readData(datPath);
    }

    public int getAlpha() {
        return 0;
    }

    private int getN() {
        Double d = (double)ampl.getValue("n");
        return d.intValue();
    }

    private List<Vertex> getVertices () {
        List<Vertex> vertices = new ArrayList<>();
        int n = getN();
        Parameter coordX = ampl.getParameter("coordX");
        Parameter coordY = ampl.getParameter("coordY");
        for (int i = 0; i <= n; i ++) {
            Vertex v = new Vertex(i, (double) coordX.get(i), (double) coordY.get(i));
            vertices.add(v);
        }
        return vertices;
    }

    public SimpleWeightedGraph<Vertex, Arc> createGraph () {
        List<Vertex> vertices = getVertices();
        SimpleWeightedGraph<Vertex, Arc> graph = new SimpleWeightedGraph<Vertex, Arc>(Arc.class);
        for (Vertex v : vertices) {
            graph.addVertex(v);
        }
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                Parameter dangerousnessParam = ampl.getParameter("d");
                Arc a = new Arc((double) dangerousnessParam.get(i, j));
                Vertex v1 = vertices.get(i);
                Vertex v2 = vertices.get(j);
                graph.addEdge(v1, v2, a);
                graph.setEdgeWeight(a, computeDistance(v1, v2));
            }
        }
        return graph;
    }

    private double computeDistance (Vertex v1, Vertex v2) {
        return Math.sqrt(Math.pow(v1.getCoordX() - v2.getCoordX(), 2) + Math.pow(v1.getCoordY() - v2.getCoordY(), 2));
    }
}
