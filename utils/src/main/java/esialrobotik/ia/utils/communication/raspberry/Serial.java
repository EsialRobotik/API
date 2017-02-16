package esialrobotik.ia.utils.communication.raspberry;

import com.pi4j.io.serial.*;

import java.io.IOException;

/**
 * Communication serie
 *
 * Communication série via Pi4J
 * @see <a href="http://pi4j.com/example/serial.html">Pi4J Serial Example</a>
 */
public class Serial {

    /**
     * Pi4J Serial
     */
    protected com.pi4j.io.serial.Serial serial;

    /**
     * Constructeur
     * @param serialPort Nom du port série (ex : /dev/ttyUSB0 ou /dev/ttyAMA0
     * @param baudRate Baud rate
     */
    public Serial(String serialPort, Baud baudRate) {
        SerialConfig serialConfig = new SerialConfig();
        serialConfig.device(serialPort)
                .baud(baudRate)
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE);

        serial = SerialFactory.createInstance();
        try {
            serial.open(serialConfig);
        } catch (IOException e) {
            // TODO loger ça correctement
            e.printStackTrace();
        }
    }

    /**
     * Envoie une string sur la liaison série
     * @param string String à envoyer
     */
    public void write(String string) {
        try {
            serial.writeln(string);
        } catch (IOException e) {
            // TODO loger ça correctement
            e.printStackTrace();
        }
    }

    /**
     * Ajoute des listeners écoutant la liaison série
     * @param serialDataEventListeners Listeners écoutant la liaison série
     * @see <a href="http://pi4j.com/example/serial.html">Pi4J Serial Example</a>
     */
    public void addReaderListeners(SerialDataEventListener... serialDataEventListeners) {
        serial.addListener(serialDataEventListeners);
    }

    /**
     * Supprime des listeners
     * @param serialDataEventListeners Listeners à supprimer
     */
    public void removeReaderListeners(SerialDataEventListener... serialDataEventListeners) {
        serial.removeListener(serialDataEventListeners);
    }

}
