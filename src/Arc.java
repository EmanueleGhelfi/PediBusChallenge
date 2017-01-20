import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * Created by emanueleghelfi on 20/01/2017.
 */
public class Arc extends DefaultWeightedEdge{
    private double dangerousness;

    public Arc(double dangerousness) {
        this.dangerousness = dangerousness;
    }

    public double getDangerousness() {
        return dangerousness;
    }
}
