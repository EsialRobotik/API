package esialrobotik.ia.detection.ultrasound.srf04.raspberry;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import esialrobotik.ia.detection.DetectionModuleConfiguration;
import esialrobotik.ia.detection.ultrasound.UltraSoundInterface;
import esialrobotik.ia.utils.gpio.raspberry.GpioInput;
import esialrobotik.ia.utils.gpio.raspberry.GpioOutput;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by franc on 18/05/2017.
 * Télémètre ultrason SRF04 analogique
 * @see <a href="https://www.robot-electronics.co.uk/htm/srf04tech.htm">Documentation</a>
 */
public class SRF04 implements UltraSoundInterface {

    private static long TIMEOUT = 300; // If no change after 30ms, it's a timeout

    private GpioInput gpioInput;
    private GpioOutput gpioOutput;

    public SRF04(int gpioInput, int gpioOutput){
        this.gpioInput = new GpioInput(gpioInput, false);
        this.gpioOutput = new GpioOutput(gpioOutput, true);
        this.init();
    }

    @Inject
    public SRF04(@Assisted DetectionModuleConfiguration.GPioPair pair) {
        this.gpioInput = new GpioInput(pair.gpio_in, false);
        this.gpioOutput = new GpioOutput(pair.gpio_out, true);
        this.init();
    }

    public void init() {
        this.gpioOutput.setLow();
    }

    /**
     * WARNING !!! Attendre 12ms entre 2 mesures, même sur des capteurs différents pour ne pas capter des echos foireux
     * @return mesure du télémètre en mm
     */
    public long getMeasure() {
        /*
         * Pour faire la mesure, il faut:
         * - envoyer une impulsion d'au moins 10 us sur la GPIO out,
         * - attendre la réponse sur la GPIO in
         * Le capteur répond en mettant à 1 le GPIO pendant une certaine durée, c'est cette durée qui donne la mesure de distance.
         * C'est la mesure brute, en nanosecondes. D'après la doc, il faut diviser par 5800 pour avoir une valeur en mm.
         */

        this.gpioOutput.setHigh();
        LockSupport.parkNanos(10000);
        this.gpioOutput.setLow();
        final long[] time = new long[2];

        long checkoutTimeout = System.currentTimeMillis();
        while (gpioInput.isLow()){
            if (System.currentTimeMillis() - checkoutTimeout > TIMEOUT) {
                return 10000;
            }
        }
        time[0] = System.nanoTime();
        checkoutTimeout = System.currentTimeMillis();
        while (gpioInput.isHigh()) {
            if (System.currentTimeMillis() - checkoutTimeout > TIMEOUT) {
                return 20000;
            }
        }
        time[1] = System.nanoTime();
        return (time[1] - time[0]) / 5800;
    }

    public static void main(String args[]) throws InterruptedException {
        SRF04 srf04AvantDroit = new SRF04(0, 2); // Avant droit
        SRF04 srf04AvantMilieu = new SRF04(12, 13); // Avant milieu
        SRF04 srf04AvantGauche = new SRF04(21, 22); // Avant gauche
        SRF04 srf04Arriere = new SRF04(24, 25); // Arriere

        long measureAvantDroit, measureAvantMilieu, measureAvantGauche, measureArriere;
        while (true) {
            measureAvantDroit = srf04AvantDroit.getMeasure();
            Thread.sleep(12);
            measureAvantMilieu = srf04AvantMilieu.getMeasure();
            Thread.sleep(12);
            measureAvantGauche = srf04AvantGauche.getMeasure();
            Thread.sleep(12);
            measureArriere = srf04Arriere.getMeasure();
            System.out.println("measureAvantDroit=" + measureAvantDroit + "  measureAvantMilieu=" + measureAvantMilieu + "  measureAvantGauche=" + measureAvantGauche + "  measureArriere=" + measureArriere);
            Thread.sleep(12);
        }

//        GpioOutput out0 = new GpioOutput(0, true);
//        GpioOutput out2 = new GpioOutput(2, true);
//        GpioOutput out12 = new GpioOutput(12, true);
//        GpioOutput out13 = new GpioOutput(13, true);
//        GpioOutput out21 = new GpioOutput(21, true);
//        GpioOutput out22 = new GpioOutput(22, true);
//        GpioOutput out24 = new GpioOutput(24, true);
//        GpioOutput out25 = new GpioOutput(25, true);
//
//        out0.setLow();
//        out2.setLow();
//        out12.setLow();
//        out13.setLow();
//        out21.setLow();
//        out22.setLow();
//        out24.setLow();
//        out25.setLow();
//
//        while (true) {
//            Thread.sleep(200);
//        }
    }

}
