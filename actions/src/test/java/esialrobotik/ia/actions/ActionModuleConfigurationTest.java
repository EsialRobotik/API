package esialrobotik.ia.actions;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by icule on 21/05/17.
 */
public class ActionModuleConfigurationTest {
    private static final String config = "{" +
            "\"baud\":115200,\n" +
            "\"serie\":\"lala\"" +
            "} ";


    @Test
    public void testInstantiation() {
        JsonObject object = new JsonParser().parse(config).getAsJsonObject();

        ActionModuleConfiguration configuration = new ActionModuleConfiguration();
        configuration.loadConfig(object);
        assertEquals(115200, configuration.getBaud());
        assertEquals("lala", configuration.getSerialPort());
    }
}