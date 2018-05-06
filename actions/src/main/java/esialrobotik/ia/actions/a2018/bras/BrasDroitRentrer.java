package esialrobotik.ia.actions.a2018.bras;

import esialrobotik.ia.actions.a2018.ActionAX12Abstract;

/**
 * Rentre le bras droit du robot
 * @author gryttix
 *
 */
public class BrasDroitRentrer  extends ActionAX12Abstract {

	@Override
	protected void childExecution() {
		go(ACTION_AX12.BRAS_DROIT_RENTRER);
		attendreImmobilisation(AX12_NAME.BRAS_DROIT);
	}

}