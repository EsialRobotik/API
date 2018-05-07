package esialrobotik.ia.actions.a2018.domotik;

import esialrobotik.ia.actions.a2018.ActionAX12Abstract;

/**
 * Allume l'interrupteur domotique puis remet les tubes verticaux
 * Avant cela, appeler InterrupteurPreparer
 * @author gryttix
 *
 */
public class InterrupteurAllumer extends ActionAX12Abstract {

	@Override
	protected void childExecution() {
		go(ACTION_AX12.EAU_PENTE_INTERRUPTEUR);
		attend(500);
		go(ACTION_AX12.EAU_PENTE_VERTICALE);
		attendreImmobilisation(AX12_NAME.PENTE);
	}
	
}