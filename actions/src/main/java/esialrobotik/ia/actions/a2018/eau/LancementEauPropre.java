package esialrobotik.ia.actions.a2018.eau;

import esialrobotik.ia.actions.a2018.ActionAX12Abstract;

/**
 * Vide les tubes gauche et droit du robot dans le lanceur puis range les tubes
 * @author gryttix
 *
 */
public class LancementEauPropre extends ActionAX12Abstract {

	@Override
	protected void childExecution() {
		// Allumage du lanceur
		this.allumerLanceur(true);
		
		// Positionnement du tube droit devant le lanceur
		go(ACTION_AX12.EAU_RAIL_LANCEUR_DROIT);
		go(ACTION_AX12.EAU_ORIENTATION_LANCEUR_DROIT);
		attendreImmobilisation(AX12_NAME.RAIL, AX12_NAME.ORIENTATION);
		go(ACTION_AX12.EAU_PENTE_QUASI_HORIZONTALE);
		attendreImmobilisation(AX12_NAME.PENTE);
		
		// Feu !
		go(ACTION_AX12.EAU_PENTE_FORTE_DROIT);
		attend(2000);
		
		// Positionnement du tube gauche devant le lanceur
		go(ACTION_AX12.EAU_PENTE_QUASI_HORIZONTALE);
		attendreImmobilisation(AX12_NAME.PENTE);
		go(ACTION_AX12.EAU_RAIL_LANCEUR_GAUCHE);
		go(ACTION_AX12.EAU_ORIENTATION_LANCEUR_GAUCHE);
		attendreImmobilisation(AX12_NAME.RAIL, AX12_NAME.ORIENTATION);
		
		// Feu !
		go(ACTION_AX12.EAU_PENTE_FORTE_GAUCHE);
		attend(2000);
		
		// Rangement des tubes et extinction du lanceur
		this.allumerLanceur(false);
		go(ACTION_AX12.EAU_PENTE_VERTICALE);
		go(ACTION_AX12.EAU_RAIL_GARAGE);
		go(ACTION_AX12.EAU_ORIENTATION_DROIT);
		attendreImmobilisation(AX12_NAME.RAIL, AX12_NAME.PENTE, AX12_NAME.ORIENTATION);
		
	}

}