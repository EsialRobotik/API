package esialrobotik.ia.actions.a2017.bras;

import esialrobotik.ia.actions.a2017.ActionAX12Abstract;

/**
 * Created by franc on 19/05/2017.
 * Le bras est sortie, on ferme simplement la pince pour chopper le module
 */
public class BrasSaisirModule extends ActionAX12Abstract {

	@Override
	protected void childExecution() {
		go(ETAT.MAIN_FERMEE);
	}

}
