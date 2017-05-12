package esialrobotik.ia.utils;

/**
 * Created by icule on 12/05/17.
 */
public class PolarCoordinatePoint {
    private double theta;
    private double radius;

    public PolarCoordinatePoint(double theta, double radius) {
        this.theta = theta;
        this.radius = radius;
    }

    public double getTheta() {
        return this.theta;
    }

    public double getRadius() {
        return this.radius;
    }
}
