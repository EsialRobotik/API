package esialrobotik.ia.asserv.raspberry;

import com.pi4j.io.serial.Baud;
import com.pi4j.io.serial.SerialDataEventListener;
import esialrobotik.ia.asserv.AsservInterface;
import esialrobotik.ia.asserv.Position;
import esialrobotik.ia.utils.communication.raspberry.Serial;
import esialrobotik.ia.utils.log.LoggerFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Implémentation de l'asservissement pour raspberry
 */
public class Asserv implements AsservInterface {

    /**
     * Port série pour communiquer avec l'asserv
     */
    protected Serial serial;

    /**
     * Position du robot
     */
    protected Position position;

    /**
     * Status de la dernière commande
     */
    protected int lastCommandStatus;

    /**
     * Logger
     */
    protected Logger logger = null;

    /**
     * Constructeur
     * @param serialPort Port série vers l'asserv
     * @param baudRate Baud rate
     */
    public Asserv(String serialPort, Baud baudRate) {
        LoggerFactory.init(Level.TRACE);
        logger = LoggerFactory.getLogger(Asserv.class);

        logger.info("Initialisation de la liason série de l'asserv, port =  " + serialPort + ", baudRate = " + baudRate.getValue());
        serial = new Serial(serialPort, baudRate);
        serial.addReaderListeners((SerialDataEventListener) serialDataEvent -> {
            try {
                parseAsservPosition(serialDataEvent.getAsciiString());
                logger.debug("Position : " + getPosition().toString());
            } catch (IOException e) {
                logger.error("Echec du parsing de la position : " + e.getMessage());
            }
        });
    }

    /*******************************************************************************************************************
     * Commandes basiques
     ******************************************************************************************************************/

    @Override
    public void emergencyStop() {
        logger.info("emergencyStop");
        serial.write("h");
    }

    @Override
    public void emergencyReset() {
        logger.info("emergencyReset");
        serial.write("r");
    }

    @Override
    public void go(int dist) {
        logger.info("go : " + dist);
        serial.write("v" + dist);
    }

    @Override
    public void turn(int degree) {
        logger.info("turn : " + degree);
        serial.write("t" + degree);
    }

    /*******************************************************************************************************************
     * Commandes GOTO
     ******************************************************************************************************************/

    @Override
    public void goTo(Position position) {
        logger.info("goTo : " + position.toString());
        serial.write("go" + position.getX() + "#" + position.getY());
    }

    @Override
    public void goToReverse(Position position) {
        logger.info("goToReverse : " + position.toString());
        serial.write("gr" + position.getX() + "#" + position.getY());
    }

    @Override
    public void face(Position position) {
        logger.info("goToFace : " + position.toString());
        serial.write("gf" + position.getX() + "#" + position.getY());
    }

    /*******************************************************************************************************************
     * Commandes controle odométrie
     ******************************************************************************************************************/

    @Override
    public void setOdometrieX(int x) {
        logger.info("setOdometrieX : " + x);
        serial.write("Osx" + x);
    }

    @Override
    public void setOdometrieY(int y) {
        logger.info("setOdometrieY : " + y);
        serial.write("Osy" + y);
    }

    @Override
    public void setOdometireTheta(double theta) {
        logger.info("setOdometireTheta");
        serial.write("Osa");
    }

    /*******************************************************************************************************************
     * Commandes controle régulateurs
     ******************************************************************************************************************/

    @Override
    public void enableLowSpeed(boolean enable) {
        logger.info("enableLowSpeed : " + enable);
        serial.write(enable ? "Rle" : "Rld");
    }

    @Override
    public void enableRegulatorAngle(boolean enable) {
        logger.info("enableRegulatorAngle : " + enable);
        serial.write(enable ? "Rae" : "Rad");
    }

    @Override
    public void resetRegualtorAngle() {
        logger.info("resetRegualtorAngle");
        serial.write("Rar");
    }

    @Override
    public void enableRegulatorDistance(boolean enable) {
        logger.info("enableRegulatorDistance : " + enable);
        serial.write(enable ? "Rda" : "Rdd");
    }

    @Override
    public void resetRegulatorDistance() {
        logger.info("resetRegulatorDistance");
        serial.write("Rdr");
    }

    /**
     * Parse le retour de la boucle d'asserv contenant la position du robot
     * @param str Position du robot renvoyée par l'asserv
     */
    private void parseAsservPosition(String str) {
        if (str.startsWith("#")) {
            String[] data = str.substring(1).split(";");
            position.setX(Integer.parseInt(data[0]));
            position.setY(Integer.parseInt(data[1]));
            position.setTheta(Double.parseDouble(data[2]));
            lastCommandStatus = Integer.parseInt(data[3]);
        }
    }

    @Override
    public Position getPosition() {
        return position;
    }

    public int getLastCommandStatus() {
        return lastCommandStatus;
    }

}
