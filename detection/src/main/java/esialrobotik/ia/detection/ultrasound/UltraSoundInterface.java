package esialrobotik.ia.detection.ultrasound;

import esialrobotik.ia.detection.DetectionModuleConfiguration;

/**
 * Created by Guillaume on 14/05/2017.
 */
public interface UltraSoundInterface {

    interface UltraSoundInterfaceFactory {
        UltraSoundInterface create(DetectionModuleConfiguration.GPioPair pair);
    }

    void init();

    long getMeasure();
}
