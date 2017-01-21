import GraphCore.Arc;
import GraphCore.GraphUtility;
import GraphCore.Vertex;
import org.jgrapht.alg.HamiltonianCycle;
import org.jgrapht.graph.SimpleWeightedGraph;
import parser.Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private static SimpleWeightedGraph<Vertex, Arc> solution;

    /**
     *
     * @param completeGraph graph without school
     * @param alpha
     * @return
     */
    public static SimpleWeightedGraph<Vertex,Arc> solve(SimpleWeightedGraph<Vertex,Arc> completeGraph, double alpha){

        //variable distance used in the for loop for maintaining the overall distance
        double distance=0;

        //the list of verteces satisfying alpha
        List<Vertex> vertexListCorrect=new ArrayList<>();

        //add school to solution
        if(!solution.containsVertex(new Vertex(0,0,0))){
            solution.addVertex(new Vertex(0,0,0));
        }

        //create hamiltonian from complete graph
        List<Vertex> vertexList = HamiltonianCycle.getApproximateOptimalForCompleteGraph(completeGraph);

        // find the verteces nearest to school
        Vertex nearest = GraphUtility.nearestToSchool(vertexList);

        //split list and rearrange it
        List<Vertex> beforeList = vertexList.subList(0,vertexList.indexOf(nearest));
        List<Vertex> afterList = vertexList.subList(vertexList.indexOf(nearest),vertexList.size());

        //now the list starts with the nearest node to the school
        afterList.addAll(beforeList);

        //compute the first distance to school
        distance+= afterList.get(0).computeDistanceFromSchool();

        //check alpha value
        for (int i =1;i<afterList.size();i++){
            distance=distance+afterList.get(i).computeDistance(afterList.get(i-1));
            //if does not satisfy alpha
            if(distance>alpha*afterList.get(i).computeDistanceFromSchool()){
                //break the list here
                //go out from the cycle
                break;
            }
            else{
                vertexListCorrect.add(afterList.get(i));
                //remove already visited verteces from graph
                completeGraph.removeVertex(afterList.get(i));
            }
        }

        GraphUtility.attachPathToSchool(solution,vertexListCorrect);

        //now the graph is without already visited edge, so check if is empty
        if(completeGraph.vertexSet().size()==0){
            return solution;
        }

        else{
            solve(completeGraph,alpha);
        }


        return null;


    }


}
