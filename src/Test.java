import GraphCore.Arc;
import GraphCore.GraphUtility;
import GraphCore.Vertex;
import org.jgrapht.alg.HamiltonianCycle;
import org.jgrapht.graph.SimpleWeightedGraph;
import parser.Parser;

import java.io.IOException;
import java.util.List;

/**
 * Created by emanueleghelfi on 20/01/2017.
 */
public class Test{

    public static void main(String[] args) throws IOException {
        Parser p = new Parser("inputs/pedibus_300.dat");
        int alpha = p.getAlpha();
        List<Vertex> vertexList = p.getVertexListWithoutSchool();
        SimpleWeightedGraph<Vertex, Arc> graph = GraphUtility.createCompleteGraph(vertexList);
        List<Vertex> hamiltonian = HamiltonianCycle.getApproximateOptimalForCompleteGraph(graph);
    }

}
