package esialrobotik.ia.actions.a2017;

import com.pi4j.io.serial.Baud;
import esialrobotik.ia.actions.ActionExecutor;
import esialrobotik.ia.actions.ActionInterface;
import esialrobotik.ia.actions.a2017.bras.*;
import esialrobotik.ia.actions.a2017.minerai.MineraiLarguer;
import esialrobotik.ia.actions.a2017.minerai.MineraiRamasser;
import esialrobotik.ia.actions.a2017.minerai.MineraiRentrer;
import esialrobotik.ia.utils.communication.raspberry.Serial;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by franc on 19/05/2017.
 */
public class Actions implements ActionInterface {

    Serial serialAX12;

    List<ActionExecutor> actionExecutors;

    public Actions() {
        // TODO implémenter le bon port série et passer ça en conf ? voir avec iCule
        this.serialAX12 = new Serial("/dev/ttyAMA0", Baud._115200);
        // On instancie les différents types d'actions, on fait les init et on stocke tout ça dans la liste
        actionExecutors = new ArrayList<>();

        /**
         * 1 - Ramasser du minerai
         * 2 - Largueur du minerai dans la zone de départ
         * 3 - Rentrer le ramasse minerai
         */
        actionExecutors.add(new MineraiRamasser().init(this.serialAX12));
        actionExecutors.add(new MineraiLarguer().init(this.serialAX12));
        actionExecutors.add(new MineraiRentrer().init(this.serialAX12));

        /**
         * 4 - Sortir le bras
         * 5 - Saisir un module
         * 6 - Rentrer le bras
         * 7 - Lacher un module à l'horizontale
         * 8 - Lacher un module à la verticale
         */
        // TODO vérifier que les actions suffisent
        actionExecutors.add(new BrasSortir().init(this.serialAX12));
        actionExecutors.add(new BrasSaisirModule().init(this.serialAX12));
        actionExecutors.add(new BrasRentrer().init(this.serialAX12));
        actionExecutors.add(new BrasLacherModuleHorizontal().init(this.serialAX12));
        actionExecutors.add(new BrasLacherModuleVertical().init(this.serialAX12));
    }

    @Override
    public ActionExecutor getActionExecutor(int id) {
        return this.actionExecutors.get(id);
    }

}
