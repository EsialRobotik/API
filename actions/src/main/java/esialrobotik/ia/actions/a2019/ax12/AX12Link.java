package esialrobotik.ia.actions.a2019.ax12;

public interface AX12Link {

	/**
	 * Envoie une commande et retourne l'�ventuelle r�ponse d�tect�e
	 * @param cmd la s�quence de byte de la commande
	 * @param baudRate change le baudrate le temps de la transmition si cette op�ration est support�e
	 */
	public byte[] sendCommand(byte[] cmd, int baudRate) throws AX12LinkException;
	
	/**
	 * Renvoie le baudRate utilis� sur la liaison actuelle
	 * @return
	 */
	public int getBaudRate();
	
	/**
	 * R�gle le baudrate de communication actuel
	 * @param baudRate
	 * @throws AX12LinkException
	 */
	public void setBaudRate(int baudRate) throws AX12LinkException;
	
	/**
	 * Met � un niveau logique haut ou bas la ligne DTR de la liaison s�rie
	 * @param enable true = ligne � LOW, false = ligne � HIGH
	 * @throws AX12LinkException 
	 */
	public void enableDtr(boolean enable) throws AX12LinkException;
	
	/**
	 * Met � un niveau logique haut ou bas la ligne RTS de la liaison s�rie
	 * @param enable true = ligne � LOW, false = ligne � HIGH
	 * @throws AX12LinkException
	 */
	public void enableRts(boolean enable) throws AX12LinkException;
	
}
