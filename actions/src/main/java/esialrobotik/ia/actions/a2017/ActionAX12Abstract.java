package esialrobotik.ia.actions.a2017;

import esialrobotik.ia.actions.ActionExecutor;
import esialrobotik.ia.utils.communication.raspberry.Serial;

/**
 * Created by franc on 20/05/2017.
 */
public abstract class ActionAX12Abstract implements ActionExecutor, AX12Link {

    private Serial serialAX12;
    private AX12 a12;
    protected boolean fini = false;
    
	protected enum ETAT {
		BRAS_LEVE(3, 60),
		BRAS_DEPOSER(3, 140),
		BRAS_BAISSE(3, 150),
		POIGNET_VERTICAL(2, 196),
		POIGNET_HORIZONTAL(2, 106),
		MAIN_FERMEE(1, 0),
		MAIN_DEPOSE(1, 20),
		MAIN_GRANDE_OUVERTE(1, 80),
		MINERAI_LARGUER(4, 237.1),
		MINERAI_RAMASSER(4, 245.1/*241.1*/),
		MINERAI_RENTRER(4, 154.5);
		
		public final int addr;
		public final double angle;
		ETAT(int addr, double angle) {
			this.addr = addr;
			this.angle = angle;
		}
	}

    public ActionExecutor init(Serial serialAX12) {
        this.serialAX12 = serialAX12;
        a12 = new AX12(1, this);
        return this;
    }
    
    @Override
    public void execute() {
		fini = false;
    	this.childExecution();
    	attend(500);
    	fini = true;
    }
    
    @Override
    public boolean finished() {
        return fini;
    }

	@Override
	public void sendCommandWithoutFeedBack(byte[] cmd, int baudRate) throws AX12LinkException {
		if (serialAX12 != null) {
			serialAX12.write(cmd);
		}
	}

	@Override
	public int getBaudRate() {
		return 115200;
	}

	@Override
	public void setBaudRate(int baudRate) throws AX12LinkException {
		// Nothing
	}
	
	/**
	 * Applique l'état demandé
	 * @param et
	 */
	protected void go(ETAT et) {
		if (a12 == null) {
			return;
		}
		a12.setAddress(et.addr);
		try {
			a12.setServoPositionInDegrees(et.angle);
		} catch (AX12LinkException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Attend une certaine durée en ms
	 * @param duree tps à attendre en ms
	 */
	protected void attend(long duree) {
		try {
			Thread.sleep(duree);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
    
    protected abstract void childExecution();

	@Override
	public void resetActionState() {
		this.fini = false;
	}

}
