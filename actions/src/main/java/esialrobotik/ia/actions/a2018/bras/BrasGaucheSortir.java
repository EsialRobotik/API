package esialrobotik.ia.actions.a2018.bras;

import esialrobotik.ia.actions.a2018.ActionAX12Abstract;

/**
 * Sort le bras gauche du robot
 * @author gryttix
 *
 */
public class BrasGaucheSortir extends ActionAX12Abstract {

	@Override
	protected void childExecution() {
		go(ACTION_AX12.BRAS_GAUCHE_SORTIR);
		attendreImmobilisation(AX12_NAME.BRAS_GAUCHE);
	}

}
