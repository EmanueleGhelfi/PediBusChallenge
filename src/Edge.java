/**
 * Created by emilianogagliardi on 20/01/2017.
 */
public class Edge {
    private double coordX;
    private double coordY;
    //TODO possible optimization with parameter distance and distance from S


    public Edge(double coordX, double coordY) {
        this.coordX = coordX;
        this.coordY = coordY;
    }

    public double getCoordX() {
        return coordX;
    }

    public double getCoordY() {
        return coordY;
    }
}
