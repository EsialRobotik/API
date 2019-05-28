package esialrobotik.ia.actions.a2019.ax12;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import gnu.io.*;

public class AX12LinkSerial implements AX12Link {
	
	private SerialPort sp;
	private OutputStream os;
	private InputStream is;
	private boolean combinedRxTx;
	
	protected ArrayList<Byte> lecture;
	
	public AX12LinkSerial(SerialPort sp, int baudRate) throws AX12LinkException {
		this(sp, baudRate, null);
	}
	
	
	public AX12LinkSerial(SerialPort sp, int baudRate, Boolean combinedRxTx) throws AX12LinkException {
		this.sp = sp;
		this.lecture = new ArrayList<Byte>();
		this.combinedRxTx = combinedRxTx == null ? false : combinedRxTx;
		try {
			this.sp.setDTR(false);
			this.sp.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			//this.sp.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_OUT);
			
			this.sp.setSerialPortParams(baudRate,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE );
			this.sp.enableReceiveTimeout(50);
		} catch (UnsupportedCommOperationException e1) {
			e1.printStackTrace();
		}
		this.setBaudRate(baudRate);
		try {
			this.is = sp.getInputStream();
			this.os = sp.getOutputStream();
		} catch (IOException e) {
			throw new AX12LinkException("Erreur de r�cup�ration des flux d'entr�es/sorties", e);
		}

	}

	@Override
	public int getBaudRate() {
		return sp.getBaudRate();
	}
	
	public String getUartName() {
		return sp.getName();
	}
	
	@Override
	public void setBaudRate(int baudRate) throws AX12LinkException {
		try {
			sp.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
			throw new AX12LinkException("Erreur de changement de  baudrate", e);
		}
	}

	@Override 
	public synchronized byte[] sendCommand(byte[] cmd, int baudRate) throws AX12LinkException {
		int oldBr = -1;
		byte[] response = null;
		if (sp.getBaudRate() != baudRate) {
			oldBr = sp.getBaudRate();
			this.setBaudRate(baudRate);
		}
		
		try {
			synchronized (this.os) {
				os.write(cmd);
				os.flush();
			
				// On retire du flux d'entr�e la commande qu'on vient juste d'envoyer si rx et tx sont combin�s
				if (this.combinedRxTx) {
					for (int i=0; i<cmd.length; i++) {
						if (is.read() == -1) {
							throw new AX12LinkException("Erreur de changement d'�chapement de la commande dans le flux d'entr�e");
						}
					}	
				}
				
				// On lit la r�ponse de l'AX12
				this.lecture.clear();
				int r;
				while ((r = is.read()) != -1) {
					lecture.add(AX12.intToUnsignedByte(r));
				}
			}
			
			response = new byte[lecture.size()];
			for (int i=lecture.size()-1; i>=0; i--) {
				response[i] = lecture.get(i);
			}
			
		} catch (IOException e1) {
			throw new AX12LinkException("Erreur de transmition de la commande", e1);
		} finally {
			if (oldBr != -1) {
				this.setBaudRate(oldBr);
			}
		}
		
		return response;
	}
	
	public static String[] getAvailableSerialPorts() {
		ArrayList<String> ports = new ArrayList<>();
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> p = CommPortIdentifier.getPortIdentifiers();
		CommPortIdentifier cpi;
		CommPort cp;
		
		while(p.hasMoreElements()){
			try {
				cpi = p.nextElement();
				if(cpi != null && !cpi.isCurrentlyOwned()){
					cp = cpi.open(AX12LinkSerial.class.getName(), 500);
					if(cp instanceof SerialPort){
						ports.add(cp.getName());
						cp.close();
					}
				}
			} catch (PortInUseException e) {
			}
		}
		
		return ports.toArray(new String[ports.size()]);
	}
	
	/**
	 * Retourne un port s�rie identifi� par son nom
	 * @param name si null, le premier port s�rie valide est retourn�
	 * @return null si le port s�rie identifi�e par son nom n'existe ou n'est pas disponible
	 */
	public static SerialPort getSerialPort(String name) {
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> p = CommPortIdentifier.getPortIdentifiers();
		CommPortIdentifier cpi;
		CommPort cp;
		
		while(p.hasMoreElements()){
			try {
				cpi = p.nextElement();
				System.out.println(cpi.getName());
				if(cpi != null && !cpi.isCurrentlyOwned() && (name == null || cpi.getName().equals(name))) {
					cp = cpi.open(AX12LinkSerial.class.getName(), 500);
					if(cp instanceof SerialPort){
						return (SerialPort) cp;
					}
				}
			} catch (PortInUseException e) {
			}
		}
		
		return null;
	}

	@Override
	public void enableDtr(boolean enable) {
		sp.setDTR(enable);
	}


	@Override
	public void enableRts(boolean enable) {
		sp.setRTS(enable);
	}


	@Override
	public void disableAx12AndShutdownLink() {
		synchronized (this.os) {
			AX12 ax = new AX12(AX12.AX12_ADDRESS_BROADCAST, this);
			try {
				ax.disableTorque();
				os.close();
			} catch (IOException | AX12LinkException | AX12Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
