package esialrobotik.ia.actions.a2019.action;

import java.util.ArrayList;
import java.util.List;

public class ActionPool {

	protected List<ActionWrapper> actionList;
	public static final int DEFAULT_RETRY_PER_ACTION = 5;
	public static final long DEFAULT_TIMEOUT = 5000;
	
	public ActionPool() {
		actionList = new ArrayList<>();
	}
	
	public void addAction(Action a) {
		actionList.add(new ActionWrapper(a));
	}
	
	public void removeAllActions() {
		this.actionList.clear();
	}
	
	public int getActionCount() {
		return this.actionList.size();
	}
	
	public Action getAction(int index) {
		return actionList.get(index).action;
	}
	
	public void doActions() throws ActionPoolException {
		doActions(DEFAULT_RETRY_PER_ACTION, DEFAULT_TIMEOUT);
	}
	
	protected void resetActions() {
		for (ActionWrapper aw : actionList) {
			aw.actionDone = false;
			aw.applyDone = false;
			aw.retryCount = 0;
		}
	}
	
	public void doActions(int retryPerAction, long timeout) throws ActionPoolException {
		timeout += System.currentTimeMillis();
		
		resetActions();
		// Trigger actions
		boolean doAnotherLoop;
		do {
			doAnotherLoop = false;
			for (ActionWrapper aw : actionList) {
				if (System.currentTimeMillis() > timeout) {
					throw new ActionPoolException("Actions trigger took to much time", null);
				}
				try {
					if (!aw.applyDone) {
						aw.action.doAction();
						aw.applyDone = true;
					}
					Thread.sleep(5); // Dont overflow ax12
				} catch (ActionException | InterruptedException e) {
					e.printStackTrace();
					aw.retryCount++;
					if (aw.retryCount > retryPerAction) {
						throw new ActionPoolException("An action exceeded the max retry limit : " + e.getMessage(), e);
					}
					doAnotherLoop = true;
				}
			}	
		} while (doAnotherLoop);
		
		// Wait end of actions
		do {
			doAnotherLoop = false;
			for (ActionWrapper aw : actionList) {
				if (System.currentTimeMillis() > timeout) {
					throw new ActionPoolException("Actions ending took to much time", null);
				}
				try {
					if (!aw.actionDone) {
						if (aw.action.actionDone()) {
							aw.actionDone = true;
						} else {
							doAnotherLoop = true;
						}
						Thread.sleep(5); // Dont overflow ax12
					}
				} catch (ActionException | InterruptedException e) {
					doAnotherLoop = true;
				}
			}	
		} while (doAnotherLoop);
	}
	
	/**
	 * Keep state info about action
	 */
	private class ActionWrapper {
		
		public Action action;
		public int retryCount;
		public boolean applyDone;
		public boolean actionDone;
		
		public ActionWrapper(Action action) {
			this.action = action;
			this.retryCount = 0;
			this.applyDone = false;
			this.actionDone = false;
		}
		
	}
	
}
