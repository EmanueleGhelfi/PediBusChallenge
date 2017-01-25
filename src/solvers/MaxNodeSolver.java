package solvers;

import GraphCore.Arc;
import GraphCore.GraphUtility;
import GraphCore.Vertex;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emanueleghelfi on 25/01/2017.
 */
public class MaxNodeSolver implements SolverInterface {

    private SimpleWeightedGraph<Vertex, Arc> solution;
    private Vertex school;

    public MaxNodeSolver() {
        solution = new SimpleWeightedGraph<Vertex, Arc>(Arc.class);
        school = GraphUtility.getSchool();
        solution.addVertex(school);
    }

    @Override
    public SimpleWeightedGraph<Vertex, Arc> solve(SimpleWeightedGraph<Vertex, Arc> graph, double alpha, int iteration) {

        //end of recursion: all nodes are in the solution graph, so the input graph is empty
        if (graph.vertexSet().size() == 0) return solution;

        //construct the list of paths from a node to the school, trying to maximize the numer
        //of nodes chosing the min distance nodes during the path construction
        List<ArrayList<Vertex>> paths = new ArrayList<>();
        List<Vertex> vertexList = new ArrayList<>(graph.vertexSet());
        int i = 0; //at the i iteration i'm creating the i-th path
        while (!vertexList.isEmpty()) {
            Vertex v = GraphUtility.getMostDistantFromSchool(vertexList);
            vertexList.remove(v);
            List<Vertex> notExplored = new ArrayList<>(vertexList);
            paths.add(new ArrayList<>());
            paths.get(i).add(v);
            while (!notExplored.isEmpty()) {
                Vertex currentV = GraphUtility.getMinDistantFrom(paths.get(i).get(0), notExplored);
                if (GraphUtility.checkPathFeasible(paths.get(i), currentV, alpha)) {
                    paths.get(i).add(0, currentV);
                }
                notExplored.remove(currentV);
            }
            i++;
        }


        //select the path with the highest number of nodes
        //TODO modify: select all the best in term of nodes number, the select the least dangerous
        int max = 0;
        List<Vertex> maxNodesPath = null;
        for (List<Vertex> path : paths) {
            if (path.size() > max) {
                max = path.size();
                maxNodesPath = path;
            }
        }


        //add all vertices in the path to the solution
        for (int j = 0; j < maxNodesPath.size(); j++) {
            solution.addVertex(maxNodesPath.get(j));
            if (j == 0) {
                solution.addEdge(school, maxNodesPath.get(j));
                solution.setEdgeWeight(solution.getEdge(school, maxNodesPath.get(j)),
                        GraphUtility.getDistanceFromSchool(maxNodesPath.get(j)));
            } else {
                solution.addEdge(maxNodesPath.get(j - 1), maxNodesPath.get(j));
                solution.setEdgeWeight(solution.getEdge(maxNodesPath.get(j - 1), maxNodesPath.get(j)),
                        maxNodesPath.get(j).computeDistance(maxNodesPath.get(j - 1)));
            }
        }

        graph.removeAllVertices(maxNodesPath);

        iteration++;

        return solve(graph, alpha, iteration);
    }
}
