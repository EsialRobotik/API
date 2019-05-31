package esialrobotik.ia.utils.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationFactory;

/**
 * Created by icule on 27/03/17.
 */
public class LoggerFactory {
    private static boolean hasBeenInit = false;

    public static void init(final Level level){
        ConfigurationFactory.setConfigurationFactory(new CustomConfigurationFactory(level));
        hasBeenInit = true;
    }


    public static Logger getLogger(Class<?> clazz){
        if(!hasBeenInit){
            throw new RuntimeException("Log module hasn't been initialize.");
        }
        return LogManager.getLogger(clazz);
    }

    public static void shutdown() {
        LogManager.shutdown();
    }
}
