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
import com.pi4j.io.pwm.PwmConfig;
import com.pi4j.io.pwm.PwmConfigBuilder;
import com.pi4j.io.pwm.PwmType;
import com.pi4j.util.Console;
import com.pi4j.util.Delay;

import java.text.DecimalFormat;
import java.text.NumberFormat;

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

    private static final int DEFAULT_PWM_FREQUENCY = 50;
    private static final int DEFAULT_CHANNEL_NUMBER = 2;
    private static final int SERVO_NUMBER = 1;
    private static final NumberFormat FORMAT = new DecimalFormat("0.###");

    private static double degree = 90.0;
    private static Integer channel = DEFAULT_CHANNEL_NUMBER;
    private static Console console;
    private static Fs90rDriver fs90R;
    private static final Delay delay = new Delay();
    private static java.util.Scanner scanner;
    private static double lowPulse = 1000.0;

    private static double highPulse = 2000.0;

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
                .frequency(DEFAULT_PWM_FREQUENCY))));


        // -cw -ccw -d mutually exclusive
        commandHandler.configuration(
            "c",
            "Set the PWM channel number",
            new IntegerParameter(0, 127),
            Fs90rDriver.DEFAULT_CHANNEL_NUMBER);

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

        DoubleMappedParameter.Rotation ranges[] = new DoubleMappedParameter.Rotation[]{
            new DoubleMappedParameter.Rotation("S", 75.0),
            new DoubleMappedParameter.Rotation("M", 48.0),
            new DoubleMappedParameter.Rotation("F", 0.0)
        };

        commandHandler.configuration(
            "cw",
            "Set the PWM rotation by range  S M F",
            DoubleMappedParameter(ranges),
            90.0);



        for (int i = 0; i < args.length; i++) {
            String o = args[i];
            if  (o.contentEquals("-cw")) {  /* 0-89 */
                String a = args[i + 1];
                onlyOne++;
                i++;
                if (a.equalsIgnoreCase("S")) {
                    degree = 75.0;
                } else if (a.equalsIgnoreCase("M")) {
                    degree = 40.0;
                } else if (a.equalsIgnoreCase("F")) {
                    degree = 0.0;
                } else {
                    console.println("  -cw invalid ");
                    System.exit(41);
                }
            } else if (o.contentEquals("-ccw")) {  /* 191-180 */
                String a = args[i + 1];
                i++;
                onlyOne++;
                if (a.equalsIgnoreCase("S")) {
                    degree = 105.0;
                } else if (a.equalsIgnoreCase("M")) {
                    degree = 140.0;
                } else if (a.equalsIgnoreCase("F")) {
                    degree = 180.0;
                } else {
                    console.println("  -ccw invalid ");
                    System.exit(42);
                }
            } else if (o.contentEquals("-q")) {
                console.println("Exit");
                System.exit(0);
            } else if (o.contentEquals("-h")) {
                console.println(helpString);
                System.exit(0);
            } else {
                console.println("  !!! Invalid Parm " + o);
                console.println(helpString);
                System.exit(43);
            }
        }

        if (onlyOne > 1) {
            console.println(" mutually exclusive parms used.");
            console.println(helpString);
            System.exit(44);
        }
        Pwm pwm = createPwm(SERVO_NUMBER, channel, pi4j);

        fs90R = new Fs90rDriver(pwm, lowPulse, highPulse);

        // if user did not provide a -cw -ccw or -d   default sets stop
        fs90R.setServoRotation(degree);

        waitChange(10L);

        pi4j.shutdown(pwm);

    }

    static Pwm createPwm(int servoNumber, Integer channel, Context pi4j) {

        final PwmConfig config = PwmConfigBuilder.newInstance(pi4j)
            .id("fs90RNumber" + servoNumber)
            .name("fs90R number " + servoNumber)
            .pwmType(PwmType.HARDWARE)
            .channel(channel)
            .chip(PwmChipUtil.getPWMChip())
            .frequency(DEFAULT_PWM_FREQUENCY)
            .build();

        return pi4j.create(config);
    }

    static void waitChange(long c) {
        while (true) {
            delay.setMillis(c).materialize();
            console.println("Enter q - quit, or degree value ");
            if (scanner.hasNextInt()) {
                double nextDegree = scanner.nextDouble();
                if ((!Double.isFinite(nextDegree) || nextDegree < 0) || (nextDegree > 180)) {
                    console.println(" degree must be in the range 0..180. 90 stop, 0..89 CW, 91..180 CCW");
                } else {
                    fs90R.setServoRotation(nextDegree);
                }
            } else {
                break;
            }
        }
    }
}





