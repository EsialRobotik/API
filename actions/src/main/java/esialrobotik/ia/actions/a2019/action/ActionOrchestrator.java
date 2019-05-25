package esialrobotik.ia.actions.a2019.action;

import java.util.ArrayList;
import java.util.List;

public class ActionOrchestrator {
	
	protected List<ActionPool> actionPoolList;
	protected Boolean playing;
	
	public ActionOrchestrator() {
		playing = false;
		actionPoolList = new ArrayList<>();
	}
	
	public void addActionPool(ActionPool actionsPool) {
		this.actionPoolList.add(actionsPool);
	}
	
	public int getActionPoolCount() {
		return this.actionPoolList.size();
	}
	
	public int getPoolActionCount(int poolIndex) {
		return this.actionPoolList.get(poolIndex).getActionCount();
	}
	
	public ActionPool getPool(int index) {
		return actionPoolList.get(index);
	}
	
	public Action getPoolAction(int poolIndex, int actionIndex) {
		return this.actionPoolList.get(poolIndex).getAction(actionIndex);
	}
	
	public void addActionToPool(int poolIndex, Action action) {
		actionPoolList.get(poolIndex).addAction(action);
	}
	
	public void removeAllActions(int poolIndex) {
		actionPoolList.get(poolIndex).removeAllActions();
	}
	
	public void replacePool(int poolIndex, ActionPool ap) {
		actionPoolList.remove(poolIndex);
		actionPoolList.add(poolIndex, ap);
	}
	
	public void playPool(int poolIdx) {
		try {
			actionPoolList.get(poolIdx).doActions();
		} catch (ActionPoolException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Blocking method that plays all pool.
	 * Method execution can be stopped from another Thread by calling {@link #stopPlayingPools}
	 */
	public void playAllPool() {
		synchronized(playing) {
			playing = true;
		}
		try {
			for (ActionPool pool : actionPoolList) {
				synchronized(playing) {
					if (playing == false) {
						return;
					}
				}
				pool.doActions();
			}
		} catch (ActionPoolException e) {
			e.printStackTrace();
		}
		synchronized(playing) {
			playing = false;
		}
	}
	
	public void stopPlayingPools() {
		synchronized(playing) {
			playing = false;
		}
	}
	
	public void movePoolUp(int poolPosition) {
		if (poolPosition < 1) {
			return;
		}
		ActionPool a = actionPoolList.remove(poolPosition);
		actionPoolList.add(poolPosition - 1, a);
	}
	
	public void movePoolDown(int poolPosition) {
		if (poolPosition >= actionPoolList.size() - 1) {
			return;
		}
		ActionPool a = actionPoolList.remove(poolPosition);
		actionPoolList.add(poolPosition + 1, a);
	}
	
	public void removePool(int poolPosition) {
		actionPoolList.remove(poolPosition);
	}
	
}
