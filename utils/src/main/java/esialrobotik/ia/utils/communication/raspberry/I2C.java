package esialrobotik.ia.utils.communication.raspberry;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;

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
     * Constructeur
     * @param deviceAddress Adresse du device (ex : 0x39)
     */
    public I2C(int deviceAddress) {
        try {
            I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_1);
            i2CDevice = i2c.getDevice(deviceAddress);
        } catch (I2CFactory.UnsupportedBusNumberException e) {
            // TODO log
            e.printStackTrace();
        } catch (IOException e) {
            // TODO log
            e.printStackTrace();
        }
    }

    /**
     * Constructeur
     * @param deviceAddress Adresse du device (ex : 0x39)
     */
    public I2C(byte deviceAddress) {
        try {
            I2CBus i2c = I2CFactory.getInstance(I2CBus.BUS_1);
            i2CDevice = i2c.getDevice(deviceAddress);
        } catch (I2CFactory.UnsupportedBusNumberException e) {
            // TODO log
            e.printStackTrace();
        } catch (IOException e) {
            // TODO log
            e.printStackTrace();
        }
    }

    /**
     * Lit la valeur d'un registre du device via l'I2C
     * @param register Registre à lire
     * @return Valeur du registre
     */
    public int read(byte register) {
        try {
            return i2CDevice.read(register);
        } catch (IOException e) {
            // TODO log
            e.printStackTrace();
        }
        // TODO on intercepte l'exception ou pas ?
        return 0;
    }

    public void write(byte register, byte value) {
        try {
            i2CDevice.write(register, value);
        } catch (IOException e) {
            // TODO log
            e.printStackTrace();
        }
    }

}
