package solvers;

import GraphCore.Arc;
import GraphCore.Vertex;
import org.jgrapht.graph.SimpleWeightedGraph;


/**
 * Created by emanueleghelfi on 25/01/2017.
 */
public interface SolverInterface {

    SimpleWeightedGraph<Vertex, Arc> solve(SimpleWeightedGraph<Vertex,Arc> graph,double alpha, int iteration);
}
