package esialrobotik.ia.utils.lcd.raspberry;

import java.util.Scanner;

import com.google.inject.Inject;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import esialrobotik.ia.utils.communication.raspberry.I2C;
import esialrobotik.ia.utils.lcd.LCD;
import esialrobotik.ia.utils.log.LoggerFactory;

/**
 * LCD 2 lignes / 16 colonnes de Joy-It
 *
 * @see <a href="https://www.gotronic.fr/pj2-sbc-lcd16x2-fr-1441.pdf">Documentation</a>
 * @see <a href="https://github.com/CaptainStouf/raspberry_lcd4x20_I2C">Librairie Python d'origine</a>
 */
public class LCD_I2C implements LCD {

    private I2C i2cDevice;
    private Logger logger;

    // Default address and line counts
    private static final int DEFAULT_I2C_ADDRESS = 0x27;
    private static final int DEFAULT_LINE_COUNT = 2;
    private static final int DEFAULT_LINE_LENGTH = 16;

    // Commands
    private static final byte LCD_CLEARDISPLAY = 0x01;
    private static final byte LCD_RETURNHOME = 0x02;
    private static final byte LCD_ENTRYMODESET = 0x04;
    private static final byte LCD_DISPLAYCONTROL = 0x08;
    private static final byte LCD_CURSORSHIFT = 0x10;
    private static final byte LCD_FUNCTIONSET = 0x20;
    private static final byte LCD_SETCGRAMADDR = 0x40;

    // flags for display entry mode
    private static final byte LCD_ENTRYRIGHT = 0x00;
    private static final byte LCD_ENTRYLEFT = 0x02;
    private static final byte LCD_ENTRYSHIFTINCREMENT = 0x01;
    private static final byte LCD_ENTRYSHIFTDECREMENT = 0x00;

    // flags for display on/off control
    private static final byte LCD_DISPLAYON = 0x04;
    private static final byte LCD_DISPLAYOFF = 0x00;
    private static final byte LCD_CURSORON = 0x02;
    private static final byte LCD_CURSOROFF = 0x00;
    private static final byte LCD_BLINKON = 0x01;
    private static final byte LCD_BLINKOFF = 0x00;

    // flags for display/cursor shift
    private static final byte LCD_DISPLAYMOVE = 0x08;
    private static final byte LCD_CURSORMOVE = 0x00;
    private static final byte LCD_MOVERIGHT = 0x04;
    private static final byte LCD_MOVELEFT = 0x00;

    // flags for function set
    private static final byte LCD_8BITMODE = 0x10;
    private static final byte LCD_4BITMODE = 0x00;
    private static final byte LCD_2LINE = 0x08;
    private static final byte LCD_1LINE = 0x00;
    private static final byte LCD_5x10DOTS = 0x04;
    private static final byte LCD_5x8DOTS = 0x00;

    // flags for backlight control
    private static final byte LCD_BACKLIGHT = 0x08;
    private static final byte LCD_NOBACKLIGHT = 0x00;

    private static final byte En = 0b00000100; // Enable bit
    private static final byte Rw = 0b00000010; // Read/Write bit
    private static final byte Rs = 0b00000001; // Register select bit

    private static final int[] LINE_ADDR = {0x80, 0xC0, 0x94, 0xD4};

    // Lines to display
    private String[] lines;
    private int lineLength;

    @Inject
    public LCD_I2C() {
        this(DEFAULT_I2C_ADDRESS, DEFAULT_LINE_COUNT, DEFAULT_LINE_LENGTH);
    }

    public LCD_I2C(int i2cAddress, int lineCount, int lineLength) {

        if (lineCount < 1 || lineCount > 4) {
            throw new ArrayIndexOutOfBoundsException();
        }

        this.logger = LoggerFactory.getLogger(LCD_I2C.class);
        logger.info("Initializing " + lineLength + "x" + lineCount + " LCD "
                + "on I2C address " + String.format("0x%X", i2cAddress));
        this.lines = new String[lineCount];
        this.lineLength = lineLength;
        this.i2cDevice = new I2C(i2cAddress);

        i2cWrite(0x3);
        i2cWrite(0x3);
        i2cWrite(0x3);
        i2cWrite(0x2);

        i2cWrite(LCD_FUNCTIONSET | LCD_2LINE | LCD_5x8DOTS | LCD_4BITMODE);
        i2cWrite(LCD_DISPLAYCONTROL | LCD_DISPLAYON);
        i2cWrite(LCD_CLEARDISPLAY);
        i2cWrite(LCD_ENTRYMODESET | LCD_ENTRYLEFT);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
    }

    private void usleep(int micro) {
        try {
            Thread.sleep(0, micro * 1000);
        } catch (InterruptedException e) {
        }
    }

    private void strobe(int value) {
        i2cDevice.write((byte) (value | En | LCD_BACKLIGHT));
        usleep(500);
        i2cDevice.write((byte) ((value & ~En) | LCD_BACKLIGHT));
        usleep(100);
    }

    private void i2cWriteFourBits(int value) {
        i2cDevice.write((byte) (value | LCD_BACKLIGHT));
        strobe(value);
    }

    private void i2cWrite(int value) {
        i2cWrite(value, 0);
    }

    private void i2cWrite(int value, int mode) {
        i2cWriteFourBits(mode | (value & 0xF0));
        i2cWriteFourBits(mode | ((value << 4) & 0xF0));
    }

    private void refresh() {
        clearLcd();

        for (int i = 0; i < lines.length; i++) {
            if (lines[i] != null) {
                logger.info("Will display '" + lines[i] + "' on line " + (i + 1));

                i2cWrite(LINE_ADDR[i]);
                byte[] strBytes = lines[i].getBytes();
                for (int j = 0; j < strBytes.length; j++) {
                    i2cWrite(strBytes[j], Rs);
                }
            }
        }
    }

    @Override
    public void println(String str) {

        String strRemain = str.trim();
        String strLine;

        while (strRemain.length() > 0) {
            int currentLineLength = Integer.min(lineLength, strRemain.length());
            strLine = strRemain.substring(0, currentLineLength);
            strRemain = strRemain.substring(currentLineLength);
            for (int i = 1; i < lines.length; i++) {
                lines[i - 1] = lines[i];
            }
            lines[lines.length - 1] = strLine;
        }

        refresh();
    }

    private void clearLcd() {
        i2cWrite(LCD_CLEARDISPLAY);
        i2cWrite(LCD_RETURNHOME);
    }

    @Override
    public void clear() {
        println("");
        println("");
    }


    public static void main(String args[]) throws InterruptedException {
        LoggerFactory.init(Level.INFO);
        System.out.println("Hello LCD");

        LCD screen = new LCD_I2C();

        while (true) {
            screen.println("Coucou");
            Thread.sleep(1000);
            screen.clear();
            System.out.println("Clear");
            Thread.sleep(1000);
        }
    }

}
