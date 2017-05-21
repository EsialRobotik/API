package esialrobotik.ia.asserv.raspberry;

import com.google.inject.Inject;
import com.pi4j.io.serial.Baud;
import com.pi4j.io.serial.SerialDataEventListener;
import esialrobotik.ia.asserv.AsservAPIConfiguration;
import esialrobotik.ia.asserv.AsservInterface;
import esialrobotik.ia.asserv.Position;
import esialrobotik.ia.utils.communication.raspberry.Serial;

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

    protected MovementDirection direction;

    /**
     * Status de la dernière commande
     */
    protected int asservStatus;

    /**
     * Taille de la file de commande restant dans l'asserv
     */
    protected int queueSize;

    /**
     * Logger
     */
//    protected Logger logger = null;

    /**
     * Constructeur
     * @param serialPort Port série
     * @param baudRate Baud rate
     */
    public Asserv(String serialPort, Baud baudRate) {
//        logger = LoggerFactory.getLogger(Asserv.class);

//        logger.info("Initialisation de la liason série de l'asserv, port =  " + serialPort + ", baudRate = " + baudRate.getValue());
        serial = new Serial(serialPort, baudRate);
        serial.addReaderListeners((SerialDataEventListener) serialDataEvent -> {
            try {
                parseAsservPosition(serialDataEvent.getAsciiString());
//                logger.debug("Position : " + getPosition().toString());
            } catch (IOException e) {
//                logger.error("Echec du parsing de la position : " + e.getMessage());
            }
        });

        position = new Position(0, 0);
    }

    /**
     * Constructeur
     * @param configuration Configuration object of the asserv API
     */
    @Inject
    public Asserv(AsservAPIConfiguration configuration) {

        String serialPort = configuration.getSeriePort();
        Baud baudRate = Baud.getInstance(configuration.getBaud());
//        logger = LoggerFactory.getLogger(Asserv.class);

//        logger.info("Initialisation de la liason série de l'asserv, port =  " + serialPort + ", baudRate = " + baudRate.getValue());
        serial = new Serial(serialPort, baudRate);
        serial.addReaderListeners((SerialDataEventListener) serialDataEvent -> {
            try {
                parseAsservPosition(serialDataEvent.getAsciiString());
//                logger.debug("Position : " + getPosition().toString());
            } catch (IOException e) {
//                logger.error("Echec du parsing de la position : " + e.getMessage());
            }
        });

        position = new Position(0, 0);
    }

    /*******************************************************************************************************************
     * Commandes basiques
     ******************************************************************************************************************/

    @Override
    public void emergencyStop() {
//        logger.info("emergencyStop");
        serial.write("h");
    }

    @Override
    public void emergencyReset() {
//        logger.info("emergencyReset");
        serial.write("r");
    }

    @Override
    public void go(int dist) {
//        logger.info("go : " + dist);
        serial.write("v" + dist);
    }

    @Override
    public void turn(int degree) {
//        logger.info("turn : " + degree);
        serial.write("t" + degree);
    }

    /*******************************************************************************************************************
     * Commandes GOTO
     ******************************************************************************************************************/

    @Override
    public void goTo(Position position) {
//        logger.info("goTo : " + position.toString());
        serial.write("go" + position.getX() + "#" + position.getY());
    }

    @Override
    public void goToChain(Position position) {
//        logger.info("goToChain : " + position.toString());
        serial.write("ge" + position.getX() + "#" + position.getY());
    }

    @Override
    public void goToReverse(Position position) {
//        logger.info("goToReverse : " + position.toString());
        serial.write("gb" + position.getX() + "#" + position.getY());
    }

    @Override
    public void face(Position position) {
//        logger.info("goToFace : " + position.toString());
        serial.write("gf" + position.getX() + "#" + position.getY());
    }

    /*******************************************************************************************************************
     * Commandes controle odométrie
     ******************************************************************************************************************/

    @Override
    public void setOdometrieX(int x) {
//        logger.info("setOdometrieX : " + x);
        serial.write("Osx" + x);
    }

    @Override
    public void setOdometrieY(int y) {
//        logger.info("setOdometrieY : " + y);
        serial.write("Osy" + y);
    }

    @Override
    public void setOdometireTheta(double theta) {
//        logger.info("setOdometireTheta");
        serial.write("Osa");
    }

    /*******************************************************************************************************************
     * Commandes controle régulateurs
     ******************************************************************************************************************/

    @Override
    public void enableLowSpeed(boolean enable) {
//        logger.info("enableLowSpeed : " + enable);
        serial.write(enable ? "Rle" : "Rld");
    }

    @Override
    public void enableRegulatorAngle(boolean enable) {
//        logger.info("enableRegulatorAngle : " + enable);
        serial.write(enable ? "Rae" : "Rad");
    }

    @Override
    public void resetRegulatorAngle() {
//        logger.info("resetRegulatorAngle");
        serial.write("Rar");
    }

    @Override
    public void enableRegulatorDistance(boolean enable) {
//        logger.info("enableRegulatorDistance : " + enable);
        serial.write(enable ? "Rde" : "Rdd");
    }

    @Override
    public void resetRegulatorDistance() {
//        logger.info("resetRegulatorDistance");
        serial.write("Rdr");
    }

    /**
     * Parse le retour de la boucle d'asserv contenant la position du robot
     * #<positionX>;<positionY>;<angle>;<commandStatus>;<cmdQueueSize>;<vitesseG>;<vitesseD>
     * @param str Position du robot renvoyée par l'asserv
     */
    private void parseAsservPosition(String str) {
        str = str.trim(); // Un petit trim pour virer la merde
        if (str.startsWith("#")) {
            str = str.substring(1);
            if (str.contains("#")) { // Si par hasard on reçoit deux lignes à la fois, on abandonne
                return;
            }
            String[] data = str.split(";");

            position.setX(Integer.parseInt(data[0]));
            position.setY(Integer.parseInt(data[1]));
            position.setTheta(Double.parseDouble(data[2]));
            asservStatus = Integer.parseInt(data[3]);
            queueSize = Integer.parseInt(data[4]);

            int vitesseG = Integer.parseInt(data[5]);
            int vitesseD = Integer.parseInt(data[6]);
            if(vitesseD > 0 && vitesseG > 0) {
                direction = MovementDirection.FORWARD;
            }
            else if(vitesseD < 0 && vitesseG < 0) {
                direction = MovementDirection.BACKWARD;
            }
            else {
                direction = MovementDirection.NONE;
            }
        }
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public MovementDirection getMovementDirection() {
        return null;
    }

    @Override
    public int getAsservStatus() {
        return asservStatus;
    }

    @Override
    public int getQueueSize() {
        return queueSize;
    }

    public static void main(String... args) {
        Asserv asserv = new Asserv("/dev/serial/by-id/usb-mbed_Microcontroller_101000000000000000000002F7F2854A-if01", Baud._230400);
        asserv.go(200);
        asserv.go(-200);
        asserv.go(200);
        asserv.go(-200);
        asserv.go(200);
        asserv.go(-200);
        asserv.go(200);
        asserv.go(-200);
        asserv.go(200);
        asserv.go(-200);
    }
}
