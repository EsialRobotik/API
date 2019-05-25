package esialrobotik.ia.actions.a2019.ax12.value;

import java.text.DecimalFormat;

public class AX12Position extends Ax12Value{

	private final int rawAngle;
	
	public static final int MINIMUM_ANGLE_INT = 0;
	public static final int MAXIMUM_ANGLE_INT = 1023;
	
	public static final double MINIMUM_ANGLE_DEGREES = 0.;
	public static final double MAXIMUM_ANGLE_DEGREES = 300.;
	
	private static DecimalFormat df;
	static {
		df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
	}
	
	/**
	 * 
	 * @param rawAngle
	 */
	private AX12Position(int rawAngle) {
		this.rawAngle = rawAngle;
	}
	
	/**
	 * Build an AX12 angle from a degree value
	 * 
	 * @param angle
	 * @throws IllegalArgumentException if value < MINIMUM_ANGLE_DEGREES or value > MAXIMUM_ANGLE_DEGREES
	 * @return
	 */
	public static AX12Position buildFromDegrees(double angle) {
		if (angle < MINIMUM_ANGLE_DEGREES && angle > MAXIMUM_ANGLE_DEGREES) {
			throw new IllegalArgumentException("Angle value is not in the allowed range ["+MINIMUM_ANGLE_DEGREES+";"+MAXIMUM_ANGLE_DEGREES+"]");
		}
		
		int val = (int) Math.round((angle/MAXIMUM_ANGLE_DEGREES)*(double)MAXIMUM_ANGLE_INT);

		return new AX12Position(val);
	}
	
	/**
	 * Build an AX12 angle from a raw value
	 * 
	 * @param angle
	 * @throws IllegalArgumentException if value < MINIMUM_ANGLE_INT or value > MAXIMUM_ANGLE_INT
	 * @return
	 */
	public static AX12Position buildFromInt(int angle) {
		if (angle < MINIMUM_ANGLE_INT || angle > MAXIMUM_ANGLE_INT) {
			throw new IllegalArgumentException("Angle value is not in the allowed range ["+MINIMUM_ANGLE_INT+";"+MAXIMUM_ANGLE_INT+"]");
		}
		
		return new AX12Position(angle);
	}
	
	/**
	 * Convert the raw angle to degrees and returns the value
	 * 
	 * @return
	 */
	public double getAngleAsDegrees() {
		return MAXIMUM_ANGLE_DEGREES * (rawAngle / (double) MAXIMUM_ANGLE_INT);
	}
	
	/**
	 * Get the raw angle
	 * 
	 * @return
	 */
	public int getRawAngle() {
		return rawAngle;
	}

	@Override
	public String getValueAsString() {
		return df.format(getAngleAsDegrees()) + "�";
	}
	
}
