package esialrobotik.ia.actions.a2019.action.ax12;

import esialrobotik.ia.actions.a2019.action.Action;
import esialrobotik.ia.actions.a2019.action.ActionException;
import esialrobotik.ia.actions.a2019.ax12.AX12;
import esialrobotik.ia.actions.a2019.ax12.AX12Exception;
import esialrobotik.ia.actions.a2019.ax12.AX12LinkException;
import esialrobotik.ia.actions.a2019.ax12.value.AX12Compliance;

public class BehaviourAction extends Action {
	
	protected int speed;
	protected int acceleration;
	protected AX12Compliance compliance;
	protected AX12 ax12;
	
	protected boolean actionDone = false;
	
	public BehaviourAction(AX12 ax12, int speed, int acceleration, AX12Compliance compliance) {
		if (speed < 0 || speed > 1023) {
			throw new IllegalArgumentException("Speed must be >= 0 and <= 1023");
		}
		
		if (acceleration < 0 || acceleration > 255) {
			throw new IllegalArgumentException("acceleration must be >= 0 and <= 255");
		}
		
		this.ax12 = ax12;
		this.speed = speed;
		this.acceleration = acceleration;
		this.compliance = compliance;
		this.actionDone = false;
	}

	@Override
	public void doAction() throws ActionException {
		try {
			this.ax12.setServoSpeed(speed);
		} catch (AX12LinkException | AX12Exception | IllegalArgumentException e) {
			e.printStackTrace();
			throw new ActionException("Error applying speed value", e);
		}
		
		try {
			this.ax12.setCcwComplianceSlope(acceleration);
			this.ax12.setCwComplianceSlope(acceleration);
		} catch (AX12LinkException | AX12Exception | IllegalArgumentException e) {
			e.printStackTrace();
			throw new ActionException("Error applying acceleration value", e);
		}
		
		try {
			this.ax12.setCcwComplianceMargin(compliance);
			this.ax12.setCwComplianceMargin(compliance);
		} catch (AX12LinkException | AX12Exception | IllegalArgumentException e) {
			throw new ActionException("Error applying compliance value", e);
		}
		
		this.actionDone = true;
	}

	@Override
	public boolean actionDone() throws ActionException {
		return this.actionDone;
	}

	@Override
	public String getActionId() {
		return "behaviour";
	}

	@Override
	public String getActionActuatorId() {
		return ""+this.ax12.getAddress();
	}

	@Override
	public String getActionValueAsString() {
		return "speed:"+this.speed + "/acc:" + this.acceleration+"/comp:"+this.compliance.getFriendlyValue();
	}
	
	public int getSPeed() {
		return speed;
	}
	
	public int getAcceleration() {
		return acceleration;
	}
	
	public AX12Compliance getCompliance() {
		return compliance;
	}
	
	public AX12 getAx12() {
		return this.ax12;
	}

}
