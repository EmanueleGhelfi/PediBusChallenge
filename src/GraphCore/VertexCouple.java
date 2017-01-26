package GraphCore;

/**
 * Created by emanueleghelfi on 26/01/2017.
 */
public class VertexCouple {

    private Vertex sourceVertex;
    private Vertex targetVertex;


    public VertexCouple(Vertex sourceVertex, Vertex targetVertex) {
        this.sourceVertex = sourceVertex;
        this.targetVertex = targetVertex;
    }


    public Vertex getSourceVertex() {
        return sourceVertex;
    }

    public Vertex getTargetVertex() {
        return targetVertex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VertexCouple that = (VertexCouple) o;

        if (sourceVertex != null ? !sourceVertex.equals(that.sourceVertex) : that.sourceVertex != null) return false;
        return targetVertex != null ? targetVertex.equals(that.targetVertex) : that.targetVertex == null;
    }

    @Override
    public int hashCode() {
        int result = sourceVertex != null ? sourceVertex.hashCode() : 0;
        result = 31 * result + (targetVertex != null ? targetVertex.hashCode() : 0);
        return result;
    }
}
