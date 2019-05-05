package esialrobotik.ia.actions.a2018.eau;

import esialrobotik.ia.actions.a2018.ActionAX12Abstract;

/**
 * Met les tubes en mode "garage" sur le c�t� gauche du robot
 * @author gryttix
 *
 */
public class RangementTubes extends ActionAX12Abstract {

	@Override
	protected void childExecution() {
		go(ACTION_AX12.EAU_RAIL_GARAGE);
		go(ACTION_AX12.EAU_ORIENTATION_DROIT);
		go(ACTION_AX12.EAU_PENTE_VERTICALE);
		attendreImmobilisation(AX12_NAME.RAIL, AX12_NAME.ORIENTATION, AX12_NAME.PENTE);
	}

}