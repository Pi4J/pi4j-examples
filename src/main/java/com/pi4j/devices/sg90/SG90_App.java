/*
 *  /*
 *  *
 *
 */

package com.pi4j.devices.sg90;

import com.pi4j.boardinfo.util.PwmChipUtil;
import com.pi4j.drivers.motor.sg90.SG90Driver;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.pwm.PwmConfig;
import com.pi4j.io.pwm.PwmConfigBuilder;
import com.pi4j.io.pwm.PwmType;
import com.pi4j.util.Console;
import com.pi4j.util.Delay;

/**
 *   SG90 servo motor control.  The driver will use the PWM device created by
 *   this app. It is assumed the device is a HardWare PWM.  Hardware PWM creates
 *   a more consistent PWM signal.
 *
 *   See README.md for more information and detail.
 */
class SG90_App {

    private static final int DEFAULT_PWM_FREQUENCY = 50;
    private static final int DEFAULT_CHANNEL_NUMBER = 2;
    private static final int SERVO_NUMBER = 1;

    private static Integer channel = DEFAULT_CHANNEL_NUMBER ;
    private static Console console ;
    private static SG90Driver sg90 ;
    private static final Delay delay = new Delay();
    private static java.util.Scanner scanner;
    private static float lowPulse = 1000;
    private static float highPulse = 2000;


    public SG90_App() {
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

        scanner = new java.util.Scanner(System.in);

        console = new Console();
        Context pi4j = Pi4J.newAutoContext();
        console.title("<-- The Pi4J V5 Project Extension  -->", "SG90_App");
        String helpString = " Parms: -c channel   -q quit -low microseconds -high microseconds -h HELP";


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
            }  else if (o.contentEquals("-c")) {
                String a = args[i + 1];
                channel = Integer.parseInt(a.substring(0));
                i++;
            }else if (o.contentEquals("-q")) {
                console.println("Exit");
                System.exit(0);
            } else if (o.contentEquals("-h")) {
                console.println(helpString);
                System.exit(0);
            }else {
                console.println("  !!! Invalid Parm " + o);
                console.println(helpString);
                System.exit(42);
            }
        }

        Pwm pwm = createPwm(SERVO_NUMBER, channel, pi4j);

        sg90 = new SG90Driver(pwm, lowPulse, highPulse);

        waitChange( 10L);

        pwm.close();

    }

    static void waitChange(long c){
        while(true){
            delay.setMillis(c).materialize();
            console.println("Enter degree value or enter any key to quit");
            if (scanner.hasNextInt() ) {
                float nextDegree = scanner.nextFloat();
                if((nextDegree < 0) || (nextDegree > 180)){
                    console.println("  degree must be in the range 0..180");
                }else {
                    sg90.setServoAngle(nextDegree);
                }
            } else{
                break;
            }
         }
    }


    static Pwm createPwm(int servoNumber, Integer channel, Context pi4j) {

            final PwmConfig config = PwmConfigBuilder.newInstance (pi4j)
                .id ("sg90Number" + servoNumber)
                .name ("SG90 number " + servoNumber)
                .channel(channel)
                .pwmType(PwmType.HARDWARE)
                .channel(channel)
                .chip(PwmChipUtil.getPWMChip())
                .frequency(DEFAULT_PWM_FREQUENCY)
                .build ();

        return pi4j.create (config);
    }

}
