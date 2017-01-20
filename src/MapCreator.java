import org.jgrapht.EdgeFactory;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;

/**
 * Created by emanueleghelfi on 20/01/2017.
 */
public class MapCreator {

    private WeightedGraph<Node, DefaultEdge> weightedGraph;

    public WeightedGraph<Node,DefaultEdge> getUndirectedGraph(){
        return weightedGraph;
    }

    public MapCreator(){
        weightedGraph = new SimpleWeightedGraph<Node, DefaultEdge>(DefaultEdge.class);
    }

    public void addNode(Node node){
        weightedGraph.addVertex(node);
    }

    public void createGraph(){

        List<Node> alreadyVisitedNodes = new ArrayList<Node>();
        for(Node node: weightedGraph.vertexSet()){
            alreadyVisitedNodes.add(node);
            for(Node toNode: weightedGraph)


        }
    }
}
