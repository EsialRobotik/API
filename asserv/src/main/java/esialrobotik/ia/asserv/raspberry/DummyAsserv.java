package esialrobotik.ia.asserv.raspberry;

import esialrobotik.ia.asserv.AsservInterface;
import esialrobotik.ia.asserv.Position;

/**
 * For test.
 *
 * Created by Guillaume on 14/05/2017.
 */
public class DummyAsserv implements AsservInterface {
  @Override
  public void emergencyStop() {

  }

  @Override
  public void emergencyReset() {

  }

  @Override
  public void go(int dist) {

  }

  @Override
  public void turn(int degree) {

  }

  @Override
  public void goTo(Position position) {

  }

  @Override
  public void goToChain(Position position) {

  }

  @Override
  public void goToReverse(Position position) {

  }

  @Override
  public void face(Position position) {

  }

  @Override
  public void setOdometireTheta(double theta) {

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
  public int getAsservStatus() {
    return 0;
  }

  @Override
  public int getQueueSize() {
    return 0;
  }

  @Override
  public Position getPosition() {
    return null;
  }
}
