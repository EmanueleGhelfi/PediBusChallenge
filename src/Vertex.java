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

    @Override
    public String toString() {
        return "Vertex{" +
                "name=" + name +
                ", coordX=" + coordX +
                ", coordY=" + coordY +
                '}';
    }
}
