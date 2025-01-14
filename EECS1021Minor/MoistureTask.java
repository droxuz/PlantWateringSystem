package eecs1021;
import org.firmata4j.Pin;
import org.firmata4j.firmata.FirmataDevice;
import java.io.IOException;
import org.firmata4j.I2CDevice;
import org.firmata4j.ssd1306.SSD1306;
import  java.util.Timer;
import java.util.TimerTask;
public class MoistureTask extends TimerTask
{
    private final Pin waterPump;
    private final Pin waterSensor;
    private final SSD1306 oledObject;
    private long sampleSensor;
    public MoistureTask(SSD1306 oledObject, Pin waterPump, Pin waterSensor)
    {
        this.oledObject = oledObject;
        this.waterPump = waterPump;
        this.waterSensor = waterSensor;
    }
    public long getwaterSensor(){
        return sampleSensor;
    }
    @Override
    public void run()
    {
        //clears oled display
        oledObject.getCanvas().clear();

        //initializes variables for logic
        var moist = waterSensor.getValue();
        int maxMoist = 540;

        // getting value to graph
        sampleSensor = waterSensor.getValue();
        if (moist>maxMoist && moist>=0)
        {
            try{
                waterPump.setValue(1);
            }
            catch(IOException e){
                System.out.println("ERROR");
            }
            oledObject.getCanvas().drawString(0,0,"PUMP ON");
            oledObject.getCanvas().drawString(0,5, "SOIL IS DRY: The Moisture is "+moist);
            oledObject.display();
        }
        else
        {
            try{
                waterPump.setValue(0);
            }
            catch(IOException e){
                System.out.println("ERROR");
            }
            oledObject.getCanvas().drawString(0,0,"PUMP OFF");
            oledObject.getCanvas().drawString(0,5,"SOIL IS WET: The Moisture is "+moist);
            oledObject.display();
        }

    }
}
