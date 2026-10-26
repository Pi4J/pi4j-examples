/*
 *  /*
 *  *
 *
 */

package com.pi4j.devices.fs90r;

import com.pi4j.Pi4J;
import com.pi4j.boardinfo.util.PwmChipUtil;
import com.pi4j.context.Context;
import com.pi4j.drivers.motor.fs90r.Fs90rDriver;
import com.pi4j.examples.utils.commandhandler.DeviceCommandHandler;
import com.pi4j.examples.utils.commandhandler.DoubleParameter;
import com.pi4j.examples.utils.commandhandler.IntegerParameter;
import com.pi4j.examples.utils.commandhandler.DoubleMappedParameter;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.pwm.PwmType;
import com.pi4j.util.Console;
import com.pi4j.util.Delay;

/**
 * Example implementation for a FS90R servo motor.
 * The Fs90R defines a specific PWM interface and exactly how the PWN signal
 * controls the servo
 * <p>
 * https://www.pololu.com/product/2820
 * <p>
 * See the README.md file for example usage and explanation.
 * <p>
 * FS90R servo motor control.  The driver will use the PWM device created by
 * this app. It is assumed the device is a HardWare PWM.  Hardware PWM creates
 * a more consistent PWM signal.
 */
public class Fs90rApp {
    private static final int SERVO_NUMBER = 1;
    private static final int PWM_CHANNEL = 2;
    private static Console console;
    private static final Delay delay = new Delay();
    private static java.util.Scanner scanner;

    public Fs90rApp() {
        super();

    }

    /**
     * <p>main.</p>
     * See the README.md file for example usage.
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
        console.title("<-- The Pi4J V5 Project Extension  -->", "Fs90rApp");
        String helpString = " Parms: -c channel -cw (clockwise) S M F  -ccw (counter-clockwise) S M F  -h HELP \n" +
            "    -low microseconds -high microseconds  -cw -ccw mutually exclusive  -d degree \n" +
            "    degree must be in the range 0..180. 90 stop, 0..89 CW, 91..180 CCW ";

        DeviceCommandHandler<Fs90rDriver> commandHandler = new DeviceCommandHandler<>(settings ->
            new Fs90rDriver(pi4j.create(Pwm.newConfigBuilder()
                .channel((Integer) settings.get("c"))
                .id("fs90RNumber" + SERVO_NUMBER)
                .name("fs90R number " + SERVO_NUMBER)
                .pwmType(PwmType.HARDWARE)
                .chip(PwmChipUtil.getPWMChip())
                .frequency(Fs90rDriver.PWM_FREQUENCY))));


        // -cw -ccw -d mutually exclusive
        commandHandler.configuration(
            "c",
            "Set the PWM channel number",
            new IntegerParameter(0, 127),
            PWM_CHANNEL);

        commandHandler.configuration(
            "low",
            "Set the PWM low microseconds",
            new DoubleParameter(500.0, 1500.0),
            1000.0);

        commandHandler.configuration(
            "high",
            "Set the PWM high microseconds",
            new DoubleParameter(1500.0, 2500.00),
            2000.0);

           commandHandler.configuration(
            "d",
            "Set the PWM rotation by degree",
            new DoubleParameter(0.0, 180.0),
            90.0);

        DoubleMappedParameter.Rotation rangesCw[] = new DoubleMappedParameter.Rotation[]{
            new DoubleMappedParameter.Rotation("S", 75.0),
            new DoubleMappedParameter.Rotation("M", 48.0),
            new DoubleMappedParameter.Rotation("F", 0.0)
        };

        commandHandler.configuration(
            "cw",
            "Set the PWM rotation cw by range  S M F",
            new DoubleMappedParameter(rangesCw),
            90.0);


        DoubleMappedParameter.Rotation rangesCcw[] = new DoubleMappedParameter.Rotation[]{
            new DoubleMappedParameter.Rotation("S", 105.0),
            new DoubleMappedParameter.Rotation("M", 148.0),
            new DoubleMappedParameter.Rotation("F", 180.0)
        };
        commandHandler.configuration(
            "ccw",
            "Set the PWM rotation ccw by range  S M F",
            new DoubleMappedParameter(rangesCcw),
            90.0);


 // TODO  compiles.
        // 1.  How to pull the value from settings   I want the value from say -cw to be used. I could obtain the value
        // first and call something similar to this passing in the double.
        //  2. How to call directly, from interactive data passing the new double value
        // appears setting this label as default it is not invoked
        commandHandler.parameterizedAction("setDegree", "Set servo degree",  new DoubleParameter(0.0, 180), (device, pt) -> {
            device.setServoRotation(pt);  // set to allow testing
            System.out.println("Servo degree set to : " + pt);
        });




        commandHandler.action("degree", "Set servo motor to this degree (default)", device -> {
            device.setServoRotation((double) 180.0);  // set to allow testing
            System.out.println("Servo degree set to : " + "???");
        });


        //  I prefer to handle this in code rather than only documentation.  low priority I can worry about later
        if (onlyOne > 1) {
            console.println(" mutually exclusive parms used.");
            console.println(helpString);
            System.exit(44);
        }

        commandHandler.setDefault("setDegree");

        commandHandler.process(args);



        waitChange(10L);


        // Shutdown Pi4J
        pi4j.shutdown();

    }


    static void waitChange(long c) {
        while (true) {
            delay.setMillis(c).materialize();
            console.println("Enter - to quit, or degree value ");
            if (scanner.hasNextInt()) {
                double nextDegree = scanner.nextDouble();
                if ((!Double.isFinite(nextDegree) || nextDegree < 0) || (nextDegree > 180)) {
                    console.println(" degree must be in the range 0..180. 90 stop, 0..89 CW, 91..180 CCW");
                } else {
                    // TODO  this will require on the fly changing an action value . This was
                    // mentioned in the earlier TODO
                   //   this no longer used, left as a Note  fs90R.setServoRotation(nextDegree);
                }
            } else {
                break;
            }
        }
    }
}





