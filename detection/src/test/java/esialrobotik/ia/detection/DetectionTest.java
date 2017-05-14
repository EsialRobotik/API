package esialrobotik.ia.detection;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by franc on 10/02/2017.
 */
public class DetectionTest {
    String configure = "{" +
            "  \"type\":\"test\", \n" +
            "  \"gpioList\":[ \n " +
            "    {\n" +
            "       \"in\":1, \n" +
            "       \"out\":2 \n" +
            "    }\n" +
            "  ]\n" +
            "}";

    String configureSRF04 = "{" +
            "  \"type\":\"srf04\", \n" +
            "  \"gpioList\":[ \n " +
            "    {\n" +
            "       \"in\":1, \n" +
            "       \"out\":2 \n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Test
    public void testLoading() {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(configure).getAsJsonObject();

        DetectionModuleConfiguration config = new DetectionModuleConfiguration(object);
        Injector injector = Guice.createInjector(new DetectionAPIModule(config));

        DetectionInterface detectionInterface = injector.getInstance(DetectionInterface.class);

        detectionInterface.init();
        long[] res = detectionInterface.ultraSoundDetection();
        Assert.assertEquals(1, res.length);
        Assert.assertEquals(2000000, res[0]);
    }

    @Test
    public void testLoadingSrf04() {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(configureSRF04).getAsJsonObject();

        DetectionModuleConfiguration config = new DetectionModuleConfiguration(object);
        Assert.assertEquals(SRF04JNI.class, config.getUltraSoundClass());
        Injector injector = Guice.createInjector(new DetectionAPIModule(config));

        DetectionInterface detectionInterface = injector.getInstance(DetectionInterface.class);
    }
}
