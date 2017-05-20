package esialrobotik.ia.utils.gpio.raspberry;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.impl.PinImpl;
import com.pi4j.util.CommandArgumentParser;

import java.util.EnumSet;

/**
 * Raspberry GPIO Output wrapper
 *
 * Crée un GPIO en sortie
 */
public class GpioOutput extends Gpio {

    /**
     * Constructeur
     * @param gpioPin Numéro du GPIO (gpio, pas numéro de la pin : <a href="http://pi4j.com/pins/model-3b-rev1.html">Mapping des pins</a>
     * @param initialLow Etat initiale de la pin, true pour bas, false pour haut
     */
    public GpioOutput(Pin gpioPin, boolean initialLow) {
        final GpioController gpio = GpioFactory.getInstance();
        Pin pin = CommandArgumentParser.getPin(
                RaspiPin.class,    // pin provider class to obtain pin instance from
                gpioPin  // default pin if no pin argument found
        );
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
