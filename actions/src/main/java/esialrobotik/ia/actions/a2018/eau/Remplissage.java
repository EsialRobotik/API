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
		
		for (ACTION_AX12 action : new ACTION_AX12[]{ACTION_AX12.EAU_RAIL_REMPLISSAGE_1_A, ACTION_AX12.EAU_RAIL_REMPLISSAGE_1_B, ACTION_AX12.EAU_RAIL_REMPLISSAGE_2_A, ACTION_AX12.EAU_RAIL_REMPLISSAGE_2_B}) {
			go(action);
			attendreImmobilisation(AX12_NAME.RAIL);
			secouer();
			attend(250);
		}
	}
	
	protected void secouer() {
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