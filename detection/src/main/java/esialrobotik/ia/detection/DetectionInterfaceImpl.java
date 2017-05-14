package esialrobotik.ia.detection;

import com.google.inject.Inject;
import esialrobotik.ia.utils.PolarCoordinatePoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by icule on 12/05/17.
 */
public class DetectionInterfaceImpl implements DetectionInterface{
    private DetectionModuleConfiguration configuration;
    private List<UltraSoundInterface> srf04List;

    @Inject
    private UltraSoundInterface.UltraSoundInterfaceFactory ultraSoundInterfaceFactory;

    @Inject
    public DetectionInterfaceImpl(DetectionModuleConfiguration detectionModuleConfiguration) {
        this.configuration = detectionModuleConfiguration;
    }

    public void init() {
        srf04List = new ArrayList<UltraSoundInterface>();
        for(DetectionModuleConfiguration.GPioPair pair : configuration.getGPioPairList()) {
            srf04List.add(ultraSoundInterfaceFactory.create(pair));
        }
    }

    public void startDetection() {

    }

    public void stopDetection() {

    }

    public long[] ultraSoundDetection() {
        long[] res = new long[srf04List.size()];
        for(int i = 0; i < srf04List.size(); ++i) {
            res[i] = srf04List.get(i).getMeasure();
        }
        return res;
    }

    public List<PolarCoordinatePoint> getLidarDetection() {
        return null;
    }
}
