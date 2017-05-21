package esialrobotik.ia.actions.a2017.bras;

import esialrobotik.ia.actions.a2017.ActionAX12Abstract;

/**
 * Created by franc on 19/05/2017.
 * Le bras est dans le robot, on le déploie
 */
public class BrasSortir extends ActionAX12Abstract {

	@Override
	protected void childExecution() {
		go(ETAT.BRAS_BAISSE);
		go(ETAT.POIGNET_VERTICAL);
		attend(300);
		go(ETAT.MAIN_GRANDE_OUVERTE);
	}

}
