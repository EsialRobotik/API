package esialrobotik.ia.asserv;

/**
 * Position et cap sur le repère de la table
 */
public class Position {

    /**
     * Position X en mm sur la table
     */
    private int x;

    /**
     * Position Y en mm sur la table
     */
    private int y;

    /**
     * Cap en radians
     */
    private double theta;

    /**
     * Constructeur sans angle
     * @param x X en mm
     * @param y Y en mm
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Construteur avec angle
     * @param x X en mm
     * @param y Y en mm
     * @param theta theta en radian
     */
    public Position(int x, int y, double theta) {
        this.x = x;
        this.y = y;
        this.theta = theta;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                ", theta=" + theta +
                '}';
    }

}
