package esialrobotik.ia.actions.a2018.eau;

import esialrobotik.ia.actions.a2018.ActionAX12Abstract;

/**
 * Remplit les tubes lorsque le robot est devant une colonne d'eau
 * Avant de faire cela il faut pr�parer le robot au remplissage avec RemplissagePreparation !
 * @author gryttix
 *
 */
public class Remplissage extends ActionAX12Abstract {

	@Override
	protected void childExecution() {
		// Remplissage partie 1
		// On se place sous el tube
		go(ACTION_AX12.EAU_RAIL_REMPLISSAGE_1);
		attendreImmobilisation(AX12_NAME.RAIL);
		
		// On fait plusieurs fois un va et vient dans l'inclinaison du tube sous le reservoir
		secouer();
		attend(500);
		
		// Remplissage partie 2
		go(ACTION_AX12.EAU_RAIL_REMPLISSAGE_2);
		attendreImmobilisation(AX12_NAME.RAIL);
		
		secouer();
		attend(500);
	}
	
	protected void secouer() {
		// On fait plusieurs fois un va et vient dans l'inclinaison du tube sous le reservoir
		for (int i =0; i<2; i++) {
			go(ACTION_AX12.EAU_ORIENTATION_REMPLISSAGE_INCLINAISON_GAUCHE);
			attendreImmobilisation(AX12_NAME.ORIENTATION);
			go(ACTION_AX12.EAU_ORIENTATION_DROIT);
			attendreImmobilisation(AX12_NAME.ORIENTATION);
			go(ACTION_AX12.EAU_ORIENTATION_REMPLISSAGE_INCLINAISON_DROITE);
			attendreImmobilisation(AX12_NAME.ORIENTATION);
			go(ACTION_AX12.EAU_ORIENTATION_DROIT);
			attendreImmobilisation(AX12_NAME.ORIENTATION);
		}
	}

}