package esialrobotik.ia.actions;

import com.google.gson.JsonObject;

/**
 * Created by icule on 21/05/17.
 */
public class ActionModuleConfiguration {

    private String serialPort;
    private int baud;

    public ActionModuleConfiguration() {
    }

    public void loadConfig(JsonObject object) {
        baud = object.get("baud").getAsInt();
        serialPort = object.get("serie").getAsString();
    }

    public int getBaud() {
        return this.baud;
    }

    public String getSerialPort() {
        return this.serialPort;
    }
}
