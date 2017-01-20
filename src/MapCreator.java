import org.jgrapht.EdgeFactory;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;

/**
 * Created by emanueleghelfi on 20/01/2017.
 */
public class MapCreator {

    private UndirectedGraph<Node, DefaultEdge> undirectedGraph;

    public UndirectedGraph<Node,DefaultEdge> getUndirectedGraph(){
        return undirectedGraph;
    }

    public MapCreator(){
        undirectedGraph = new SimpleWeightedGraph<Node, DefaultEdge>(DefaultEdge.class);
    }

    public void addNode(Node node){
        undirectedGraph.addVertex(node);
    }

    public void createGraph(){

        List<Node> alreadyVisitedNodes = new ArrayList<Node>();
        for(Node node: undirectedGraph.vertexSet()){
            alreadyVisitedNodes.add(node);

        }
    }
}
