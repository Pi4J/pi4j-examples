/*
 *  /*
 *  *
 *
 */

package com.pi4j.devices.fs90r;


import com.pi4j.drivers.motor.fs90r.Fs90RDriver;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.pwm.PwmConfigBuilder;
import com.pi4j.boardinfo.util.PwmChipUtil;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.pwm.PwmConfig;
import com.pi4j.io.pwm.PwmType;
import com.pi4j.util.Console;
import com.pi4j.util.Delay;

/**
 * Driver for a FS90R servo motor.
 * The Fs90R defines a specific PWM interface and exactly how the PWN signal
 * controls the servo
 *
 * https://www.pololu.com/product/2820
 *
 *
 *   These devices do not have a customary DataSheet.  Most servo devices provide limited specifications
 * for required pulse width, they just say 1 - 2 millisecond.
 *
 * The FS90 R 360 defines a specific PWM interface and exactly how the PWN signal
 * controls the servo. There are many different manufacturers of the FS90 servo.
 * The pulse width required for setting the servo at 0 degrees MAX Clockwise (CW) or 180 degrees
 * MAX Countrer ClockWise (CCW) varies by manufacturer. The driver by default uses the pulse width timing as
 * (low)1000 - (high)2000 microseconds. The user of this example application can change these defaults
 * when first invoking the program.
 *
 * If the servo does not rotate to the expected RPM,Example to rotate faster CW  reduce the -low
 *  argument, to rotate CCW faster increase the -high argument. The -low -high change should be balanced or 90 degree will not properly align.
 * Meaning if you reduce the -low by 200, you should increase the -high by 200, or visa-versa.
 *
 *
 */

    /**
     *   SG90R servo motor control.  The driver will use the PWM device created by
     *   this app. It is assumed the device is a HardWare PWM.  Hardware PWM creates
     *   a more consistent PWM signal.
     */
    public class FS90R_App {

        private static final int DEFAULT_PWM_FREQUENCY = 50;
        private static final int DEFAULT_CHANNEL_NUMBER = 2;
        private static final int SERVO_NUMBER = 1;



        private static Pwm pwm ;
        private static float degree = 90;
        private static Integer channel = DEFAULT_CHANNEL_NUMBER ;
        private static Console console ;
        private static Fs90RDriver fs90R ;
        private static final Delay delay = new Delay();
        private static java .util.Scanner scanner;
        private static float lowPulse = 1000;
        private static float highPulse = 2000;


        public FS90R_App() {
            super();

        }


        /**
         * <p>main.</p>
         *
         * @param args an array of {@link java.lang.String} objects.
         * @throws java.lang.Exception if any.
         */
         static void main(String[] args) throws Exception {
           System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "INFO");

           int onlyOne = 0;   // numerous parms a mutually exclusive

            scanner = new java.util.Scanner(System.in);

            console = new Console();
            Context pi4j = Pi4J.newAutoContext();
            console.title("<-- The Pi4J V5 Project Extension  -->", "SG90_App");
            String helpString = " Parms: -c channel -cw (clockwise) S M F  -ccw (counter-clockwise) S M F  -h HELP \n" +
                "    -low microseconds -high microseconds  -cw -ccw mutually exclusive  -d degree \n" +
                "    degree must be in the range 0..180. 90 stop, 0..89 CW, 91..180 CCW ";


            for (int i = 0; i < args.length; i++) {
                String o = args[i];
                if (o.contentEquals("-low")) {
                    String a = args[i + 1];
                    lowPulse = Integer.parseInt(a.substring(0));
                    i++;
                } else if (o.contentEquals("-high")) {
                    String a = args[i + 1];
                    highPulse = Integer.parseInt(a.substring(0));
                    i++;
                }  if (o.contentEquals("-d")) {
                    String a = args[i + 1];
                    degree = Float.parseFloat(a.substring(0));
                    if((degree < 0) || (degree > 180)){
                        console.println("-d  degree must be in range 0..180");
                        System.exit(40);
                    }
                    onlyOne++;
                    i++;
                } else if (o.contentEquals("-c")) {
                    String a = args[i + 1];
                    channel = Integer.parseInt(a.substring(0));
                    i++;
                } else if (o.contentEquals("-cw")) {  /* 0-89 */
                    String a = args[i + 1];
                    onlyOne++;
                    i++;
                    if (a.equalsIgnoreCase("S")) {
                        degree = 75;
                    } else if (a.equalsIgnoreCase("M")) {
                        degree = 40;
                    }  else if (a.equalsIgnoreCase("F")) {
                        degree = 0;
                    } else {
                        console.println("  -cw invalid ");
                        System.exit(41);
                    }
                } else if (o.contentEquals("-ccw")) {  /* 191-180 */
                    String a = args[i + 1];
                    i++;
                    onlyOne++;
                   if (a.equalsIgnoreCase("S")) {
                        degree = 105;
                    } else if (a.equalsIgnoreCase("M")) {
                        degree = 140;
                    }  else if (a.equalsIgnoreCase("F")) {
                        degree = 180;
                    } else {
                        console.println("  -cw invalid ");
                        System.exit(42);
                    }
                } else if (o.contentEquals("-q")) {
                    console.println("Exit");
                    System.exit(0);
                } else if (o.contentEquals("-h")) {
                    console.println(helpString);
                    System.exit(0);
                }else {
                    console.println("  !!! Invalid Parm " + o);
                    console.println(helpString);
                    System.exit(43);
                }
            }

            if (onlyOne > 1 ) {
                console.println(" mutually exclusive parms used.");
                console.println(helpString);
                System.exit(44);
            }
            pwm = createPwm(SERVO_NUMBER, DEFAULT_CHANNEL_NUMBER, pi4j);

            fs90R = new Fs90RDriver(pwm, lowPulse, highPulse);

            // if user did not provide a -cw -ccw or -d   default sets stop
            fs90R.setServoRotation(degree);

            waitChange( 10l);

            pi4j.shutdown(pwm);

        }


        static Pwm createPwm(int servoNumber, Integer channel, Context pi4j) {

            final PwmConfig config = PwmConfigBuilder.newInstance (pi4j)
                .id ("fs90RNumber" + servoNumber)
                .name ("fs90R number " + servoNumber)
                .pwmType(PwmType.HARDWARE)
                .channel(channel)
                .chip(PwmChipUtil.getPWMChip())
                .frequency(DEFAULT_PWM_FREQUENCY)
                .build ();

            return pi4j.create (config);
        }

// todo  -cw   -ccw   -d
         static void waitChange(long c){
            while(true){
                delay.setMillis(c).materialize();
                console.println("Enter q - quit, or degree value ");
                if (scanner.hasNextInt() ) {
                    int nextDegree = scanner.nextInt();
                    if((nextDegree < 0) || (nextDegree > 180)){
                        console.println(" degree must be in the range 0..180. 90 stop, 0..89 CW, 91..180 CCW");
                    }else {
                        fs90R.setServoRotation(nextDegree);
                    }
                } else{
                    break;
                }
            }
        }
    }





