package esialrobotik.ia.actions;

import com.google.gson.JsonObject;
import esialrobotik.ia.utils.log.LoggerFactory;

import java.io.File;

import org.apache.logging.log4j.Logger;

/**
 * Created by icule on 21/05/17.
 */
public class ActionModuleConfiguration {

    private String serialPort;
    private int baud;
    private String dataDir;

    private Logger logger;

    public ActionModuleConfiguration() {
        logger = LoggerFactory.getLogger(ActionModuleConfiguration.class);
    }

    public void loadConfig(JsonObject object) {
        logger.info("ActionModuleConfiguration = " + object.toString());
        baud = object.get("baud").getAsInt();
        serialPort = object.get("serie").getAsString();
        dataDir = object.get("dataDir").getAsString();
    }

    public int getBaud() {
        return this.baud;
    }

    public String getSerialPort() {
        return this.serialPort;
    }
    
    public File getDataDir() {
    	return new File(this.dataDir);
    }
}
