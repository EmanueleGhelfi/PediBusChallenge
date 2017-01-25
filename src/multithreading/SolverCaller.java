package multithreading;

import GraphCore.Arc;
import GraphCore.Vertex;
import org.jgrapht.graph.SimpleWeightedGraph;
import solvers.SolverInterface;

import java.util.concurrent.Callable;

/**
 * Created by emanueleghelfi on 25/01/2017.
 */
public class SolverCaller implements Callable<SimpleWeightedGraph<Vertex,Arc>> {

    private SolverInterface solverInterface;
    private SimpleWeightedGraph<Vertex,Arc> graph;
    private double alpha;

    public SolverCaller(SolverInterface solverInterface,SimpleWeightedGraph<Vertex,Arc> graph,double alpha) {
        this.solverInterface = solverInterface;
        this.graph = graph;
        this.alpha = alpha;
    }

    @Override
    public SimpleWeightedGraph<Vertex, Arc> call() throws Exception {
        return solverInterface.solve(graph,alpha,0);
    }
}
