package esialrobotik.ia.detection;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;

/**
 * Created by icule on 12/05/17.
 */
public class DetectionAPIModule extends AbstractModule{
    protected void configure() {
        bind(DetectionInterface.class).to(DetectionInterfaceImpl.class).in(Singleton.class);
    }
}
