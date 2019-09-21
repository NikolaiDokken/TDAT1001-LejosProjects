import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.*;
public class SverdProgram {
 
 public static void main(String[] args) throws Exception{
	 Motor.D.setSpeed(400);
	 
	 int teller = 10;
	 while (teller > 0) {
		 System.out.println("slag " + teller);
		 Motor.D.forward();
		 Thread.sleep(200);
		 Motor.D.backward();
		 Thread.sleep(200);
		 teller --;
	}
	Motor.D.stop();
 }
}