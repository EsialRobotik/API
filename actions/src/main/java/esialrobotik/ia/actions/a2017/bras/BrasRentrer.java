package esialrobotik.ia.actions.a2017.bras;

import esialrobotik.ia.actions.a2017.ActionAX12Abstract;

/**
 * Created by franc on 19/05/2017.
 * Le bras est sortie, on le rentre dans le robot
 */
public class BrasRentrer extends ActionAX12Abstract {

	@Override
	protected void childExecution() {
		go(ETAT.MAIN_FERMEE);
		attend(200);
		go(ETAT.POIGNET_VERTICAL);
		attend(400);
		go(ETAT.BRAS_LEVE);
	}

}
