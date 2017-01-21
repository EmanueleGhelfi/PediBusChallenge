package GraphCore;

import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.List;

/**
 * Created by emilianogagliardi on 20/01/2017.
 */
public class GraphUtility {

    /*
    Return the complete graph obtained from the list of vertex,
    assigning as weight of arcs the euclidean distance between the
    two connected nodes
    */
    public static SimpleWeightedGraph<Vertex, Arc> createCompleteGraph (List<Vertex> vertexList) {
        SimpleWeightedGraph<Vertex, Arc> graph = new SimpleWeightedGraph<Vertex, Arc>(Arc.class);
        for (Vertex v : vertexList) {
            graph.addVertex(v);
        }
        for (int i = 0; i < vertexList.size(); i++) {
            for (int j = i + 1; j < vertexList.size(); j++) {
                Arc a = new Arc();
                Vertex v1 = vertexList.get(i);
                Vertex v2 = vertexList.get(j);
                graph.addEdge(v1, v2, a);
                graph.setEdgeWeight(a, v1.computeDistance(v2));
            }
        }
        return graph;
    }

    /*
    Return a graph that is the circuit obtained connecting the vertices in the list in the given order
     */
    public static SimpleWeightedGraph<Vertex, Arc> createCircuit(List<Vertex> vertexList){
        SimpleWeightedGraph<Vertex, Arc> graph = new SimpleWeightedGraph<>(Arc.class);
        for (Vertex v : vertexList) {
            graph.addVertex(v);
        }
        for (int i = 0; i < vertexList.size() - 1; i++) {
            Arc a = new Arc();
            Vertex v1 = vertexList.get(i);
            Vertex v2 = vertexList.get(i+1);
            graph.addEdge(v1, v2, a);
            graph.setEdgeWeight(a, v1.computeDistance(v2));
        }
        return graph;
    }

    public static Vertex nearestToSchool (List<Vertex> vList) {
        double minDistance = vList.get(0).computeDistanceFromSchool();
        Vertex nearest = vList.get(0);
        for (Vertex v : vList) {
            double d = v.computeDistanceFromSchool();
            if (d < minDistance) {
                minDistance = d;
                nearest = v;
            }
        }
        return nearest;
    }
}
