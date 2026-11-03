/*
 *  /*
 *  *
 *
 */

package com.pi4j.devices.sg90;

import com.pi4j.Pi4J;
import com.pi4j.boardinfo.util.PwmChipUtil;
import com.pi4j.context.Context;
import com.pi4j.drivers.motor.sg90.Sg90Driver;
import com.pi4j.examples.utils.commandhandler.DeviceCommandHandler;
import com.pi4j.examples.utils.commandhandler.DoubleParameter;
import com.pi4j.examples.utils.commandhandler.IntegerParameter;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.pwm.PwmType;
import com.pi4j.util.Console;

/**
 * SG90 servo motor control.
 * Example implementation for a FS90R servo motor.
 * The Fs90R defines a specific PWM interface and exactly how the PWN signal
 * controls the servo.
 * https://www.friendlywire.com/projects/ne555-servo-safe/SG90-datasheet.pdf
 * <p><The driver will use the PWM device created by
 * this app. It is assumed the device is a HardWare PWM.  Hardware PWM creates
 * a more consistent PWM signal.
 * </p>
 * <p>
 * See README.md for more information and detail.
 * </p>
 */
class Sg90App {

    private static final int PWM_CHANNEL_NUMBER = 2;
    private static final int SERVO_NUMBER = 1;
    private static Console console;

    public Sg90App() {
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

        console = new Console();
        Context pi4j = Pi4J.newAutoContext();
        console.title("<-- The Pi4J V5 Project Extension  -->", "Sg90App");
        String helpString = " Parms: -c channel   -q quit -low microseconds -high microseconds -h HELP";

        DeviceCommandHandler<Sg90Driver> commandHandler = new DeviceCommandHandler<>(settings ->
            new Sg90Driver(pi4j.create(Pwm.newConfigBuilder()
                .channel((Integer) settings.get("c"))
                .id("fs90RNumber" + SERVO_NUMBER)
                .name("fs90R number " + SERVO_NUMBER)
                .pwmType(PwmType.HARDWARE)
                .chip(PwmChipUtil.getPWMChip())
                .frequency(Sg90Driver.PWM_FREQUENCY))));

        commandHandler.configuration(
            "c",
            "Set the PWM channel number",
            new IntegerParameter(1, 4),
            PWM_CHANNEL_NUMBER);

        // Following actions can be used as arguments when first invoking this program (-low value) , or
        // after instantiation used on the command line (low value).

        commandHandler.parameterizedAction("low", "Minimal pulse width", new DoubleParameter(500.0, 1500.0), (device, param) -> {
            device.setPulseWidthMin(param);
        });

        commandHandler.parameterizedAction("high", "Maximum pulse width", new DoubleParameter(1500.0, 2500.0), (device, param) -> {
            device.setPulseWidthMax(param);
        });

        commandHandler.parameterizedAction("degree", "rotation degree", new DoubleParameter(0.0, 180.0), (device, param) -> {
            device.setServoAngle(param);
        });

        commandHandler.process(args);

        // Shutdown Pi4J
        pi4j.shutdown();

    }

}
