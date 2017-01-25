package solvers;

import GraphCore.Arc;
import GraphCore.GraphUtility;
import GraphCore.Vertex;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;

/**
 * Created by emanueleghelfi on 25/01/2017.
 */
public class MinDistanceSolver implements SolverInterface {


    private SimpleWeightedGraph<Vertex, Arc> solution;
    private Vertex school;

    public MinDistanceSolver() {
        solution = new SimpleWeightedGraph<Vertex, Arc>(Arc.class);
        school = GraphUtility.getSchool();
        solution.addVertex(school);
    }

    @Override
    public SimpleWeightedGraph<Vertex, Arc> solve(SimpleWeightedGraph<Vertex, Arc> graph, double alpha, int iteration) {
        //end of recursion: all nodes are in the solution graph, so the input graph is empty
        if (graph.vertexSet().size() == 0) return solution;

        ArrayList<Vertex> vertexList = new ArrayList<>(graph.vertexSet());
        Vertex nearVertex = GraphUtility.getMinDistantFrom(GraphUtility.getSchool(),vertexList);

        ArrayList<Vertex> currentPath = new ArrayList<>();
        currentPath.add(nearVertex);
        vertexList.remove(nearVertex);
        graph.removeVertex(nearVertex);
        int visited = 0;

        while (visited < vertexList.size()) {
            //get the node with min distance from current path
            Vertex minDistant = GraphUtility.getMinDistantFrom(currentPath.get(currentPath.size()-1), vertexList);
            //if the path is feasible then add the nodes
            if (GraphUtility.checkPathFeasible(currentPath, minDistant, alpha,currentPath.size())) {
                currentPath.add(currentPath.size(), minDistant);
            }
            vertexList.remove(minDistant);

            visited++;
        }


        //add all vertices in the path to the solution
        for (int i = 0; i < currentPath.size(); i++) {
            solution.addVertex(currentPath.get(i));
            if (i == 0) {
                solution.addEdge(school, currentPath.get(i));
                solution.setEdgeWeight(solution.getEdge(school, currentPath.get(i)),
                        GraphUtility.getDistanceFromSchool(currentPath.get(i)));
            } else {
                solution.addEdge(currentPath.get(i - 1), currentPath.get(i));
                solution.setEdgeWeight(solution.getEdge(currentPath.get(i - 1), currentPath.get(i)),
                        currentPath.get(i).computeDistance(currentPath.get(i - 1)));
            }
        }


        graph.removeAllVertices(currentPath);

        iteration++;

        return solve(graph, alpha, iteration);
    }
}
