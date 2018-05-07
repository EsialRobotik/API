package esialrobotik.ia.actions.a2018;

import com.pi4j.io.serial.Baud;
import esialrobotik.ia.actions.ActionExecutor;
import esialrobotik.ia.actions.ActionInterface;
import esialrobotik.ia.actions.ActionModuleConfiguration;
import esialrobotik.ia.actions.a2018.bras.BrasDroitRentrer;
import esialrobotik.ia.actions.a2018.bras.BrasDroitSortir;
import esialrobotik.ia.actions.a2018.bras.BrasGaucheRentrer;
import esialrobotik.ia.actions.a2018.bras.BrasGaucheSortir;
import esialrobotik.ia.actions.a2018.domotik.InterrupteurAllumer;
import esialrobotik.ia.actions.a2018.domotik.InterrupteurPreparer;
import esialrobotik.ia.actions.a2018.eau.*;
import esialrobotik.ia.utils.communication.raspberry.Serial;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by franc on 06/05/2018.
 */
public class Actions implements ActionInterface {

    private Serial serialAX12;

    private List<ActionExecutor> actionExecutors;

    @Inject
    public Actions(ActionModuleConfiguration actionModuleConfiguration) {
        this.serialAX12 = new Serial(actionModuleConfiguration.getSerialPort(), Baud.getInstance(actionModuleConfiguration.getBaud()));
        this.serialAX12.setDTR(false);
        // On instancie les différents types d'actions, on fait les init et on stocke tout ça dans la liste
        actionExecutors = new ArrayList<>();

        /*
         * Controle des bras latéraux
         * 0 - Rentrer Bras Droit
         * 1 - Sortir Bras Droit
         * 2 - Rentrer Bras Gauche
         * 3 - Sortir Bras Gauche
         */
        actionExecutors.add(new BrasDroitRentrer().init(this.serialAX12));
        actionExecutors.add(new BrasDroitSortir().init(this.serialAX12));
        actionExecutors.add(new BrasGaucheRentrer().init(this.serialAX12));
        actionExecutors.add(new BrasGaucheSortir().init(this.serialAX12));

        /*
         * 4 - Lancement Eau Propre
         * 5 - Largage Eau Sale Droit
         * 6 - Largage Eau Sale Gauche
         * 7 - Largage Eau Sale Préparation
         * 8 - Rangement Tubes
         * 9 - Remplissage
         * 10 - Remplissage Préparation
         * 11 - Remplissage Rangement
         */
        actionExecutors.add(new LancementEauPropre().init(this.serialAX12));
        actionExecutors.add(new LargageEauSaleDroit().init(this.serialAX12));
        actionExecutors.add(new LargageEauSaleGauche().init(this.serialAX12));
        actionExecutors.add(new LargageEauSalePreparation().init(this.serialAX12));
        actionExecutors.add(new RangementTubes().init(this.serialAX12));
        actionExecutors.add(new Remplissage().init(this.serialAX12));
        actionExecutors.add(new RemplissagePreparation().init(this.serialAX12));
        actionExecutors.add(new RemplissageRangement().init(this.serialAX12));

        /*
         * 12 - Préparation de l'allumage de l'interrupteur
         * 13 - Allumage de l'interrupteur
         */
        actionExecutors.add(new InterrupteurPreparer().init(this.serialAX12));
        actionExecutors.add(new InterrupteurAllumer().init(this.serialAX12));
    }

    @Override
    public ActionExecutor getActionExecutor(int id) {
        return this.actionExecutors.get(id);
    }

}