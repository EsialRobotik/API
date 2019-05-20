package esialrobotik.ia.detection.lidar;

import esialrobotik.ia.detection.lidar.rplidar.raspberry.LidarPoint;

import java.util.List;

public interface LidarInterface {
    interface LidarInterfaceFactory {
        LidarInterface create(String lidarPort);
    }

    void init();
    void stop();

    List<LidarPoint> getMeasures();
}
