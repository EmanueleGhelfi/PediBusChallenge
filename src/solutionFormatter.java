import GraphCore.Vertex;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emilianogagliardi on 20/01/2017.
 */
public class solutionFormatter {

    public static void writeSolution (SimpleWeightedGraph graph, String destinationPath) {
        try{
            PrintWriter writer = new PrintWriter(destinationPath, "UTF-8");
            ArrayList vertexList = new ArrayList<>(graph.vertexSet());
            for (int i = 0; i < vertexList.size(); i++) {
                for (int j = i + 1; j < vertexList.size(); j++) {
                    Vertex v1 = (Vertex) vertexList.get(i);
                    Vertex v2 = (Vertex) vertexList.get(j);
                    if(graph.containsEdge(v1, v2))
                        writer.println(v1.getName() + " " + v2.getName());
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("impossible to write the file");
        }
    }
}
