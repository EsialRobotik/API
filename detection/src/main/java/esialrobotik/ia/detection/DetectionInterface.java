package esialrobotik.ia.detection;

import esialrobotik.ia.utils.PolarCoordinatePoint;

import java.util.List;

/**
 * Created by franc on 10/02/2017.
 */
public interface DetectionInterface {
    void init();

    void startDetection();

    //We never know right
    void stopDetection();

    int[] ultraSoundDetection();

    List<PolarCoordinatePoint> getLidarDetection();
}
