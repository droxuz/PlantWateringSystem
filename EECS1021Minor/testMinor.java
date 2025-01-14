package eecs1021;

import jm.audio.io.SampleOut;
import org.firmata4j.firmata.FirmataDevice;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import java.io.IOException;
import org.firmata4j.I2CDevice;
import org.firmata4j.ssd1306.SSD1306;
import	java.util.Timer;
public class testMinor
{
    public static void main(String[] args)
            throws IOException, InterruptedException
    {
        var myArduino = new FirmataDevice("COM3");
        myArduino.start();
        myArduino.ensureInitializationIsDone();
        var waterSensorObject = myArduino.getPin(15);
        var moist = String.valueOf(waterSensorObject.getValue());
        System.out.println(moist);
    }
}
