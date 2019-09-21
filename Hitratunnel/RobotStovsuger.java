/* RobotStovsuger.java

   Programmet får EV3 roboten til å kjøre i mønsteret til en robotstøvsuger.
   Kjører rett frem intill den møter på et hinder; snur seg til venstre og
   prøver å kjøre videre.
*/

import lejos.hardware.motor.*;
import lejos.hardware.lcd.*;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.hardware.sensor.NXTColorSensor;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.hardware.sensor.NXTUltrasonicSensor;
import lejos.hardware.port.Port;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.Keys;
import lejos.hardware.sensor.SensorModes;
import lejos.robotics.SampleProvider;
import lejos.hardware.sensor.*;


public class RobotStovsuger{
	public static void main (String[] arg) throws Exception  {
		Motor.A.setSpeed(400); // setter hastigheten for motorene
		Motor.B.setSpeed(400); // sett hastighet (toppfart = 900)

		try{

			Brick brick = BrickFinder.getDefault();
    		Port s2 = brick.getPort("S2"); // trykksensor
    		Port s3 = brick.getPort("S3"); // ultrasonisksensor

			EV3 ev3 = (EV3) BrickFinder.getLocal();
			TextLCD lcd = ev3.getTextLCD();
			Keys keys = ev3.getKeys();


			/* Definerer en trykksensor */
			SampleProvider trykksensor = new EV3TouchSensor(s2);
			float[] trykkSample = new float[trykksensor.sampleSize()]; // tabell som inneholder avlest verdi

			/* Definerer en ultrasonisksensor */
			EV3UltrasonicSensor ultraSensor = new EV3UltrasonicSensor(s3);
			SampleProvider ultraLeser = ultraSensor.getDistanceMode();
			float[] ultraSample = new float[ultraLeser.sampleSize()]; // tabell som inneholder avlest verdi

			boolean fortsett  = true;

			while(fortsett) {
				ultraLeser.fetchSample(ultraSample, 0);
				if(ultraSample[0] < 0.3){
					Motor.A.forward();
					Motor.B.backward();
					Thread.sleep(100);
				} else{
					Motor.A.forward();
					Motor.B.forward();
					Thread.sleep(200);
				}

	  			if (trykkSample != null && trykkSample.length > 0){
	  				trykksensor.fetchSample(trykkSample, 0);
	  				if (trykkSample[0] > 0){
		  				System.out.println("Avslutter");
		 			 	fortsett = false;
	 				}
  	 			}else System.out.println("Sample er null eller 0");
  			} // while
		}catch(Exception e){
			System.out.println("Feil: " + e);
		} //try-catch
	} // main
} // class SensorTest