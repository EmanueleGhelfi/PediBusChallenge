package GraphCore;

import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by emilianogagliardi on 20/01/2017.
 */
public class GraphUtility {

    private static Vertex school;

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
        double minDistance = getDistanceFromSchool(vList.get(0));
        Vertex nearest = vList.get(0);
        for (Vertex v : vList) {
            double d = getDistanceFromSchool(v);
            if (d < minDistance) {
                minDistance = d;
                nearest = v;
            }
        }
        return nearest;
    }

    public static SimpleWeightedGraph<Vertex,Arc> attachPathToSchool(SimpleWeightedGraph<Vertex,Arc> graph,
                                                                     List<Vertex> vertecesToAttach){

        //first create the connections in the graph
        for (Vertex v : vertecesToAttach) {
            graph.addVertex(v);
        }

//        vertecesToAttach.add(0,new Vertex(0,0,0));

        for (int i = 0; i < vertecesToAttach.size() - 1; i++) {
            Arc a = new Arc();
            Vertex v1 = vertecesToAttach.get(i);
            Vertex v2 = vertecesToAttach.get(i+1);
            graph.addEdge(v1, v2, a);
            graph.setEdgeWeight(a, v1.computeDistance(v2));
        }

        graph.addEdge(new Vertex(0, 0, 0), vertecesToAttach.get(0));
        graph.setEdgeWeight(graph.getEdge(new Vertex(0, 0, 0), vertecesToAttach.get(0)), vertecesToAttach.get(0).computeDistanceFromSchool());

        return graph;
    }

    public static void setSchool(Vertex v){
        school=v;
    }


    public static double getDistanceFromSchool(Vertex v1){

        return v1.computeDistance(school);

    }

    public static Vertex getSchool(){
        return school;
    }

    public static Vertex getMostDistantFromSchool(List<Vertex> vertexList) {
        double maxDistance = getDistanceFromSchool(vertexList.get(0));
        Vertex mostDistant=vertexList.get(0);
        for (Vertex v : vertexList) {
            double d = getDistanceFromSchool(v);
            if (d > maxDistance) {
                maxDistance = d;
                mostDistant = v;
            }
        }
        return mostDistant;
    }

    public static Vertex getMinDistantFrom(Vertex vertex, List<Vertex> vertexList) {
        double minDistance = vertexList.get(0).computeDistance(vertex);
        Vertex nearest = vertexList.get(0);
        for (Vertex v : vertexList) {
            double d = v.computeDistance(vertex);
            if (d < minDistance) {
                minDistance = d;
                nearest = v;
            }
        }
        return nearest;

    }

    public static boolean checkPathFeasible(List<Vertex> currentPath, Vertex minDistant,double alpha) {
        double distance = getDistanceFromSchool(minDistant);

        //for every node in the path check alpha condition
        for(int i = 0; i<currentPath.size();i++){
            if(i==0){
                distance+=currentPath.get(0).computeDistance(minDistant);
            }
            else{
                distance+=currentPath.get(i).computeDistance(currentPath.get(i-1));
            }

            if(distance>alpha*getDistanceFromSchool(currentPath.get(i))){
                return false;
            }
        }
        return true;
    }

    //check if the path with vertex in pos position is feasible.
    public static boolean checkPathFeasible(ArrayList<Vertex> path, Vertex vertex, double alpha, int position) {
        List<Vertex> pathCopy = (ArrayList<Vertex>) path.clone();
        int distance = 0;
        pathCopy.add(position,vertex);
        //for every node in the path check alpha condition
        for(int i = 0; i<pathCopy.size();i++){
            if(i==0){
                distance+=getDistanceFromSchool(pathCopy.get(i));
            }
            else{
                distance+=pathCopy.get(i).computeDistance(pathCopy.get(i-1));
            }

            if(distance>alpha*getDistanceFromSchool(pathCopy.get(i))){
                return false;
            }
        }
        return true;
    }
}
