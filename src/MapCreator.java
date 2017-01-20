import org.jgrapht.EdgeFactory;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emanueleghelfi on 20/01/2017.
 */
public class MapCreator {

    private WeightedGraph<Vertex, Arc> weightedGraph;

    public WeightedGraph<Vertex,Arc> getUndirectedGraph(){
        return weightedGraph;
    }

    public MapCreator(){
        weightedGraph = new SimpleWeightedGraph<Vertex, Arc>(Arc.class);
    }

    public void addNode(Vertex node){
        weightedGraph.addVertex(node);
    }

    public void createGraph(){

        List<Vertex> alreadyVisitedNodes = new ArrayList<Vertex>();
        for(Vertex node: weightedGraph.vertexSet()){
            alreadyVisitedNodes.add(node);
            for(Vertex toNode: weightedGraph.vertexSet()){
                if(alreadyVisitedNodes.contains(toNode)){
                    Arc arc = new Arc(9);
                    weightedGraph.addEdge(node,toNode,arc);
                    //weightedGraph.setEdgeWeight(arc,);
                }
            }


        }
    }
}
