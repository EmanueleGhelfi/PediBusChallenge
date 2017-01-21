import GraphCore.Arc;
import GraphCore.Vertex;
import org.jgrapht.graph.SimpleWeightedGraph;

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


}
