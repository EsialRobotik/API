package esialrobotik.ia.actions.a2017.bras;

import esialrobotik.ia.actions.a2017.ActionAX12Abstract;

/**
 * Created by franc on 19/05/2017.
 * Le bras est rentré, on le déploie partiellement et on largue le module à l'horizontale dans une zone de dépôt
 */
public class BrasLacherModuleHorizontal extends ActionAX12Abstract {

	@Override
	protected void childExecution() {
		go(ETAT.BRAS_DEPOSER);
		attend(200);
		go(ETAT.POIGNET_HORIZONTAL);
		attend(500);
		go(ETAT.MAIN_DEPOSE);	
	}

}
