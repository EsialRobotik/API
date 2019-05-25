package esialrobotik.ia.actions.a2019.action.ax12;

import esialrobotik.ia.actions.a2019.action.Action;
import esialrobotik.ia.actions.a2019.action.ActionException;
import esialrobotik.ia.actions.a2019.ax12.AX12;
import esialrobotik.ia.actions.a2019.ax12.AX12Exception;
import esialrobotik.ia.actions.a2019.ax12.AX12LinkException;
import esialrobotik.ia.actions.a2019.ax12.value.AX12Position;

public class AX12PositionAction extends Action {
	
	protected AX12Position angle;
	protected AX12 ax12;
	
	public AX12PositionAction(AX12 ax12, AX12Position angle) {
		this.ax12 = ax12;
		this.angle = angle;
	}
	
	@Override
	public void doAction() throws ActionException {
		try {
			this.ax12.setServoPosition(angle);
		} catch (AX12LinkException | AX12Exception e) {
			throw new ActionException("Error applying position state", e);
		}
	}

	@Override
	public boolean actionDone() {
		try {
			return !this.ax12.isMoving();
		} catch (AX12LinkException | AX12Exception e) {
			return false;
		}
	}

	@Override
	public String getActionId() {
		return "position";
	}

	@Override
	public String getActionValueAsString() {
		return angle.getValueAsString();
	}

	@Override
	public String getActionActuatorId() {
		return "" + this.ax12.getAddress();
	}
	
	public AX12 getAX12() {
		return this.ax12;
	}
	
	public AX12Position getAX12Position() {
		return this.angle;
	}
}
