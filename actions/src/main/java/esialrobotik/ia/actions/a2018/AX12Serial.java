package esialrobotik.ia.actions.a2018;

import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import esialrobotik.ia.utils.communication.raspberry.Serial;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by franc on 07/05/2018.
 */
public class AX12Serial implements SerialDataEventListener {

    private final List<Byte> bytes;
    private final Semaphore sem;
    private final OutputStream os;
    private final Serial serial;

    private int timeout = 100;

    public AX12Serial(Serial ax12Serial) {
        this.serial = ax12Serial;
        bytes = new ArrayList<>();
        ax12Serial.addReaderListeners(this);
        os = ax12Serial.getOutputStream();
        sem = new Semaphore(0);
    }

    public void setDefaultReadTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setDTR(boolean dtr) {
        serial.setDTR(dtr);
    }

    public int read() {
        return read(timeout);
    }

    public int read(int timeout) {
        try {
            if (sem.tryAcquire(timeout, TimeUnit.MILLISECONDS)) {
                synchronized (bytes) {
                    return AX12.unsignedByteToInt(bytes.remove(0));
                }
            } else {
                return -1;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void write(byte[] b) throws IOException {
        this.os.write(b);
    }

    public void write(byte b) throws IOException {
        this.os.write(b);
    }

    public void flush() throws IOException {
        this.os.flush();
    }

    @Override
    public void dataReceived(SerialDataEvent event) {
        synchronized (bytes) {
            try {
                for (byte b : event.getBytes()) {
                    bytes.add(b);
                    sem.release();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Allume ou éteint le lanceur de balles
     * @param enable
     * @throws AX12LinkException
     */
	public void enableLanceur(boolean enable) throws AX12LinkException {
		try {
			String cmd = enable ? "1111111111" : "0000000000";
			int count = 11;
			os.write(cmd.getBytes());	
			os.flush();
			
		} catch (IOException e1) {
			throw new AX12LinkException("Erreur de transmission de la commande du lanceur", e1);
		}
	}
}
