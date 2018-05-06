package esialrobotik.ia.actions.a2018.eau;

import esialrobotik.ia.actions.a2018.ActionAX12Abstract;

/**
 * Prépare les tubes pour un remplissage avant que le robot ne les présente sur le côté de la colonne d'eau
 * @author gryttix
 *
 */
public class RemplissagePreparation extends ActionAX12Abstract {

	@Override
	protected void childExecution() {
		go(ACTION_AX12.EAU_RAIL_GARAGE);
		go(ACTION_AX12.EAU_PENTE_REMPLISSAGE);
		go(ACTION_AX12.EAU_ORIENTATION_DROIT);
		attendreImmobilisation(AX12_NAME.RAIL, AX12_NAME.ORIENTATION, AX12_NAME.PENTE);
	}

}