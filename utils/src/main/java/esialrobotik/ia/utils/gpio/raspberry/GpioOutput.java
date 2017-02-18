package esialrobotik.ia.utils.gpio.raspberry;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.impl.PinImpl;

import java.util.EnumSet;

/**
 * Raspberry GPIO Output wrapper
 *
 * Crée un GPIO en sortie
 */
public class GpioOutput extends Gpio {

    /**
     * Constructeur
     * @param gpioNumber Numéro du GPIO (gpio, pas numéro de la pin : <a href="http://pi4j.com/pins/model-3b-rev1.html">Mapping des pins</a>
     * @param initialLow Etat initiale de la pin, true pour bas, false pour haut
     */
    public GpioOutput(int gpioNumber, boolean initialLow) {
        final GpioController gpio = GpioFactory.getInstance();
        PinImpl pin = new PinImpl("RaspberryPi GPIO Provider", gpioNumber, "GPIO"+gpioNumber, EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT), PinPullResistance.all());
        gpioPinDigital = gpio.provisionDigitalOutputPin(pin, initialLow ? PinState.LOW : PinState.HIGH);
    }

    /**
     * Met la pin à haut
     */
    public void setHigh() {
        ((GpioPinDigitalOutput)gpioPinDigital).high();
    }

    /**
     * Met la pin à bas
     */
    public void setLow() {
        ((GpioPinDigitalOutput)gpioPinDigital).low();
    }

}
