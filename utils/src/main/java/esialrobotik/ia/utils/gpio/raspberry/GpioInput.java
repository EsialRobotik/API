package esialrobotik.ia.utils.gpio.raspberry;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.gpio.impl.PinImpl;

import java.util.EnumSet;

/**
 * Raspberry GPIO Input wrapper
 *
 * Crée un GPIO en entrée
 */
public class GpioInput extends Gpio {

    /**
     * Constructeur
     * @param gpioNumber Numéro du GPIO (gpio, pas numéro de la pin : <a href="http://pi4j.com/pins/model-3b-rev1.html">Mapping des pins</a>
     * @param pullUp true pour un GPIO en pull up, false pour du pull in
     */
    public GpioInput(int gpioNumber, boolean pullUp) {
        final GpioController gpio = GpioFactory.getInstance();
        PinImpl pin = new PinImpl("RaspberryPi GPIO Provider", gpioNumber, "GPIO"+gpioNumber, EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT), PinPullResistance.all());
        gpioPinDigital = gpio.provisionDigitalInputPin(pin, pullUp ? PinPullResistance.PULL_UP : PinPullResistance.PULL_DOWN);
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

}
