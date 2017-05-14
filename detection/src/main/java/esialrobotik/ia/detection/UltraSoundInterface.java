package esialrobotik.ia.detection;

/**
 * Created by Guillaume on 14/05/2017.
 */
public interface UltraSoundInterface {
    interface  UltraSoundInterfaceFactory {
      UltraSoundInterface create(DetectionModuleConfiguration.GPioPair pair);
    }

    void init();

    long getMeasure();
}
