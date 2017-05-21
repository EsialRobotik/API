package esialrobotik.ia.actions.a2017.minerai;

import esialrobotik.ia.actions.a2017.ActionAX12Abstract;

/**
 * Created by franc on 19/05/2017.
 * Déploie le ramasse minerai
 */
public class MineraiLarguer extends ActionAX12Abstract {

	@Override
	protected void childExecution() {
		go(ETAT.MINERAI_LARGUER);
	}

}
