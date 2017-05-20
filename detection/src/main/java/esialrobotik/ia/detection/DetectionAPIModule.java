package esialrobotik.ia.detection;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import esialrobotik.ia.detection.ultrasound.UltraSoundInterface;

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
    }
}
