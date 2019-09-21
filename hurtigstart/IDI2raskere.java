import lejos.hardware.motor.*;
import lejos.hardware.lcd.*;
import lejos.hardware.port.Port;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.ev3.EV3;
import lejos.hardware.Keys;
import lejos.robotics.SampleProvider;
import lejos.hardware.sensor.*;
import lejos.hardware.Sound;
import java.io.File;

class jakepaul extends Thread {
         jakepaul() {

         }

         public void run() {
            while (true) {
				Sound.setVolume(200);
				File file = new File("tokyo.wav");
				Sound.playSample(file, 100);
		}
    }
}
class IDI2 {
    public static void main (String[] arg) throws Exception {
		try{
			
			jakepaul jake = new jakepaul();
			jake.setDaemon(true);
			jake.start();
		
        // Henter inn porter
        Brick brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1"); // fargesensor
        Port s2 = brick.getPort("S2"); // trykksensor
        
        // Skjerm shit
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();

		// Trykksensor
		SampleProvider trykksensor = new EV3TouchSensor(s2);
		float[] trykkSample = new float[trykksensor.sampleSize()]; // tabell som inneholder avlest verdi
		

		// Fargesensor
		EV3ColorSensor fargesensor = new EV3ColorSensor(s1); // ev3-fargesensor
		SampleProvider fargeLeser = fargesensor.getMode("RGB");

		float[] fargeSample = new float[fargeLeser.sampleSize()];  // tabell som innholder avlest verdi
   
        Motor.A.setSpeed(600);
        Motor.B.setSpeed(600);
		boolean fortsett  = true;
		int i = 0;
		
		while (fortsett) {
           	fargeLeser.fetchSample(fargeSample, 0);  // hent verdi fra fargesensor
            // lcd.drawString("Farge: " + fargeSample[0], 0, 3);
            // lcd.drawString("i: " + i, 0, 6);			
        	
			if (fargeSample[0] > 0.25) {
				Motor.A.setSpeed(900);
				Motor.B.setSpeed(900);
				Motor.A.forward();
				Motor.B.forward();
				
			} else if (fargeSample[0] > 0.08) {
                //if(i == 4){
                  //  Motor.A.stop(true);
                    //Motor.B.stop(true);
					// }

				if(i == 20){
					Motor.A.setSpeed(150);
					Motor.B.setSpeed(150);
                    Motor.B.forward();
                    Motor.A.backward();
                    Thread.sleep(20);
				} else {
                    Motor.A.setSpeed(150);
				    Motor.B.setSpeed(150);
				    Motor.B.backward();
                    Motor.A.forward();
                    Thread.sleep(10);
					i++;
                }
                
       	 	} else {
			    Motor.A.setSpeed(600);
                Motor.B.setSpeed(600);
                Motor.A.forward();
	            Motor.B.forward();
			    i = 0;
        	}

            //Hvis man trykker på killswitchen ender programmet:
            if (trykkSample != null && trykkSample.length > 0){
                trykksensor.fetchSample(trykkSample, 0);
                if (trykkSample[0] > 0){
                    System.out.println("Avslutter");
                    fortsett = false;
                }
            }
			}
		} catch(Exception e){
			System.out.println("FEIL:" + e);
			Thread.sleep(1000);
	}
    }
}