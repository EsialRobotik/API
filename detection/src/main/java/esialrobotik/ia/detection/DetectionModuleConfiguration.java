package esialrobotik.ia.detection;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import esialrobotik.ia.detection.ultrasound.UltraSoundInterface;
import esialrobotik.ia.detection.ultrasound.srf04.raspberry.SRF04;
import esialrobotik.ia.detection.ultrasound.DummyUltraSound;
import esialrobotik.ia.utils.log.LoggerFactory;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guillaume on 14/05/2017.
 */
public class DetectionModuleConfiguration {

    public static class GPioPair {
        public int gpio_in;
        public int gpio_out;

        public GPioPair (int gpio_in, int gpio_out) {
            this.gpio_in = gpio_in;
            this.gpio_out = gpio_out;
        }
    }

    private List<GPioPair> gPioPairList;
    private Class<? extends UltraSoundInterface> ultraSoundClass;

    private String lidarPort;

    private Logger logger;

    @Inject
    public DetectionModuleConfiguration() {
        logger = LoggerFactory.getLogger(DetectionModuleConfiguration.class);
    }

    public void loadConfiguration(JsonObject configNode) {
        logger.info("DetectionModuleConfiguration = " + configNode.toString());

        lidarPort = null;
        gPioPairList = null;
        ultraSoundClass = null;

        if(configNode.has("ultrasound")) {
            setUltraConfig(configNode.get("ultrasound").getAsJsonObject());
        }
        if(configNode.has("lidar")) {
            lidarPort = configNode.get("lidar").getAsJsonObject().get("port").getAsString();
        }
    }

    private void setUltraConfig(JsonObject configNode) {
        this.gPioPairList = new ArrayList<GPioPair>();
        JsonArray gpioPairArray = configNode.getAsJsonArray("gpioList");
        for(JsonElement e : gpioPairArray) {
            JsonObject temp = e.getAsJsonObject();
            gPioPairList.add(new GPioPair(temp.get("in").getAsInt(), temp.get("out").getAsInt()));
        }

        String type = configNode.get("type").getAsString();
        if(type.equals("srf04")) {
            ultraSoundClass = SRF04.class;
        }
        else if(type.equals("test")) {
            ultraSoundClass = DummyUltraSound.class;
        }
    }

    public String getLidarPort() {
        return lidarPort;
    }

    public List<GPioPair> getGPioPairList() {
        return this.gPioPairList;
    }

    public Class<? extends UltraSoundInterface> getUltraSoundClass() {
        return this.ultraSoundClass;
    }
}
