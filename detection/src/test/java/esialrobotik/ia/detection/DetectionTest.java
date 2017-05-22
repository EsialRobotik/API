package esialrobotik.ia.detection;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Guice;
import com.google.inject.Injector;
import esialrobotik.ia.detection.ultrasound.srf04.raspberry.SRF04;
import esialrobotik.ia.utils.log.LoggerFactory;
import org.apache.logging.log4j.Level;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by franc on 10/02/2017.
 */



public class DetectionTest {
    String configure = "{\n" +
            "    \"ultrasound\" : {\n" +
            "      \"type\":\"test\",\n" +
            "      \"gpioList\":[\n" +
            "        {\n" +
            "          \"desc\" : \"Avant droit\",\n" +
            "          \"in\" : 0,\n" +
            "          \"out\": 2\n" +
            "        },\n" +
            "        {\n" +
            "          \"desc\" : \"Avant milieu\",\n" +
            "          \"in\" : 12,\n" +
            "          \"out\": 13\n" +
            "        },\n" +
            "        {\n" +
            "          \"desc\" : \"Avant gauche\",\n" +
            "          \"in\" : 21,\n" +
            "          \"out\": 22\n" +
            "        },\n" +
            "        {\n" +
            "          \"desc\" : \"Arrière\",\n" +
            "          \"in\" : 24,\n" +
            "          \"out\": 25\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    \"lidar\": {\n" +
            "      \"port\": \"/dev/serial/by-id/usb-Silicon_Labs_CP2102_USB_to_UART_Bridge_Controller_0001-if00-port0\"\n" +
            "    }\n" +
            "  }";

    String configureSRF04 = "{\n" +
            "    \"ultrasound\" : {\n" +
            "      \"type\":\"srf04\",\n" +
            "      \"gpioList\":[\n" +
            "        {\n" +
            "          \"desc\" : \"Avant droit\",\n" +
            "          \"in\" : 0,\n" +
            "          \"out\": 2\n" +
            "        },\n" +
            "        {\n" +
            "          \"desc\" : \"Avant milieu\",\n" +
            "          \"in\" : 12,\n" +
            "          \"out\": 13\n" +
            "        },\n" +
            "        {\n" +
            "          \"desc\" : \"Avant gauche\",\n" +
            "          \"in\" : 21,\n" +
            "          \"out\": 22\n" +
            "        },\n" +
            "        {\n" +
            "          \"desc\" : \"Arrière\",\n" +
            "          \"in\" : 24,\n" +
            "          \"out\": 25\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    \"lidar\": {\n" +
            "      \"port\": \"/dev/serial/by-id/usb-Silicon_Labs_CP2102_USB_to_UART_Bridge_Controller_0001-if00-port0\"\n" +
            "    }\n" +
            "  }";
    
    
    @Before
    public void setUp(){
       LoggerFactory.init(Level.TRACE);
    }

    @Test
    public void testLoading() {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(configure).getAsJsonObject();

        DetectionModuleConfiguration config = new DetectionModuleConfiguration();
        config.loadConfiguration(object);
        Injector injector = Guice.createInjector(new DetectionAPIModule(config));

        DetectionInterface detectionInterface = injector.getInstance(DetectionInterface.class);

        detectionInterface.init();
        long[] res = detectionInterface.ultraSoundDetection();
        Assert.assertEquals(4, res.length);
        Assert.assertEquals(2000000, res[0]);
    }

    @Test
    public void testLoadingSrf04() {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(configureSRF04).getAsJsonObject();

        DetectionModuleConfiguration config = new DetectionModuleConfiguration();
        config.loadConfiguration(object);
        Assert.assertEquals(SRF04.class, config.getUltraSoundClass());
        Injector injector = Guice.createInjector(new DetectionAPIModule(config));

        DetectionInterface detectionInterface = injector.getInstance(DetectionInterface.class);
    }
}
