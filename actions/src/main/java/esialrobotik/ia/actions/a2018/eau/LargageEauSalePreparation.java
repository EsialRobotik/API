package esialrobotik.ia.actions.a2018.eau;

import esialrobotik.ia.actions.a2018.ActionAX12Abstract;

/**
 * Prépare les tubes pour larguer l'eau sale
 * @author gryttix
 *
 */
public class LargageEauSalePreparation extends ActionAX12Abstract {

	@Override
	protected void childExecution() {
		go(ACTION_AX12.EAU_RAIL_MILIEU_VIDANGE);
		go(ACTION_AX12.EAU_ORIENTATION_DROIT);
		go(ACTION_AX12.EAU_PENTE_VERTICALE);
		attendreImmobilisation(AX12_NAME.RAIL, AX12_NAME.ORIENTATION, AX12_NAME.PENTE);
	}

}