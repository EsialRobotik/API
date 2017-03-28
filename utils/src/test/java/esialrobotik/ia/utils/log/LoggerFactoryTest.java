package esialrobotik.ia.utils.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;

import static org.junit.Assert.fail;

/**
 * Created by icule on 27/03/17.
 */
public class LoggerFactoryTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp(){
        //We need to delete the previous log file
        File f = new File(CustomConfigurationFactory.rollingFilename);
        if(f.exists()){
            f.delete();
        }
    }

    @Test
    public void testGetLogger() throws IOException {
        Logger logger = null;
        try {
            logger = LoggerFactory.getLogger(LoggerFactoryTest.class);
            fail("Uninitialized loggerFactory work");
        } catch(RuntimeException e){

        }

        LoggerFactory.init(Level.TRACE);
        logger = LoggerFactory.getLogger(LoggerFactoryTest.class);
        Assert.assertEquals(Level.TRACE, logger.getLevel());
        logger.fatal("Test");
        logger.error("Test");
        logger.warn("Test");
        logger.info("Test");
        logger.debug("Test");
        logger.trace("Test");

        BufferedReader br = new BufferedReader(new FileReader(new File(CustomConfigurationFactory.rollingFilename)));
        Assert.assertTrue(br.readLine().contains("FATAL"));
        Assert.assertTrue(br.readLine().contains("ERROR"));
        Assert.assertTrue(br.readLine().contains("WARN"));
        Assert.assertTrue(br.readLine().contains("INFO"));
        Assert.assertTrue(br.readLine().contains("DEBUG"));
        Assert.assertTrue(br.readLine().contains("TRACE"));
    }
}