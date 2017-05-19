package esialrobotik.ia.actions.a2017;

import esialrobotik.ia.actions.ActionExecutor;
import esialrobotik.ia.actions.ActionInterface;

/**
 * Created by franc on 19/05/2017.
 */
public class Actions implements ActionInterface {

    public Actions() {
        // TODO instancier la liaison série pour jouer avec les AX12
    }

    @Override
    public ActionExecutor getActionExecutor(int id) {
        return null;
    }

}
