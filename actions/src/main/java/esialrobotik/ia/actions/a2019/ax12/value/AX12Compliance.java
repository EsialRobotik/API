package esialrobotik.ia.actions.a2019.ax12.value;

public class AX12Compliance extends Ax12Value {
	
	protected int rawValue;
	protected int friendlyValue;
	
	protected static final int[] seuils = new int[] {3, 7, 15, 31, 63, 127, 254};
	
	private AX12Compliance(int friendlyValue) {
		this.rawValue = seuils[friendlyValue - 1];
		this.friendlyValue = friendlyValue;
	}
	
	public static AX12Compliance fromFriendlyValue(int i) {
		if (i < 1 || i > seuils.length) {
			throw new IllegalArgumentException("Compliance must be set between 1 and " + seuils.length +" included, given : " + i);
		}
		
		return new AX12Compliance(i);
	}
	
	/**
	 * G�n�re une consigne � partir d'une valeur brute
	 * @param raw
	 * @return
	 */
	public static AX12Compliance fromRaw(int raw) {
		if (raw <0 || raw > 254) {
			throw new IllegalArgumentException("Raw must be set between 0 and 254 included");
		}
		
		for (int i=0; i<seuils.length; i++) {
			if (raw < seuils[i]) {
				return new AX12Compliance(i + 1);
			}
		}
		
		return new AX12Compliance(seuils.length);
	}
	
	@Override
	public String getValueAsString() {
		return this.friendlyValue + "/" + seuils.length;
	}
	
	public int getRawValue() {
		return this.rawValue;
	}
	
	public int getFriendlyValue() {
		return this.friendlyValue;
	}

}
