package esialrobotik.ia.detection.lidar.rplidar.raspberry;

public class LidarPoint {
    // Angle en radians
    public double angle;

    // Distance en mm
    public int distance;

    public LidarPoint(double angle, int distance) {
        this.angle = angle;
        this.distance = distance;
    }
}
