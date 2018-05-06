package esialrobotik.ia.actions.a2018;

public interface AX12Link {

	/**
	 * Envoie une commande et retourne l'éventuelle réponse détectée
	 * @param cmd la séquence de byte de la commande
	 * @param baudRate change le baudrate le temps de la transmition si cette opération est supportée
	 */
	public byte[] sendCommand(byte[] cmd, int baudRate) throws AX12LinkException;
	
	/**
	 * Renvoie le bauudRate utilisé sur la liaison actuelle
	 * @return
	 */
	public int getBaudRate();
	
	/**
	 * Règle le baudrate de communication actuel
	 * @param baudRate
	 * @throws AX12LinkException
	 */
	public void setBaudRate(int baudRate) throws AX12LinkException;
	
	/**
	 * Active ou désactuve la PIN DTR de l'UART
	 * @param acitvate
	 */
	public void enableDTR(boolean acitvate);
	
}
