package esialrobotik.ia.actions.a2017;

import java.util.BitSet;

public class AX12 {
	
	public static final int AX12_ADDRESS_MIN = 0;
	public static final int AX12_ADDRESS_MAX = 253;
	public static final int AX12_ADDRESS_BROADCAST = 254;
	public static final double AX12_MAX_ANGLE_DEGRES = 300;
	public static final double AX12_MIN_ANGLE_DEGRES = 0;
	
	// AX12 instructions
	public enum AX12_Instr {
	  AX12_INSTR_PING(0x01),
	  AX12_INSTR_READ_DATA(0x02),
	  AX12_INSTR_WRITE_DATA(0x03),
	  AX12_INSTR_RESET(0x06);
	  
	  public byte instr;
	  private AX12_Instr(int instr) {
		  this.instr = AX12.intToRawByte(instr);
	  }
	};
	
	// AX12 registers
	public enum AX12_Register {
	  AX12_ROM_MODEL_NUMBER(0x00),
	  AX12_ROM_FIRMWARE_VERSION(0x02),
	  AX12_ROM_ID(0x03),
	  AX12_ROM_BAUD_RATE(0x04),
	  AX12_ROM_RETURN_DELAY_TIME(0x05),
	  AX12_ROM_CW_ANGLE_LIMIT(0x06),
	  AX12_ROM_CCW_ANGLE_LIMIT(0x08),
	  AX12_ROM_HIGH_TEMP_LIMIT(0x0B),
	  AX12_ROM_LOW_VOLTAGE_LIMIT(0x0C),
	  AX12_ROM_HIGH_VOLTAGE_LIMIT(0x0D),
	  AX12_ROM_MAX_TORQUE(0x0E),
	  AX12_ROM_STATUS_RETURN_LEVEL(0x10),
	  AX12_ROM_ALARM_LED(0x11),
	  AX12_ROM_ALARM_SHUTDOWN(0x12),
	  AX12_ROM_DOWN_CALIBRATION(0x14),
	  AX12_ROM_UP_CALIBRATION(0x16),
	  AX12_RAM_TORQUE_ENABLE(0x18),
	  AX12_RAM_LED(0x19),
	  AX12_RAM_CW_COMPILANCE_MARGIN(0x1A),
	  AX12_RAM_CCW_COMPILANCE_MARGIN(0x1B),
	  AX12_RAM_CW_COMPILANCE_SLOPE(0x1C),
	  AX12_RAM_CCW_COMPILANCE_SLOPE(0x1D),
	  AX12_RAM_GOAL_POSITION(0x1E),
	  AX12_RAM_MOVING_SPEED(0x20),
	  AX12_RAM_TORQUE_LIMIT(0x22),
	  AX12_RAM_PRESENT_POSITION(0x24),
	  AX12_RAM_PRESENT_SPEED(0x26),
	  AX12_RAM_PRESENT_LOAD(0x28),
	  AX12_RAM_PRESENT_VOLTAGE(0x2A),
	  AX12_RAM_PRESENT_TEMPERATURE(0x2B),
	  AX12_RAM_REGISTERED_INSTRUCTION(0x2C),
	  AX12_RAM_MOVING(0x2E),
	  AX12_RAM_LOCK(0x2F),
	  AX12_RAM_PUNCH(0x30);
	  
	  public byte regi;
	  private AX12_Register(int regi) {
		  this.regi = AX12.intToRawByte(regi);;
	  }
	};
	
	// AX12 register values for UART SPEED
	public enum AX12_UART_SPEEDS {
		SPEED_1000000(1, 1000000),
		SPEED_500000(3, 500000),
		SPEED_4000000(4, 400000),
		SPEED_250000(7, 250000),
		SPEED_200000(9, 200000),
		SPEED_115200(16, 115200),
		SPEED_57600(34, 57600),
		SPEED_19200(103, 19200),
		SPEED_9600(207, 9600);
		
		public final byte byteVal;
		public final int intVal;
		
		private AX12_UART_SPEEDS(int byteVal, int intVal) {
			this.intVal = intVal;
			this.byteVal = AX12.intToRawByte(byteVal);
		}
		
		public static AX12_UART_SPEEDS fromValue(int speed) {
			for (AX12_UART_SPEEDS s : AX12_UART_SPEEDS.values()) {
				if (s.intVal == speed) {
					return s;
				}
			}
			return null;
		}
		
		@Override
		public String toString() {
			return this.intVal+" bps";
		}
	}
	
	
	private AX12Link ac;
	private int baudRate;
	private byte addr;
	private AX12Link alink;

