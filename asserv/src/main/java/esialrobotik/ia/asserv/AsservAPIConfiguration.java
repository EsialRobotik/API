package esialrobotik.ia.asserv;

import com.google.gson.JsonObject;
import com.pi4j.io.serial.Baud;

/**
 * Created by Guillaume on 14/05/2017.
 */
public class AsservAPIConfiguration {
  private int baud;
  private String serie;

  public AsservAPIConfiguration (JsonObject object) {
    baud = object.get("baud").getAsInt();
    serie = object.get("serie").getAsString();
  }

  public int getBaud() {
    return this.baud;
  }

  public String getSeriePort() {
    return serie;
  }
}
