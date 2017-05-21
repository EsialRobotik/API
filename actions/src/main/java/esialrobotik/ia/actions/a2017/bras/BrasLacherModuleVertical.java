package esialrobotik.ia.actions.a2017.bras;

import esialrobotik.ia.actions.a2017.ActionAX12Abstract;

/**
 * Created by franc on 19/05/2017.
 * Le bras est sortie, on relache le module à la verticale (pour la zone de départ)
 */
public class BrasLacherModuleVertical extends ActionAX12Abstract {

    @Override
    public void childExecution() {
		go(ETAT.BRAS_BAISSE);
		go(ETAT.POIGNET_VERTICAL);
		attend(300);
		go(ETAT.MAIN_GRANDE_OUVERTE);
    }
    
}
