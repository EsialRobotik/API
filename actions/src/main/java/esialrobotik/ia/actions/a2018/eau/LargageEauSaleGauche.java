package esialrobotik.ia.actions.a2018.eau;

import esialrobotik.ia.actions.a2018.ActionAX12Abstract;

/**
 * Largue l'eau sale par le c�t� gauche du robot.
 * Avant de faire cela il faut pr�parer le largage avec LargageEauSalePreparation !
 * @author gryttix
 *
 */
public class LargageEauSaleGauche extends ActionAX12Abstract {

	@Override
	protected void childExecution() {
		go(ACTION_AX12.EAU_ORIENTATION_VIDANGE_GAUCHE);
		attendreImmobilisation(AX12_NAME.ORIENTATION);
		attend(200);
		go(ACTION_AX12.EAU_ORIENTATION_DROIT);
		// Pas besoin d'attendre la fin de la rotation qui remet les tubes � la verticale
	}

}