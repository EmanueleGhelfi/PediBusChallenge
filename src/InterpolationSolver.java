import GraphCore.Arc;
import GraphCore.Vertex;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * This solver starts with a graph where the center is the school and all the other nodes are leaves.
 * Then it finds the interpolation straight line that pass through S for each path from S to a leaf (the vertices are
 * considered as points). Given the straight lines, the two paths with the less difference between the angular
 * coefficients of the corresponding straight lines are picked up.
 * From the two picked up paths, it constructs a unique path (ordinating the nodes by distance from S).
 * If the obtained solution is not feasible (the alpha condition is not satisfied) it take the second couple of paths
 * with minimum coefficient angular of corresponding straight lines difference.
 * When there are no more feasible leaf decreasing solutions the algorithm terminates.
 */
public class InterpolationSolver {
    private Vertex school;

    public InterpolationSolver(Vertex school) {
        this.school = school;
    }

    /*
        A "star tree" is a tree where all the branches outgoing from the root are paths
         */
    public SimpleWeightedGraph<Vertex, Arc> solve(SimpleWeightedGraph<Vertex, Arc> starTree, double alpha) {
        //obtain a list containing all the paths outgoing from s
        List<ArrayList<Vertex>> allPath = allPathsFromS(starTree);
        //compute the differences between each pair of path
    }

    private List<ArrayList<Vertex>> allPathsFromS (SimpleWeightedGraph<Vertex, Arc> starTree) {

    }

    /*
                - sum{i in V} (Xi - Xs)*(Ys-Yi)
        m =     --------------------------------
                    sum{i in V} (Xi - Xs)^2
     */
    private double fitlineThroughS_angularCoefficient (List<Vertex> vertexList) {
        List<Double> numeratorSummationElements = new ArrayList<>(vertexList.size());
        List<Double> denominatorSummationElements = new ArrayList<>(vertexList.size());
        for (int i = 0; i < vertexList.size(); i++) {
            Vertex v = vertexList.get(i);
            numeratorSummationElements.set(i, (v.getCoordX()-school.getCoordX())*(school.getCoordY()-v.getCoordY()));
            denominatorSummationElements.set(i, Math.pow(v.getCoordX()-school.getCoordX(), 2));
        }
        double numerator = - sumAll(numeratorSummationElements);
        double denominator = sumAll(denominatorSummationElements);
        return numerator/denominator;
    }

    private double sumAll (List<Double> list) {
        double sum = 0;
        for (Double d : list) {
            sum += d;
        }
        return sum;
    }
}
