package esialrobotik.ia.actions.a2018.eau;

import esialrobotik.ia.actions.a2018.ActionAX12Abstract;

/**
 * Une fois que les tubes sont remplis, remet les tubes correctement pour transporter l'eau sans risque
 * @author gryttix
 *
 */
public class RemplissageRangement extends ActionAX12Abstract {

	@Override
	protected void childExecution() {
		go(ACTION_AX12.EAU_RAIL_MILIEU_VIDANGE);
		go(ACTION_AX12.EAU_ORIENTATION_DROIT);
		go(ACTION_AX12.EAU_PENTE_VERTICALE);
		attendreImmobilisation(AX12_NAME.RAIL, AX12_NAME.ORIENTATION, AX12_NAME.PENTE);
	}

}
