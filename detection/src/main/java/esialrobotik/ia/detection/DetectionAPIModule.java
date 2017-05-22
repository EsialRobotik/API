package esialrobotik.ia.detection;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import esialrobotik.ia.detection.ultrasound.UltraSoundInterface;
import esialrobotik.ia.utils.rplidar.RpLidarLowLevelDriver;

import javax.inject.Singleton;

/**
 * Created by icule on 12/05/17.
 */
public class DetectionAPIModule extends AbstractModule{
    private DetectionModuleConfiguration detectionModuleConfiguration;

    public DetectionAPIModule(DetectionModuleConfiguration configuration) {
        this.detectionModuleConfiguration = configuration;
    }
    protected void configure() {
        bind(DetectionInterface.class).to(DetectionInterfaceImpl.class).in(Singleton.class);
        bind(DetectionModuleConfiguration.class).toInstance(detectionModuleConfiguration);

        install(new FactoryModuleBuilder()
                .implement(UltraSoundInterface.class, detectionModuleConfiguration.getUltraSoundClass())
                .build(UltraSoundInterface.UltraSoundInterfaceFactory.class));

        // Loading of the lidar
        // NOTE the listener is not bind here and nee to be bind somewhere else
        bind(String.class).annotatedWith(Names.named("lidarPort")).toInstance(detectionModuleConfiguration.getLidarPort());
        bind(RpLidarLowLevelDriver.class).in(Singleton.class);
    }
}
