package esialrobotik.ia.utils.gpio.raspberry;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.impl.PinImpl;
import com.pi4j.util.CommandArgumentParser;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Raspberry GPIO Input wrapper
 *
 * Crée un GPIO en entrée
 */
public class GpioInput extends Gpio {

    /**
     * Constructeur
     * @param gpioPin Numéro du GPIO (gpio, pas numéro de la pin : <a href="http://pi4j.com/pins/model-3b-rev1.html">Mapping des pins</a>
     * @param pullUp true pour un GPIO en pull up, false pour du pull in
     */
    public GpioInput(int gpioPin, boolean pullUp) {
        final GpioController gpio = GpioFactory.getInstance();

        Pin pin = new PinImpl(RaspiGpioProvider.NAME,
                gpioPin,
                "GPIO " + gpioPin,
                EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT, PinMode.SOFT_PWM_OUTPUT),
                PinPullResistance.all(),
                EnumSet.allOf(PinEdge.class));
        PinPullResistance pull = CommandArgumentParser.getPinPullResistance(
                pullUp ? PinPullResistance.PULL_UP : PinPullResistance.PULL_DOWN  // default pin pull resistance if no pull argument found
        );
        gpioPinDigital =  gpio.provisionDigitalInputPin(pin, pull);
    }

    /**
     * Ajoute des listeners
     * @param listeners GPIO listeners
     */
    public void addListener(GpioPinListenerDigital... listeners) {
        ((GpioPinDigitalInput)gpioPinDigital).addListener(listeners);
    }

    /**
     * Supprime des listeners
     * @param listeners GPIO listeners
     */
    public void removeListener(GpioPinListenerDigital... listeners) {
        ((GpioPinDigitalInput)gpioPinDigital).removeListener(listeners);
    }

    /**
     * Supprime tous les listeners
     */
    public void removeAllListener() {
        ((GpioPinDigitalInput)gpioPinDigital).removeAllListeners();
    }

    // On ajoute les triggers ou pas ? c'est des events liés à l'état d'une autre pin

    public static void main(String args[]) throws InterruptedException {
        GpioInput input = new GpioInput(4, false); // Tirette
        GpioInput input2 = new GpioInput(5, false); // Capteur couleur
        while (true) {
            System.out.println((input.isHigh() ? "HIGH" : "-") + "##" + (input.isLow() ? "LOW" : "-")
                + "   " + (input2.isHigh() ? "HIGH" : "-") + "##" + (input2.isLow() ? "LOW" : "-"));
            Thread.sleep(1000);
        }
    }

}
