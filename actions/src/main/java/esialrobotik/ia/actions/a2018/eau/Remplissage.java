package esialrobotik.ia.actions.a2018.eau;

import esialrobotik.ia.actions.a2018.ActionAX12Abstract;

/**
 * Remplit les tubes lorsque le robot est devant une colonne d'eau
 * Avant de faire cela il faut préparer le robot au remplissage avec RemplissagePreparation !
 * @author gryttix
 *
 */
public class Remplissage extends ActionAX12Abstract {

	@Override
	protected void childExecution() {
		// Remplissage partie 1
		go(ACTION_AX12.EAU_RAIL_REMPLISSAGE_1);
		attendreImmobilisation(AX12_NAME.RAIL);
		attend(2000);
		
		// Remplissage partie 2
		go(ACTION_AX12.EAU_RAIL_REMPLISSAGE_2);
		attendreImmobilisation(AX12_NAME.RAIL);
		attend(2000);
	}

}