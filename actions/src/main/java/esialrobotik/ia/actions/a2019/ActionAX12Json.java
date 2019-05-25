package esialrobotik.ia.actions.a2019;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Semaphore;

import esialrobotik.ia.actions.ActionExecutor;
import esialrobotik.ia.actions.a2019.action.ActionOrchestrator;
import esialrobotik.ia.actions.a2019.action.ActionOrchestratorHelper;
import esialrobotik.ia.actions.a2019.ax12.AX12Link;

public class ActionAX12Json implements ActionExecutor {

	protected ActionOrchestrator ao;
	protected Semaphore semaphore;
	protected boolean finished;
	
	public ActionAX12Json(AX12Link ax12link, File fileToLoad) {
		try {
			ao = ActionOrchestratorHelper.unserializeFromJson(ax12link, fileToLoad);
		} catch (IOException e) {
			e.printStackTrace();
		}
		semaphore = new Semaphore(1);
		finished = false;
	}
	
	@Override
	public void execute() {
		if (finished || !semaphore.tryAcquire()) {
			return;
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				ActionAX12Json.this.ao.playAllPool();
				finished = true;
				semaphore.release();
			}
		}).start();;
	}

	@Override
	public boolean finished() {
		return this.finished;
	}

	@Override
	public void resetActionState() {
		this.finished = false;
	}

}
