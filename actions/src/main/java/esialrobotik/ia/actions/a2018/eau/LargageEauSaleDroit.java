package esialrobotik.ia.actions.a2018.eau;

import esialrobotik.ia.actions.a2018.ActionAX12Abstract;

/**
 * Largue l'eau sale par le côté droit du robot.
 * Avant de faire cela il faut préparer le largage avec LargageEauSalePreparation !
 * @author gryttix
 *
 */
public class LargageEauSaleDroit extends ActionAX12Abstract {

	@Override
	protected void childExecution() {
		go(ACTION_AX12.EAU_ORIENTATION_VIDANGE_DROIT);
		attendreImmobilisation(AX12_NAME.ORIENTATION);
		attend(2000);
		go(ACTION_AX12.EAU_ORIENTATION_DROIT);
		// Pas besoin d'attendre la fin de la rotation qui remet les tubes à la verticale
	}

}