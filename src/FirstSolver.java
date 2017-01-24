import GraphCore.Arc;
import GraphCore.GraphUtility;
import GraphCore.Vertex;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.jgrapht.alg.HamiltonianCycle;
import org.jgrapht.graph.SimpleWeightedGraph;
import parser.Parser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * This algorithm uses an heuristic hamiltonian path algorithm to find the solution: given the graph, it finds an
 * hamiltonian path on the graph obtained removing the school, then it find the nearest node of the path to the school
 * and connect it to the school. The resulting graph is a tree with only a branch, or still a circuit. After it verifies
 * that the alpha-condition is satisfied on the obtained graph, and it breaks the connections where not. On the not
 * connected set of vertices the hamiltonian circuit is another time found, ne nearest vertex is connected to the school,
 * and the verification of the alpha-condition is applied another time. The algorithm iterates in this way while the
 * alpha condition is satisfied and the vertices are all connected.
 */
public class FirstSolver {
    private SimpleWeightedGraph<Vertex, Arc> solution;
    private Vertex school;

    public FirstSolver() {
        solution = new SimpleWeightedGraph<Vertex, Arc>(Arc.class);
        school = GraphUtility.getSchool();
        solution.addVertex(school);
    }

    /**
     * @param completeGraph graph without school, it represents the set of the vertices that aren't yet in the sol
     * @param alpha
     * @return
     */
    public SimpleWeightedGraph<Vertex, Arc> solve(SimpleWeightedGraph<Vertex, Arc> completeGraph,
                                                  double alpha, int iteration) {
        System.out.println(completeGraph.vertexSet().size());
        System.out.println(alpha);
        //end of recursion: all nodes are in the solution graph, so the input graph is empty
        if (completeGraph.vertexSet().size() == 0) return solution;

        //compute the hamiltonian cycle contain all the nodes that are not in the intermediate solution
        List<Vertex> hamiltonian = HamiltonianCycle.getApproximateOptimalForCompleteGraph(completeGraph);

        //find the nearest node to the school
        Vertex nearest = GraphUtility.nearestToSchool(hamiltonian);

        //split the list and rearrange it such to have the nearest as first
        List<Vertex> firstSublist = hamiltonian.subList(0, hamiltonian.indexOf(nearest)); //this list doesn't contain nearest
        List<Vertex> rearrangedHamiltonian = hamiltonian.subList(hamiltonian.indexOf(nearest), hamiltonian.size());
        rearrangedHamiltonian.addAll(firstSublist);

        //Add the first vertex to the solution, it certainly satisfies the alpha condition if alpha > 1
        solution.addVertex(nearest);
        solution.addEdge(school, nearest);
        solution.setEdgeWeight(solution.getEdge(school, nearest), GraphUtility.getDistanceFromSchool(nearest));
        //the vertex that have been added have to be removed from the from the completeGraph
        //because it represents the set of nodes that aren't yet in the solution
        completeGraph.removeVertex(nearest);

        //iterate on the rearrangedHamiltonian and add the vertices to the solution while the alpha condition is satisfied
        double distance = GraphUtility.getDistanceFromSchool(nearest); //keep the distance computed on the path
        int i = 1;
        while (i < rearrangedHamiltonian.size() &&
                distance + rearrangedHamiltonian.get(i).computeDistance(rearrangedHamiltonian.get(i - 1))
                        < GraphUtility.getDistanceFromSchool(rearrangedHamiltonian.get(i)) * alpha
                ) {
            solution.addVertex(rearrangedHamiltonian.get(i));
            solution.addEdge(rearrangedHamiltonian.get(i - 1), rearrangedHamiltonian.get(i));
            solution.setEdgeWeight(solution.getEdge(rearrangedHamiltonian.get(i - 1), rearrangedHamiltonian.get(i)),
                    rearrangedHamiltonian.get(i).computeDistance(rearrangedHamiltonian.get(i - 1)));
            distance += rearrangedHamiltonian.get(i).computeDistance(rearrangedHamiltonian.get(i - 1));

            System.out.println("Node " + rearrangedHamiltonian.get(i).getName() + " Distance : " + distance + " Directed*alpha: " + GraphUtility.getDistanceFromSchool(rearrangedHamiltonian.get(i)) * alpha);
            //then the vertices that are added to the solution have to be removed from the completeGraph
            //because it represents the set of nodes that aren't yet in the solution
            completeGraph.removeVertex(rearrangedHamiltonian.get(i));
            i++;
        }
        return solve(completeGraph, alpha, ++iteration);
    }


