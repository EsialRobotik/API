package esialrobotik.ia.detection.ultrasound;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import esialrobotik.ia.detection.DetectionModuleConfiguration;
import esialrobotik.ia.utils.log.LoggerFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Dummy class create for test purpose.
 *
 * Created by Guillaume on 14/05/2017.
 */
public class DummyUltraSound implements UltraSoundInterface {

    private DetectionModuleConfiguration.GPioPair pair;

    /**
     * Logger
     */
    protected Logger logger = null;

    public DummyUltraSound() {
        logger = LoggerFactory.getLogger(DummyUltraSound.class);
    }

    @Inject
    public DummyUltraSound(@Assisted DetectionModuleConfiguration.GPioPair pair) {
        this.pair = pair;
        logger = LoggerFactory.getLogger(DummyUltraSound.class);
    }

    public void init() {
        logger.info("init DummyUltraSound");
    }

    public long getMeasure() {
        logger.info("getMeasure");
        return 2000000; //We don't want to detect something with this class
    }

    public DetectionModuleConfiguration.GPioPair getGPioPair() {
        return this.pair;
    }

    public static void main(String... args) {
        LoggerFactory.init(Level.TRACE);
        DummyUltraSound dummyUltraSound = new DummyUltraSound();
        dummyUltraSound.init();
        dummyUltraSound.getMeasure();
        dummyUltraSound.getMeasure();
        dummyUltraSound.getMeasure();
        dummyUltraSound.getMeasure();
    }

}
