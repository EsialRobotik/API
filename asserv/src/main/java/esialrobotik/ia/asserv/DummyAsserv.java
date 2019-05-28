package esialrobotik.ia.asserv;

import esialrobotik.ia.utils.log.LoggerFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

/**
 * For test.
 *
 * Created by Guillaume on 14/05/2017.
 */
public class DummyAsserv implements AsservInterface {

    /**
     * Logger
     */
    protected Logger logger = null;

    public DummyAsserv() {
        logger = LoggerFactory.getLogger(DummyAsserv.class);
        logger.info("Dummy Asserv GOOOO!");
    }

    @Override
    public void initialize() {
        logger.info("initialize");
    }

    @Override
    public void stop() {
        logger.info("stop");
    }

    @Override
    public void emergencyStop() {
        logger.info("emergencyStop");
    }

    @Override
    public void emergencyReset() {
        logger.info("emergencyReset");
    }

    @Override
    public void go(int dist) {
        logger.info("go");
    }

    @Override
    public void turn(int degree) {
        logger.info("turn");
    }

    @Override
    public void goTo(Position position) {
        logger.info("goTo");
    }

    @Override
    public void goToChain(Position position) {
        logger.info("goToChain");
    }

    @Override
    public void goToReverse(Position position) {
        logger.info("goToReverse");
    }

    @Override
    public void face(Position position) {
        logger.info("face");
    }

    @Override
    public void setOdometrieTheta(double theta) {

    }

    @Override
    public void setOdometrieX(int x) {

    }

    @Override
    public void setOdometrieY(int y) {

    }

    @Override
    public void enableLowSpeed(boolean enable) {

    }

    @Override
    public void enableRegulatorAngle(boolean enable) {

    }

    @Override
    public void resetRegulatorAngle() {

    }

    @Override
    public void enableRegulatorDistance(boolean enable) {

    }

    @Override
    public void resetRegulatorDistance() {

    }

    @Override
    public AsservStatus getAsservStatus() {
        return AsservStatus.STATUS_IDLE;
    }

    @Override
    public int getQueueSize() {
        return 0;
    }

    @Override
    public Position getPosition() {
        return null;
    }

    @Override
    public MovementDirection getMovementDirection() {
        return null;
    }

    @Override
    public void calage(boolean positiveY) throws InterruptedException {
        // TODO Auto-generated method stub
    }

    @Override
    public void goStart(boolean isColor0) throws InterruptedException {

    }

    public static void main(String... args) {
        LoggerFactory.init(Level.TRACE);
        DummyAsserv asserv = new DummyAsserv();
        asserv.go(10);
        asserv.go(10);
        asserv.go(10);
        asserv.go(10);
        asserv.go(10);
        asserv.go(10);
        asserv.go(10);
        asserv.go(10);
    }
}
