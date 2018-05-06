package esialrobotik.ia.actions.a2018.bras;

import esialrobotik.ia.actions.a2018.ActionAX12Abstract;

/**
 * Sort le bras droit du robot
 * @author gryttix
 *
 */
public class BrasDroitSortir  extends ActionAX12Abstract {

	@Override
	protected void childExecution() {
		go(ACTION_AX12.BRAS_DROIT_SORTIR);
		attendreImmobilisation(AX12_NAME.BRAS_DROIT);
	}

}