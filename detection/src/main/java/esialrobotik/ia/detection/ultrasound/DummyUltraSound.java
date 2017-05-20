package esialrobotik.ia.detection.ultrasound;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import esialrobotik.ia.detection.DetectionModuleConfiguration;

/**
 * Dummy class create for test purpose.
 *
 * Created by Guillaume on 14/05/2017.
 */
public class DummyUltraSound implements UltraSoundInterface {

    private DetectionModuleConfiguration.GPioPair pair;

    @Inject
    public DummyUltraSound(@Assisted DetectionModuleConfiguration.GPioPair pair) {
        this.pair = pair;
    }

    public void init() {

    }

    public long getMeasure() {
        return 2000000; //We don't want to detect something with this class
    }

    public DetectionModuleConfiguration.GPioPair getGPioPair() {
        return this.pair;
    }

}
