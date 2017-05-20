package esialrobotik.ia.actions.a2017;

import esialrobotik.ia.actions.ActionExecutor;
import esialrobotik.ia.utils.communication.raspberry.Serial;

/**
 * Created by franc on 20/05/2017.
 */
public abstract class ActionAX12Abstract implements ActionExecutor {

    Serial serialAX12;

    public ActionExecutor init(Serial serialAX12) {
        this.serialAX12 = serialAX12;
        return this;
    }

}
