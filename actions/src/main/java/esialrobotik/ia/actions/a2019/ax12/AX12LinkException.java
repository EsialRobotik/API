package esialrobotik.ia.actions.a2019.ax12;

public class AX12LinkException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public AX12LinkException(String reason) {
		super(reason);
	}
	
	public AX12LinkException(String reason, Throwable previous) {
		super(reason, previous);
	}
	
}
