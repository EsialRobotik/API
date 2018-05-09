package esialrobotik.ia.asserv;

import com.google.gson.JsonObject;
import esialrobotik.ia.utils.log.LoggerFactory;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

/**
 * Created by Guillaume on 14/05/2017.
 */
public class AsservAPIConfiguration {

    private int baud;
    private String serie;

    private Logger logger;

    @Inject
    public AsservAPIConfiguration() {
        logger = LoggerFactory.getLogger(AsservAPIConfiguration.class);
    }

    public void loadConfig(JsonObject object) {
        logger.info("AsservAPIConfiguration = " + object.toString());
        baud = object.get("baud").getAsInt();
        serie = object.get("serie").getAsString();
    }

    public int getBaud() {
        return this.baud;
    }

    public String getSeriePort() {
        return serie;
    }
}
