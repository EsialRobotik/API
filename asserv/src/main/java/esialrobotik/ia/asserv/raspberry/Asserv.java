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

    @Override
    public void enableLowSpeed(boolean enable) {
        logger.info("enableLowSpeed : " + enable);
        serial.write("elw" + (enable ? 1 : 0));
    }

    @Override
    public void enableRegulatorAngle(boolean enable) {
        logger.info("enableRegulatorAngle : " + enable);
        serial.write("era" + (enable ? 1 : 0));
    }

    @Override
    public void enableRegulatorDistance(boolean enable) {
        logger.info("enableRegulatorDistance : " + enable);
        serial.write("erd" + (enable ? 1 : 0));
    }

    @Override
    public void resetTheta() {
        logger.info("resetTheta");
        serial.write("rth");
    }

    @Override
    public void resetRegualtorAngle() {
        logger.info("resetRegualtorAngle");
        serial.write("rra");
    }

    @Override
    public void resetRegulatorDistance() {
        logger.info("resetRegulatorDistance");
        serial.write("rrd");
    }

    @Override
    public void defineX(int x) {
        logger.info("defineX : " + x);
        serial.write("dfx" + x);
    }

    @Override
    public void defineY(int y) {
        logger.info("defineY : " + y);
        serial.write("dfy" + y);
    }

    @Override
    public void definePosition(Position position) {
        logger.info("definePosition : " + position.toString());
        this.position = position;
        serial.write("dfp" + position.getX() + "#" + position.getY() + "#" + position.getTheta());
    }

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
    public void goTo(Position position) {
        logger.info("goTo : " + position.toString());
        serial.write("g" + position.getX() + "#" + position.getY());
    }

    @Override
    public void goToReverse(Position position) {
        logger.info("goToReverse : " + position.toString());
        serial.write("b" + position.getX() + "#" + position.getY());
    }

    @Override
    public void face(Position position) {
        logger.info("face : " + position.toString());
        serial.write("f" + position.getX() + "#" + position.getY());
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

    protected void parseAsservPosition(String str) {
        if (str.startsWith("#")) {
            position.setX(Integer.parseInt(str.substring(2, str.indexOf("y"))));
            position.setY(Integer.parseInt(str.substring(str.indexOf("y") + 1, str.indexOf("a"))));
            position.setTheta(Integer.parseInt(str.substring(str.indexOf("a") + 1, str.indexOf("d"))));
            lastCommandStatus = Integer.parseInt(str.substring(str.indexOf("d") + 1, str.indexOf("vg")));
//            int motorSpeedLeft = Integer.parseInt(str.substring(str.indexOf("vg") + 1, str.indexOf("vd")));
//            int motorSpeedRight = Integer.parseInt(str.substring(str.indexOf("vd") + 1));
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
