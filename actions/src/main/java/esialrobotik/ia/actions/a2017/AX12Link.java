package esialrobotik.ia.actions.a2017;

public interface AX12Link {

	/**
	 * Envoie une commande sur le canal
	 * @param cmd la séquence de byte de la commande
	 * @param baudRate change le baudrate le temps de la transmission
	 */
	public void sendCommandWithoutFeedBack(byte[] cmd, int baudRate) throws AX12LinkException;
	
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
	
}
