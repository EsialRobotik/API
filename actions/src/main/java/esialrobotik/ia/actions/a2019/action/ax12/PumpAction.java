package esialrobotik.ia.actions.a2019.action.ax12;

import esialrobotik.ia.actions.a2019.action.Action;
import esialrobotik.ia.actions.a2019.action.ActionException;
import esialrobotik.ia.actions.a2019.ax12.AX12Link;
import esialrobotik.ia.actions.a2019.ax12.AX12LinkException;

public class PumpAction extends Action {
	
	protected AX12Link ax12Link;
	protected boolean enable;
	protected boolean actionDone;
	protected PUMP pumpId;
	
	public enum PUMP {
		GAUCHE,
		DROITE,
	}

	public PumpAction(AX12Link ax12Link, PUMP pumpId, boolean enable) {
		this.ax12Link = ax12Link;
		this.enable = enable;
		this.pumpId = pumpId;
		this.actionDone = false;
	}
	
	@Override
	public void doAction() throws ActionException {
		try {
			if (this.pumpId == PUMP.GAUCHE) {
				this.ax12Link.enableDtr(this.enable);	
			} else {
				this.ax12Link.enableRts(this.enable);
			}
			this.actionDone = true;
		} catch (AX12LinkException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean actionDone() throws ActionException {
		return this.actionDone;
	}

	@Override
	public String getActionId() {
		return "pump";
	}

	@Override
	public String getActionActuatorId() {
		return this.pumpId.name();
	}

	@Override
	public String getActionValueAsString() {
		return this.enable ? "enable" : "disable";
	}
	
	public PUMP getPump() {
		return this.pumpId;
	}
	
	public boolean getEnable() {
		return this.enable;
	}

}
