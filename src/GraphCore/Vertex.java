package GraphCore;

/**
 * Created by emilianogagliardi on 20/01/2017.
 */
public class Vertex {
    private int name;
    private double coordX;
    private double coordY;
    //TODO possible optimization with parameter distance and distance from S


    public Vertex(int name, double coordX, double coordY) {
        this.name = name;
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public double getCoordX() {
        return coordX;
    }

    public double getCoordY() {
        return coordY;
    }

    public int getName() {
        return name;
    }

    public double computeDistance (Vertex v) {
        return Math.sqrt(Math.pow(this.coordX - v.coordX, 2) + Math.pow(this.coordY - v.coordY, 2));
    }

    //compute euclidean distance from school to the vertex
    public double computeDistanceFromSchool () {
        return this.computeDistance(new Vertex(0, 0, 0));
    }

    @Override
    public String toString() {
        return "GraphCore.Vertex{" +
                "name=" + name +
                ", coordX=" + coordX +
                ", coordY=" + coordY +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex)) return false;

        Vertex vertex = (Vertex) o;

        if (name != vertex.name) return false;
        if (Double.compare(vertex.coordX, coordX) != 0) return false;
        return Double.compare(vertex.coordY, coordY) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name;
        temp = Double.doubleToLongBits(coordX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(coordY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
