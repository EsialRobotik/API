package esialrobotik.ia.actions.a2019.action;

public abstract class Action {
	
	/**
	 * Apply the state to the AX12
	 * 
	 * @throws AX12ActionException
	 */
	public abstract void doAction() throws ActionException;
	
	/**
	 * Tells if the action has been done
	 * Always returns false if the doAction() has never been called or its last call returned an error
	 * 
	 * @return
	 * @throws ActionException if the previous call to doAction() has thrown an error
	 */
	public abstract boolean actionDone() throws ActionException;
	
	/**
	 * @return
	 */
	public abstract String getActionId();
	
	/**
	 * 
	 * @return
	 */
	public abstract String getActionActuatorId();
	
	/**
	 * 
	 * @return
	 */
	public abstract String getActionValueAsString();
	
}