    public SimpleWeightedGraph<Vertex, Arc> solve2(SimpleWeightedGraph<Vertex, Arc> graph, double alpha, int iteration) {

        System.out.println(iteration);

        //end of recursion: all nodes are in the solution graph, so the input graph is empty
        if (graph.vertexSet().size() == 0) return solution;

        ArrayList<Vertex> vertexList = new ArrayList<>(graph.vertexSet());
        Vertex distantVertex = GraphUtility.getMostDistantFromSchool(vertexList);

        System.out.println("Most distant is " + distantVertex.getName());
        ArrayList<Vertex> currentPath = new ArrayList<>();
        currentPath.add(distantVertex);
        vertexList.remove(distantVertex);
        graph.removeVertex(distantVertex);
        int visited = 0;

        while (visited < vertexList.size()) {
            //get the node with min distance from current path
            Vertex minDistant = GraphUtility.getMinDistantFrom(currentPath.get(0), vertexList);
            //if the path is feasible then add the nodes
            if (GraphUtility.checkPathFeasible(currentPath, minDistant, alpha)) {
                currentPath.add(0, minDistant);
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

        return solve2(graph, alpha, iteration);
    }


    public SimpleWeightedGraph<Vertex, Arc> solve3(SimpleWeightedGraph<Vertex, Arc> graph, double alpha, int iteration) {

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
        System.out.println("the length of the just find path is" + maxNodesPath.size());

        iteration++;

        return solve3(graph, alpha, iteration);
    }


    public SimpleWeightedGraph<Vertex, Arc> solveWithLocal(SimpleWeightedGraph<Vertex, Arc> graph, double alpha, int iteration) {
        SimpleWeightedGraph<Vertex,Arc> simpleWeightedGraph = solve3(graph,alpha,iteration);

        // arcs outgoing from school
        ArrayList<Arc> outgoingArc = new ArrayList<>(simpleWeightedGraph.edgesOf(school));
        //all vertices connected to the school
        ArrayList<Vertex> vertices = new ArrayList<>();
        //all path not containing the school
        List<ArrayList<Vertex>> paths = new ArrayList<>();
        for(Arc arc: outgoingArc){
            Vertex vertex = graph.getEdgeTarget(arc);
            if(!vertex.equals(school)){
                vertices.add(vertex);
            }
        }

        System.out.println("outgoing : "+ outgoingArc.size()+" vertices "+vertices.size());


        //find paths
        int i =0;
        for(Vertex v: vertices){
            paths.add(new ArrayList<>());
            paths.get(i).add(v);
            for(int j = 0; j< paths.get(i).size();j++){
                ArrayList<Arc> currentArcs = new ArrayList<>(simpleWeightedGraph.edgesOf(paths.get(i).get(j)));
                for(Arc arc: currentArcs){
                    Vertex vertex = graph.getEdgeTarget(arc);
                    if(!vertex.equals(paths.get(i).get(j))){
                        paths.get(i).add(vertex);
                    }
                }
            }
            System.out.println("length : "+paths.get(i).size());
            i++;
        }

        System.out.println("Paths : "+paths.size());


        //now the paths array contains all paths from school
        int changes = 0;
        do{
            changes=0;
            for(int j = 0; j< paths.size();j++){
                //here we get the path j-th, check for all other path if is possible to add a node in path j
                //for other paths with more nodes
                for(int k = 0;k<paths.size();k++){
                    // if k-th paths is not equal to j-th and has a lower number of nodes
                    if(paths.get(j).size()>paths.get(k).size()){
                        // for every node in k-th path
                        for(int m = 0; m<paths.get(k).size();m++){
                            Vertex vertex = paths.get(k).get(m);
                            // check if it's possible to add it in some position of j-th path
                            for(int l = paths.get(j).size()-1;l>=0;l--){
                                if(GraphUtility.checkPathFeasible((ArrayList<Vertex>)paths.get(j).clone(),vertex,alpha,l)){
                                    System.out.println("CHANGE!!!!");
                                    paths.get(j).add(l,vertex);
                                    paths.get(k).remove(m);
                                    System.out.println("Added path length : "+paths.get(j).size());
                                    System.out.println("removed path length "+paths.get(k).size());
                                    changes++;
                                    break;
                                }
                            }
                        }
                    }
                }
            }



        }while (changes!=0);

        solution = new SimpleWeightedGraph<Vertex, Arc>(Arc.class);
        solution.addVertex(school);
        //add all vertices in the path to the solution
        for(ArrayList<Vertex> path: paths){
            for (int j = 0; j < path.size(); j++) {
                solution.addVertex(path.get(j));
                if (j == 0) {
                    solution.addEdge(school, path.get(j));
                    solution.setEdgeWeight(solution.getEdge(school, path.get(j)),
                            GraphUtility.getDistanceFromSchool(path.get(j)));
                } else {
                    solution.addEdge(path.get(j - 1), path.get(j));
                    solution.setEdgeWeight(solution.getEdge(path.get(j - 1), path.get(j)),
                            path.get(j).computeDistance(path.get(j - 1)));
                }
            }
        }

        return solution;
    }



}
