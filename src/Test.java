import com.ampl.*;
import com.sun.xml.internal.rngom.digested.DDataPattern;
import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.IOException;

/**
 * Created by emanueleghelfi on 20/01/2017.
 */
public class Test extends SimpleWeightedGraph<Node,Arc>{

    public Test(EdgeFactory<Node, Arc> ef) {
        super(ef);
    }

    public Test(Class<? extends Arc> edgeClass) {
        super(edgeClass);
    }

    public static void main(String[] args){

        AMPL ampl  = new AMPL();
        try {
            ampl.read("pedibus_mod.mod");
            ampl.readData("pedibus_10.dat");
            Parameter alpha =ampl.getParameter("alpha");
            System.out.println(alpha);


        } catch (IOException e) {
            e.printStackTrace();
        }

        EdgeFactory edgeFactory = new ClassBasedEdgeFactory(Arc.class);

        SimpleWeightedGraph simpleWeightedGraph = new SimpleWeightedGraph(edgeFactory);





    }



}
