package esialrobotik.ia.asserv.raspberry;

import com.pi4j.io.serial.Baud;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import esialrobotik.ia.asserv.AsservInterface;
import esialrobotik.ia.asserv.Position;
import esialrobotik.ia.utils.communication.raspberry.Serial;

import java.io.IOException;

/**
 * Implémentation de l'asservissement pour raspberry
 */
public class Asserv implements AsservInterface {

    private Serial serial;
    private Position position;
    private int lastCommandStatus;

    public Asserv(String serialPort, Baud baudRate) {
        serial = new Serial(serialPort, baudRate);
        serial.addReaderListeners((SerialDataEventListener) serialDataEvent -> {
            try {
                parseAsservPosition(serialDataEvent.getAsciiString());
            } catch (IOException e) {
                // TODO logger
                e.printStackTrace();
            }
        });
    }

    @Override
    public void enableLowSpeed(boolean enable) {
        serial.write("elw" + (enable ? 1 : 0));
    }

    @Override
    public void enableRegulatorAngle(boolean enable) {
        serial.write("era" + (enable ? 1 : 0));
    }

    @Override
    public void enableRegulatorDistance(boolean enable) {
        serial.write("erd" + (enable ? 1 : 0));
    }

    @Override
    public void resetTheta() {
        serial.write("rth");
    }

    @Override
    public void resetRegualtorAngle() {
        serial.write("rra");
    }

    @Override
    public void resetRegulatorDistance() {
        serial.write("rrd");
    }

    @Override
    public void defineX(int x) {
        serial.write("dfx" + x);
    }

    @Override
    public void defineY(int y) {
        serial.write("dfy" + y);
    }

    @Override
    public void definePosition(Position position) {
        serial.write("dfp" + position.x + "#" + position.y + "#" + position.theta);
    }

    @Override
    public void emergencyStop() {
        serial.write("h");
    }

    @Override
    public void emergencyReset() {
        serial.write("r");
    }

    @Override
    public void goTo(Position position) {
        serial.write("g" + position.x + "#" + position.y);
    }

    @Override
    public void goToReverse(Position position) {
        serial.write("b" + position.x + "#" + position.y);
    }

    @Override
    public void face(Position position) {
        serial.write("f" + position.x + "#" + position.y);
    }

    @Override
    public void go(int dist) {
        serial.write("v" + dist);
    }

    @Override
    public void turn(int degree) {
        serial.write("t" + degree);
    }

    @Override
    public Position askPosition() {
        final String[] position = new String[1];
        SerialDataEventListener listener = serialDataEvent -> {
            try {
                 position[0] = serialDataEvent.getAsciiString();
            } catch (IOException e) {
                // TODO logger ça
                e.printStackTrace();
            }
        };

        serial.addReaderListeners(listener);
        serial.write("p");

        while (position[0] == null) ;

        serial.removeReaderListeners(listener);
        // parse x%xy%ya%a
        return new Position(
                Integer.parseInt(position[0].substring(1, position[0].indexOf("y"))),
                Integer.parseInt(position[0].substring(position[0].indexOf("y") + 1, position[0].indexOf("a"))),
                Double.parseDouble(position[0].substring(position[0].indexOf("a") + 1))
        );
    }

    public void parseAsservPosition(String str) {
        // #x%lfy%lfa%lfd%dvg%dvd%d
        if (str.startsWith("#")) {
            str = str.substring(1);
            position.x = Integer.parseInt(str.substring(1, str.indexOf("y")));
            position.y = Integer.parseInt(str.substring(str.indexOf("y") + 1, str.indexOf("a")));
            position.theta = Integer.parseInt(str.substring(str.indexOf("a") + 1, str.indexOf("d")));
            lastCommandStatus = Integer.parseInt(str.substring(str.indexOf("d") + 1, str.indexOf("vg")));
            int motorSpeedLeft = Integer.parseInt(str.substring(str.indexOf("vg") + 1, str.indexOf("vd")));
            int motorSpeedRight = Integer.parseInt(str.substring(str.indexOf("vd") + 1));
        }

        // TODO logger le retour
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }

    public int getLastCommandStatus() {
        return lastCommandStatus;
    }

}
