import GraphCore.Arc;
import GraphCore.GraphUtility;
import GraphCore.Vertex;
import org.jgrapht.alg.HamiltonianCycle;
import org.jgrapht.graph.SimpleWeightedGraph;
import parser.Parser;

import java.util.List;

/**
 * Created by emanueleghelfi on 20/01/2017.
 */
public class Test{

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("write input and output file path as parameter");
            System.exit(0);
        }

        String inputPath = args[0];
        String ouputPath = args[1];

        //littleInterfacesTest(inputPath, ouputPath);

        Parser p = new Parser(inputPath);
        List<Vertex> vertexList = p.getVertexListWithoutSchool();
        SimpleWeightedGraph<Vertex, Arc> graph = GraphUtility.createCompleteGraph(vertexList);
        SimpleWeightedGraph<Vertex, Arc> graph1  = FirstSolver.solve(graph,p.getAlpha());
        SolutionFormatter.writeSolution(graph1, ouputPath);
    }

    public static void littleInterfacesTest (String inputPath, String ouputPath) {
        Parser p = new Parser(inputPath);
        List<Vertex> vertexList = p.getVertexListWithoutSchool();
        SimpleWeightedGraph<Vertex, Arc> graph = GraphUtility.createCompleteGraph(vertexList);
        List<Vertex> hamiltonian = HamiltonianCycle.getApproximateOptimalForCompleteGraph(graph);
        hamiltonian.add(new Vertex(0, 0, 0));
        SimpleWeightedGraph<Vertex, Arc> graph1 = GraphUtility.createCircuit(hamiltonian);
        SolutionFormatter.writeSolution(graph1, ouputPath);
    }
}
