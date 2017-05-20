package esialrobotik.ia.actions.a2017;

import com.pi4j.io.serial.Baud;
import esialrobotik.ia.actions.ActionExecutor;
import esialrobotik.ia.actions.ActionInterface;
import esialrobotik.ia.utils.communication.raspberry.Serial;

/**
 * Created by franc on 19/05/2017.
 */
public class Actions implements ActionInterface {

    Serial serialAX12;

    public Actions() {
        // TODO implémenter le bon port série et passer ça en conf ? voir avec iCule
        this.serialAX12 = new Serial("/dev/ttyAMA0", Baud._115200);
    }

    @Override
    public ActionExecutor getActionExecutor(int id) {
        return null;
    }

}
