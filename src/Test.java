import com.ampl.*;
import com.sun.xml.internal.rngom.digested.DDataPattern;
import org.jgrapht.EdgeFactory;
import org.jgrapht.alg.HamiltonianCycle;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.IOException;
import java.util.List;

/**
 * Created by emanueleghelfi on 20/01/2017.
 */
public class Test{

    public static void main(String[] args) throws IOException {
        Parser p = new Parser("pedibus_300.dat");
        List<Vertex> vList = HamiltonianCycle.getApproximateOptimalForCompleteGraph(p.createGraph());
        for (Vertex v : vList) {
            System.out.println(v);
        }
    }
}
