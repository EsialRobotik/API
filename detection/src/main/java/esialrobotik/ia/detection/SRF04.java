package esialrobotik.ia.detection;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import esialrobotik.ia.utils.gpio.raspberry.GpioInput;
import esialrobotik.ia.utils.gpio.raspberry.GpioOutput;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by franc on 18/05/2017.
 */
public class SRF04 implements UltraSoundInterface {

    private GpioInput gpioInput;
    private GpioOutput gpioOutput;

    public SRF04(int gpioInput, int gpioOutput){
        this.gpioInput = new GpioInput(gpioInput, false);
        this.gpioOutput = new GpioOutput(gpioOutput, false);
        this.init();
    }

    @Inject
    public SRF04(@Assisted DetectionModuleConfiguration.GPioPair pair) {
        this.gpioInput = new GpioInput(pair.gpio_in, false);
        this.gpioOutput = new GpioOutput(pair.gpio_out, false);
        this.init();
    }

    public void init() {

    }

    /**
     * WARNING !!! Attender 12ms entre 2 mesures, même sur des capteurs différents pour ne pas capter des echos foireux
     * @return mesure du télémètre en mm
     */
    public long getMeasure() {
        /*
         * Pour faire la mesure, il faut:
         * - envoyer une impulsion d'au moins 10 us sur la GPIO out,
         * - attendre la réponse sur la GPIO in
         * Le capteur répond en mettant à 1 le GPIO pendant une certaine
         * durée, c'est cette durée qui donne la mesure de distance.
         * C'est la mesure brute, en nanosecondes. D'après la doc, il faut diviser
         * par 5800 pour avoir une valeur en mm.
         */

        this.gpioOutput.setHigh();
        LockSupport.parkNanos(10000);
        final long[] time = new long[2];
        Thread t = new Thread(new Runnable() {
            public void run() {
                while (gpioInput.isLow());
                time[0] = System.nanoTime();
                while (gpioInput.isHigh());
                time[1] = System.nanoTime();
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return (time[1] - time[0]) / 5800;
    }
}
