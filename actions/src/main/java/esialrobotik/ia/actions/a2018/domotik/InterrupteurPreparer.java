package esialrobotik.ia.actions.a2018.domotik;

import esialrobotik.ia.actions.a2018.ActionAX12Abstract;

/**
 * Pr�pare les tubes pour allumer le panneau domotique quand le robot est devant son interrupteur
 * @author gryttix
 *
 */
public class InterrupteurPreparer extends ActionAX12Abstract {

	@Override
	protected void childExecution() {
		System.out.println("Préparation interrupteur !!!!");
		go(ACTION_AX12.EAU_ORIENTATION_DROIT);
		go(ACTION_AX12.EAU_PENTE_VERTICALE);
		go(ACTION_AX12.EAU_RAIL_MILIEU_VIDANGE);
		attendreImmobilisation(AX12_NAME.RAIL, AX12_NAME.PENTE, AX12_NAME.ORIENTATION);
	}

}