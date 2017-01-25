import GraphCore.Arc;
import GraphCore.Vertex;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.AStarAdmissibleHeuristic;
import org.jgrapht.alg.shortestpath.AStarShortestPath;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.HashMap;
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
        List<ArrayList<Vertex>> allPaths = allPathsFromS(starTree);

        /*
        compute the differences between the angle fitline corresponding to each pair of path
        considering also the directions, oin order to avoid situation where the fitline coefficient are similar
        but the paths goes in opposite directions
         */
        HashMap<PathCouple, Double> angularDifferences = angularDifferences(allPaths);

        /*
        try to construct a path from two path, while a feasible one is found (check in growing order)
        if none is found the algorithm terminates (recursion terminates)
        */
        boolean found = false;
        while (!angularDifferences.isEmpty()) {
            PathCouple couple = getNearestCouple(angularDifferences);
            angularDifferences.remove(couple);
            //try to construct a unique path from the two in the couple

        }

        //if one is found recursive call with the new solution
        return null;
    }

    /*
    get from the star tree the paths that connect each leaf to the school
     */
    private List<ArrayList<Vertex>> allPathsFromS (SimpleWeightedGraph<Vertex, Arc> starTree) {
        List<Vertex> vertexList = new ArrayList<>(starTree.vertexSet());
        //remove from vertexList all the nodes that are not leaves
        for (Vertex v : vertexList) {
            if (starTree.degreeOf(v) > 1)
                vertexList.remove(v);
        }
        //TODO maybe it is better to use something else
        // there is no need of a good heuristic, because there is only a path. h(v) = 0 for each v is
        //certainly admissible
        AStarShortestPath<Vertex, Arc> pathFinder = new AStarShortestPath<>(starTree, new AStarAdmissibleHeuristic<Vertex>() {
            @Override
            public double getCostEstimate(Vertex vertex, Vertex v1) {
                return 0;
            }
        });
        List<ArrayList<Vertex>> paths = new ArrayList<>(vertexList.size()); //the number of path is the number of leaves
        for (Vertex leaf : vertexList) {
            GraphPath<Vertex, Arc> graphPath = pathFinder.getPath(leaf, school);
            ArrayList<Vertex> path = new ArrayList<>(graphPath.getVertexList());
            path.remove(school);
            paths.add(path);
        }
        return paths;
    }

    public HashMap<PathCouple, Double> angularDifferences (List<ArrayList<Vertex>> allPaths) {
        HashMap<PathCouple, Double> adjustedDifferences = new HashMap<>();
        for (int i = 0; i < allPaths.size(); i++) {
            for (int j = i + 1; j < allPaths.size(); j++) {
                double angle = Math.abs(adjustedArctanAngle(allPaths.get(i)) - adjustedArctanAngle(allPaths.get(j)));
                adjustedDifferences.put(new PathCouple(allPaths.get(i), allPaths.get(j)), angle);
            }
        }
        return adjustedDifferences;
    }

    /*
    it uses arctan and adjust it to compute the angle. There is the assumption that the first node of the path in the
    list is a leaf (the most distant from the school)
     */
    //TODO I'm not sure about this method
    private double adjustedArctanAngle (List<Vertex> path) {
        double m = fitlineThroughS_angularCoefficient(path);
        double angle = Math.atan(m);
        if (path.get(path.size()-1).getCoordX() < school.getCoordX()) {
            angle += Math.PI;
        }
        return angle;
    }

    /*
    Given a list of vertex, it computes the angular coefficient of the fitline (considering vertices as cartesian points)
    that passes through the school.
    The coefficient computation is simplified because of the condition that makes it pass through the school

             - sum{i in V} (Xi - Xs)*(Ys-Yi)
        m =  --------------------------------
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
        if (denominator == 0) { //a sort of handling of the division by zero exception
            return Double.MAX_VALUE;
        }
        return numerator/denominator;
    }

    private double sumAll (List<Double> list) {
        double sum = 0;
        for (Double d : list) {
            sum += d;
        }
        return sum;
    }

    private PathCouple getNearestCouple (HashMap<PathCouple, Double> differencesMap) {
        double min = Double.MAX_VALUE;
        PathCouple nearest = null;
        for (PathCouple couple : differencesMap.keySet()){
            double diff = differencesMap.get(couple);
            if (diff <= min) {
                min = diff;
                nearest = couple;
            }
        }
        return nearest;
    }

    private List<Vertex> constructPath (List<Vertex> path1, List<Vertex> path2) {
        //TODO construct the path picking up each time the nearest to the school
        return null;
    }
}

class PathCouple {
    private List<Vertex> path1;
    private List<Vertex> path2;

    public List<Vertex> getPath1() {
        return path1;
    }

    public List<Vertex> getPath2() {
        return path2;
    }

    public PathCouple(List<Vertex> path1, List<Vertex> path2) {

        this.path1 = path1;
        this.path2 = path2;
    }
}