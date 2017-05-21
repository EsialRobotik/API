package esialrobotik.ia.utils.communication.raspberry;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import esialrobotik.ia.utils.log.LoggerFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Communication I2C
 *
 * Communication I2C pour une raspberry pi via Pi4J
 * @see <a href='https://github.com/Pi4J/pi4j/blob/master/pi4j-example/src/main/java/I2CExample.java'>Pi4J I2C Example</a>
 */
public class I2C {

    /**
     * Pi4J I2C device
     */
    protected I2CDevice i2CDevice;

    /**
     * Adresse du device
     */
    protected int deviceAddress;

    /**
     * Logger
     */
    protected Logger logger = null;

    /**
     * Constructeur
     * @param deviceAddress Adresse du device (ex : 0x39)
     */
    public I2C(int deviceAddress) {
        logger = LoggerFactory.getLogger(I2C.class);

        try {
            logger.info("I2C " + deviceAddress + " init");
            I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_1);
            i2CDevice = i2c.getDevice(deviceAddress);
        } catch (I2CFactory.UnsupportedBusNumberException | IOException e) {
            logger.error("I2C " + deviceAddress + " init fail : " + e.getMessage());
        }
    }

    /**
     * Constructeur
     * @param deviceAddress Adresse du device (ex : 0x39)
     */
    public I2C(byte deviceAddress) {
        logger = LoggerFactory.getLogger(I2C.class);

        try {
            logger.info("I2C " + deviceAddress + " init");
            I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_1);
            i2CDevice = i2c.getDevice(deviceAddress);
        } catch (I2CFactory.UnsupportedBusNumberException | IOException e) {
            logger.error("I2C " + deviceAddress + " init fail : " + e.getMessage());
        }
    }

    /**
     * Lit la valeur d'un registre du device via l'I2C
     * @param register Registre à lire
     * @return Valeur du registre
     */
    public int read(byte register) {
        try {
            logger.info("I2C " + deviceAddress + " read register " + register);
            return i2CDevice.read(register);
        } catch (IOException e) {
            logger.error("I2C " + deviceAddress + " read register " + register + " fail : " + e.getMessage());
            return 0;
        }
    }

    public void write(byte register, byte value) {
        try {
            logger.info("I2C " + deviceAddress + " write register " + register + " with value " + value);
            i2CDevice.write(register, value);
        } catch (IOException e) {
            logger.info("I2C " + deviceAddress + " write register " + register + " with value " + value + " fail : " + e.getMessage());
        }
    }

    public void write(byte value) {
        try {
            logger.info("I2C " + deviceAddress + " write value " + value);
            i2CDevice.write(value);
        } catch (IOException e) {
            logger.info("I2C " + deviceAddress + " write value " + value + " fail : " + e.getMessage());
        }
    }

}