	public AX12(int addr, AX12_UART_SPEEDS speed, AX12Link alink) {
		if (speed == null) {
			throw new IllegalArgumentException("La vitesse de l'UARTY n'est pas valide");
		}
		this.addr = AX12.intToRawByte(addr);
		this.baudRate = speed.intVal;
		this.alink = alink;
	}
	
	public AX12(int addr, AX12Link alink) {
		this(addr, AX12_UART_SPEEDS.fromValue(alink.getBaudRate()), alink);
	}
	
	public AX12Link getAX12Communicator() {
		return this.ac;
	}
	
	/**
	 * Change l'adresse de l'AX12
	 * @param adresse
	 */
	public void setAddress(int adresse) {
		checkAddressRange(adresse);
		this.addr = AX12.intToRawByte(adresse);
	}
	
	public int getAddress() {
		return this.addr;
	}
	
	protected byte[] buildInstruction(byte instruction, byte[] params) {
		  // Instruction packet : FF FF <ID> <LEN> <INSTR> <PAR0>..<PARN> <CKSUM>
		  // <CKSUM> = ( ~(<ID> + <LEN> + <INSTR> + <PAR0> + .. + <PARN>)~) % 0xFF
		  byte buffer[] = new byte[params.length + 6];
		  int i;
		  int pos = 0;
		  int checksum = 0;
		  buffer[pos++] = AX12.intToRawByte(0xFF);
		  buffer[pos++] = AX12.intToRawByte(0xFF);
		  checksum += (buffer[pos++] = this.addr);
		  checksum += (buffer[pos++] = AX12.intToRawByte(params.length+2));
		  checksum += (buffer[pos++] = instruction);

		  for(i=0; i<params.length; i++){
		    buffer[pos++] = params[i];
		    checksum += params[i];
		  }
		  checksum = (~checksum) & 0xFF;
		  buffer[pos++] = AX12.intToRawByte(checksum);

		  return buffer;
	}
	
	/**
	 * 
	 * @param val
	 * @return
	 * @throws IllegalArgumentException Si la valeur donnée n'est pas comprise entre 0 et 255
	 */
	public static byte intToRawByte(int val) throws IllegalArgumentException {
		if (val < 0 || val > 255) {
			throw new IllegalArgumentException("La valeur doit être comprise entre 0 et 255. Reçu : "+val);
		}
		
		if(val == 0) {
			return 0;
		}
		BitSet bs = new BitSet(8);
		int vals[] = new int[]{1, 2, 4, 8, 16, 32, 64, 128};
		
		for(int i=vals.length-1; i>=0; i--) {
			if (val >= vals[i]) {
				val -= vals[i];
				bs.set(i, true);
			} else {
				bs.set(i, false);
			}
		}
		
		return bs.toByteArray()[0];
	}
	
	/**
	 * Met le servo moteur en mode rotation continue
	 * @throws AX12LinkException 
	 */
	public void setWheelMode() throws AX12LinkException {
		byte data[] = new byte[]{AX12_Register.AX12_RAM_CCW_COMPILANCE_MARGIN.regi, AX12.intToRawByte(0), AX12.intToRawByte(0)};
		executeInstruction(buildInstruction(AX12_Instr.AX12_INSTR_WRITE_DATA.instr, data));
		data[0] = AX12_Register.AX12_ROM_CCW_ANGLE_LIMIT.regi;
		executeInstruction(buildInstruction(AX12_Instr.AX12_INSTR_WRITE_DATA.instr, data));
	}

	/**
	 * Met le servo en mode angle
	 * @throws AX12LinkException  
	 */
	public void setPositionMode() throws AX12LinkException {
		byte data[] = new byte[]{AX12_Register.AX12_ROM_CW_ANGLE_LIMIT.regi, AX12.intToRawByte(1), AX12.intToRawByte(0)};
		executeInstruction(buildInstruction(AX12_Instr.AX12_INSTR_WRITE_DATA.instr, data));
		data[0] = AX12_Register.AX12_ROM_CCW_ANGLE_LIMIT.regi;
		data[1]= AX12.intToRawByte(0xFF);
		data[2]= AX12.intToRawByte(0x03);
		executeInstruction(buildInstruction(AX12_Instr.AX12_INSTR_WRITE_DATA.instr, data));
	}

