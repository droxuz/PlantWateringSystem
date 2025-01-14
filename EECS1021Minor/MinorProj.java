package eecs1021;
import edu.princeton.cs.introcs.StdDraw;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import java.io.IOException;
import org.firmata4j.I2CDevice;
import org.firmata4j.ssd1306.SSD1306;

import java.util.ArrayList;
import	java.util.Timer;

public class MinorProj
{
    static final int waterSensor = 15; //water sensor
    static final int waterPump = 2; // water pump
    static	final	byte	I2C0	=	0x3C;	//	OLED	Display
    public static void main(String[] args)
            throws
            IOException, InterruptedException
    {
        var myArduino = new FirmataDevice("COM3");
        myArduino.start();
        myArduino.ensureInitializationIsDone();
        // initialize OLED
        I2CDevice I2CObject = myArduino.getI2CDevice(I2C0);
        SSD1306 oledObject = new SSD1306(I2CObject, SSD1306.Size.SSD1306_128_64);
        oledObject.init();

        // initialize waterPump
        var waterPumpObject = myArduino.getPin(waterPump);
        waterPumpObject.setMode(Pin.Mode.OUTPUT);

        // initialize waterSensor
        var waterSensorObject = myArduino.getPin(waterSensor);
        //waterSensorObject.setMode(Pin.Mode.ANALOG);

        // setup of timer task
        Timer timer = new Timer();
        var task = new MoistureTask(oledObject,waterPumpObject,waterSensorObject);
        new Timer().schedule(task,0,1000);

        // Setting the count for graph
        var count = 1;
        var countArray = new ArrayList<Integer>();
        var sensorArray = new ArrayList<Double>();
        //Graph Setup
        StdDraw.setXscale(-3,20);
        StdDraw.setYscale(-30,100);

        //Pen parameters
        StdDraw.setPenRadius(0.010);
        StdDraw.setPenColor(StdDraw.RED);

        //Drawing axes
        StdDraw.line(0,0,0,50);
        StdDraw.line(0,0,40,0);

        //Labels
        StdDraw.text(10,-20,"Time");
        StdDraw.text(-3,25,"Volts");
        StdDraw.text(10,60,"Time vs Moisture");



        //Updating Graph
        while (true)
        {
            var sensorValue = task.getwaterSensor();
            double fitted5VSensor = (double)sensorValue*5/1023;
            fitted5VSensor = fitted5VSensor*10;
            countArray.add(count);
            sensorArray.add(fitted5VSensor);
            //StdDraw.text((double)count,(double)50,"*");
            StdDraw.show();
            System.out.println(fitted5VSensor);
            System.out.println(sensorValue);

            Thread.sleep(1000);
            if (count > 40){

                for (int i = 0; i < 40;){
                    StdDraw.text((double)countArray.get(i),sensorArray.get(i),"*");
                    i++;
                }
                //Graph Setup
                Thread.sleep(10000);
                countArray.clear();
                sensorArray.clear();
                StdDraw.clear();
                StdDraw.setXscale(-3,40);
                StdDraw.setYscale(-30,100);

                //Pen parameters
                StdDraw.setPenRadius(0.010);
                StdDraw.setPenColor(StdDraw.RED);

                //Drawing axes
                StdDraw.line(0,0,0,50);
                StdDraw.line(0,0,40,0);

                //Labels
                StdDraw.text(10,-20,"Time");
                StdDraw.text(-3,25,"Volts");
                StdDraw.text(10,60,"Time vs Moisture");
                count = 0;
            }
            count++;
        }
    }
}
