import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;

public class JegHaterLejos {
 
 public static void main(String[] args) throws Exception{
 
 System.out.println("Hei EV3 - du suger litt.");
 Thread.sleep(500);
 EV3 ev3 = (EV3) BrickFinder.getLocal();
 TextLCD lcd = ev3.getTextLCD();
 Keys keys = ev3.getKeys();
 lcd.drawString("Heisann Hoppsann", 4, 4);
 keys.waitForAnyPress();
 }
} 