	/**
	 * 
	 * @param spd
	 * @throws AX12LinkException 
	 */
	public void setServoSpeed(int spd) throws AX12LinkException {
		byte data[] = new byte[]{AX12_Register.AX12_RAM_MOVING_SPEED.regi, AX12.intToRawByte(spd & 0xFF), AX12.intToRawByte((spd >> 8) & 0xFF)};
		executeInstruction(buildInstruction(AX12_Instr.AX12_INSTR_WRITE_DATA.instr, data));
	}

	public void setServoPositionInDegrees(double pos) throws AX12LinkException {
		if (pos < AX12_MIN_ANGLE_DEGRES || pos > AX12_MAX_ANGLE_DEGRES) {
			throw new IllegalArgumentException("L'angle de l'AX12 est en dehors de la plage ["+AX12_MIN_ANGLE_DEGRES+" ~ "+AX12_MAX_ANGLE_DEGRES+"]. Obtenu : "+pos);
		}
		int val = (int) Math.round((pos/AX12_MAX_ANGLE_DEGRES)*1024.);
		if(val > 1023)
			val = 1023;
		byte data[] = new byte[]{AX12_Register.AX12_RAM_GOAL_POSITION.regi, AX12.intToRawByte(val & 0xFF), AX12.intToRawByte((val >> 8) & 0xFF)};
		executeInstruction(buildInstruction(AX12_Instr.AX12_INSTR_WRITE_DATA.instr, data));
	}
	
	public void setLed(boolean on) throws AX12LinkException {
		byte data[] = new byte[]{AX12_Register.AX12_RAM_LED.regi, AX12.intToRawByte(on ? 1 : 0)};
		executeInstruction(buildInstruction(AX12_Instr.AX12_INSTR_WRITE_DATA.instr, data));
	}
	
	public void writeUartSpeed(AX12_UART_SPEEDS speed) throws AX12LinkException {
		byte data[] = new byte[]{AX12_Register.AX12_ROM_BAUD_RATE.regi, speed.byteVal};
		executeInstruction(buildInstruction(AX12_Instr.AX12_INSTR_WRITE_DATA.instr, data));
	}
	
	/**
	 * Met à jour l'ID de l'AX2 sur sa ROM et bascule sur la nouvelle adresse
	 * @param address
	 * @throws AX12LinkException
	 */
	public void writeAddress(int address) throws AX12LinkException {
		checkAddressRange(address);
		byte data[] = new byte[]{AX12_Register.AX12_ROM_ID.regi, AX12.intToRawByte(address)};
		executeInstruction(buildInstruction(AX12_Instr.AX12_INSTR_WRITE_DATA.instr, data));
		this.addr = AX12.intToRawByte(address);
	}
	
	public static String byteToString(byte b) {
		if (b == 0) {
			return "00000000";
		}
		BitSet bs = BitSet.valueOf(new byte[]{b});
		StringBuffer sb = new StringBuffer();
		for (int i=7; i>=0; i--) {
			sb.append(bs.get(i) ? '1' : '0');
		}
		return sb.toString();
	}
	
	/**
	 * Lève une exception si l'adresse de l'AX12 n'est pas valide
	 * @param address
	 * @throws IllegalArgumentException
	 */
	public static void checkAddressRange(int address) throws IllegalArgumentException {
		if (address == AX12_ADDRESS_BROADCAST) {
			return;
		}
		if (address < AX12_ADDRESS_MIN || address > AX12_ADDRESS_MAX) {
			throw new IllegalArgumentException("L'adresse de l'AX12 doit être contenue dans la plage ["+AX12_ADDRESS_MIN+" ~ "+AX12_ADDRESS_MAX+"] Ou correspondre à l'adresse de BroadCast "+AX12_ADDRESS_BROADCAST+". Obtenu : "+address);
		}
	}
	
	private static void checkBaudRateRange(int baudRate) {
		boolean flag = false;
		for (AX12_UART_SPEEDS speed : AX12_UART_SPEEDS.values()) {
			if (baudRate == speed.intVal) {
				flag = true;
				break;
			}
		}
		
		if (!flag) {
			StringBuffer sb = new StringBuffer();
			for (AX12_UART_SPEEDS speed : AX12_UART_SPEEDS.values()) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(speed.intVal);
			}
			throw new IllegalArgumentException("BaudRate "+baudRate+" invalide. BaudRates valides : ["+sb.toString()+"]");
		}
	}
	
	public void setBaudRateRaw(int baudRate) {
		this.baudRate = baudRate;
	}
	
	private void executeInstruction(byte[] instruction) throws AX12LinkException {
		alink.sendCommandWithoutFeedBack(instruction, this.baudRate);
	}
	
	public void setAx12Link(AX12Link alink) {
		this.alink = alink;
	}
}
