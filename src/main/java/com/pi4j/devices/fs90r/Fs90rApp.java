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
import com.pi4j.examples.utils.commandhandler.StringParameter;
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.pwm.PwmType;
import com.pi4j.util.Console;
import com.pi4j.util.Delay;

/**
 * Example implementation for a FS90R servo motor.
 * The Fs90R defines a specific PWM interface and exactly how the PWN signal
 * controls the servo
 * <p>
 * <a href="https://www.pololu.com/product/2820">Servo details</a>
 * <p>
 * See the README.md file for example usage and explanation.
 * <p>
 * FS90R servo motor control.  The driver will use the PWM device created by
 * this app. It is assumed the device is a HardWare PWM.  Hardware PWM supports
 * a more consistent PWM signal.
 * The user controls the direction of rotation via setDirection(), where cw denotes clockwise and
 * ccw counterclockwise.  The direction must be set prior to use of setSpeed().
 * SetSpeed() is a percentage of the maximum RPM.
 *
 *
 */
public class Fs90rApp {
    private static final int SERVO_NUMBER = 1;
    private static final int PWM_CHANNEL = 2;
    private static Console console;
    private static final Delay delay = new Delay();

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
        console = new Console();
        Context pi4j = Pi4J.newAutoContext();
        console.title("<-- The Pi4J V5 Project Extension  -->", "Fs90rApp");

        DeviceCommandHandler<Fs90rDriver> commandHandler = new DeviceCommandHandler<>(settings ->
            new Fs90rDriver(pi4j.create(Pwm.newConfigBuilder()
                .channel((Integer) settings.get("c"))
                .id("fs90RNumber" + SERVO_NUMBER)
                .name("fs90R number " + SERVO_NUMBER)
                .pwmType(PwmType.HARDWARE)
                .chip(PwmChipUtil.getPWMChip())
                .frequency(Fs90rDriver.PWM_FREQUENCY))));

        commandHandler.configuration(
            "c",
            "Set the PWM channel number",
            new IntegerParameter(1, 4),
            PWM_CHANNEL);

        // Following actions can be used as command arguments after instantiation of this program via './runSg90.sh - '.

        commandHandler.parameterizedAction("low", "Minimal pulse width", new DoubleParameter(500.0, 1500.0), (device, param) -> {
            device.setPulseWidthMin(param);
        });


        commandHandler.parameterizedAction("high", "Maximum pulse width", new DoubleParameter(1500.0, 2500.0), (device, param) -> {
            device.setPulseWidthMax(param);
        });

        commandHandler.parameterizedAction("percent", "set rotation by percentage of max possible", new DoubleParameter(0.0, 100.0), (device, param) -> {
            device.setSpeed(param);
        });

        commandHandler.parameterizedAction("direction", "set rotation direction", new StringParameter("cw", "ccw"), (device, param) -> {
            if (param.equals("cw")) {
                device.setDirection(Fs90rDriver.ServoDirection.CLOCKWISE);
            } else {
                device.setDirection(Fs90rDriver.ServoDirection.COUNTERCLOCKWISE);
            }

        });

        commandHandler.process(args);

        // Shutdown Pi4J
        pi4j.shutdown();

    }

}





