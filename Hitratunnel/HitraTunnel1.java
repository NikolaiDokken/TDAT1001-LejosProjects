/* SensorTest.java  GS - 2015-08-20

   Program som tester bruk av trykk-sensor og farge-sensor (ev3).
   Programmet g�r i l�kke og leser av og skriver avlest-farge p� EV3ens LCD-skjerm.
   L�kken/ programemt avsluttes n�r trykksensoren trykkes p�.

*/

import lejos.hardware.motor.*;
import lejos.hardware.lcd.*;
import lejos.hardware.port.Port;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.Keys;
import lejos.robotics.SampleProvider;
import lejos.hardware.sensor.*;


public class HitraTunnel{
	public static void main (String[] arg) throws Exception  {
		Motor.A.setSpeed(400);
		Motor.B.setSpeed(400); // sett hastighet (toppfart = 900)

		try{

			Brick brick = BrickFinder.getDefault();
			Port s1 = brick.getPort("S1"); // fargesensor
    		Port s2 = brick.getPort("S2"); // trykksensor
    		Port s3 = brick.getPort("S3"); // ultrasonisksensor

			EV3 ev3 = (EV3) BrickFinder.getLocal();
			TextLCD lcd = ev3.getTextLCD();
			Keys keys = ev3.getKeys();


			/* Definerer en trykksensor */
			SampleProvider trykksensor = new EV3TouchSensor(s2);
			float[] trykkSample = new float[trykksensor.sampleSize()]; // tabell som inneholder avlest verdi

			/* Definerer en fargesensor og fargeAvleser */
			EV3ColorSensor fargesensor = new EV3ColorSensor(s1); // ev3-fargesensor
			SampleProvider fargeLeser = fargesensor.getMode("RGB");  // svart = 0.01..
			//SampleProvider fargeLeser = fargesensor.getColorIDMode();  // OBS: Funker ikke - får arrayfeil. can identify 8 unique colors (NONE, BLACK, BLUE, GREEN, YELLOW, RED, WHITE, BROWN).

    		float[] fargeSample = new float[fargeLeser.sampleSize()];  // tabell som innholder avlest verdi

			/* Definerer en ultrasonisksensor */
			EV3UltrasonicSensor ultraSensor = new EV3UltrasonicSensor(s3);
			SampleProvider ultraLeser = ultraSensor.getDistanceMode();
			float[] ultraSample = new float[ultraLeser.sampleSize()]; // tabell som inneholder avlest verdi

			boolean fortsett  = true;
			int teller = 0;

			while(fortsett) {
				//Fargesensor
				fargeLeser.fetchSample(fargeSample, 0);  // hent verdi fra fargesensor
				lcd.drawString("Farge: " + fargeSample[0], 0, 3);
				//System.out.println("Farge: " + fargeSample[0]);

				//Avstandsensor
				ultraLeser.fetchSample(ultraSample, 0);

				if(fargeSample[0] < 0.01){
					Motor.A.forward();
					Motor.B.backward();
					Thread.sleep(200);
				}
				
				if(ultraSample[0] < 0.3){
					if(teller < 2){
						Motor.A.forward();
						Motor.B.backward();
						Thread.sleep(100);
						teller++;
					} else {
						Motor.A.backward();
						Motor.B.forward();
						Thread.sleep(100);
						teller = 0;
					}
				} else{
					Motor.A.forward();
					Motor.B.forward();
					Thread.sleep(100);
				}
				

				//Hvis man trykker på killswitchen ender programmet:
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