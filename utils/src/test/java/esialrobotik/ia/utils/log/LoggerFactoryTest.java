package esialrobotik.ia.utils.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    @Test
    public void testLogInThreadPool() throws IOException {
        LoggerFactory.init(Level.TRACE);
        Logger logger = LoggerFactory.getLogger(LoggerFactoryTest.class);
        Assert.assertEquals(Level.TRACE, logger.getLevel());

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                logger.info("Test");
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new FileReader(new File(CustomConfigurationFactory.rollingFilename)));
        Assert.assertTrue(br.lines().count() == 10);
    }

    @Test
    public void testLogInThread() throws IOException {
        ThreadLog thread = new ThreadLog();
        thread.start();
        for (int i = 0; i < 10; i++) {
            thread.log("Test" + i);
        }
        thread.interrupt();

        BufferedReader br = new BufferedReader(new FileReader(new File(CustomConfigurationFactory.rollingFilename)));
        Assert.assertTrue(br.lines().count() == 10);
    }

    private class ThreadLog extends Thread {

        Logger logger;

        public ThreadLog() {
            LoggerFactory.init(Level.TRACE);
            logger = LoggerFactory.getLogger(LoggerFactoryTest.class);
        }

        @Override
        public void run() {
            while (!isInterrupted());
        }

        public void log(String str) {
            logger.info("Test");
        }
    }
}