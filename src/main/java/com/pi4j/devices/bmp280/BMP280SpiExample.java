package com.pi4j.devices.bmp280;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.drivers.sensor.environment.bmx280.Bmx280Driver;
import com.pi4j.examples.utils.commandhandler.DeviceCommandHandler;
import com.pi4j.examples.utils.commandhandler.IntegerParameter;
import com.pi4j.io.spi.Spi;
import com.pi4j.io.spi.SpiBus;
import com.pi4j.io.spi.SpiMode;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BMP280SpiExample {

    private static final int DEFAULT_BUS = 0x0;
    private static final NumberFormat FORMAT = new DecimalFormat("0.###");

    /**
     * Sample application using BMP280 sensor chip.
     *
     * @param args an array of {@link java.lang.String} objects.
     *             Parms are not required. if 'any' parameter value is supplied,
     *             the example uses the create pattern for device instantiation,
     *             otherwise provider setup is used
     * @throws java.lang.Exception if any.
     */
    static void main(String[] args) throws Exception {
        System.out.println("BMP 280 I2C Example");
        System.out.println();

        Context pi4j = Pi4J.newAutoContext();

        DeviceCommandHandler<Bmx280Driver> commandHandler = new DeviceCommandHandler<>(settings ->
            new Bmx280Driver(pi4j.create(Spi.newConfigBuilder()
                .id("SPI" + settings.get("bus") + "_BMP280")
                .name("SENSOR")
                .baud(Spi.DEFAULT_BAUD)    // Max 10MHz
                .baud(Spi.DEFAULT_BAUD)    // Max 10MHz
                .mode(SpiMode.MODE_0)
                .bus((Integer) settings.get("bus"))
                .channel((Integer) settings.get("chipdelect")))));

        commandHandler.configuration(
            "chipselect",
            "Set the BMx280 SPI chip select",
            new IntegerParameter(0, 8),
            Spi.DEFAULT_CHANNEL);

        commandHandler.configuration(
            "bus",
            "Set the BMx280 SPI Bus",
            new IntegerParameter(0, 6),
            DEFAULT_BUS);

        commandHandler.action("reset", "Reset the sensor", device -> {
            device.reset();
        });

        commandHandler.action("read", "Read measurements (default)", device -> {
            Bmx280Driver.Measurement measurement = device.readMeasurement();

            System.out.println("Temperature °C: " + FORMAT.format(measurement.getTemperature()));
            System.out.println("Temperature °F: " + FORMAT.format(measurement.getTemperature() * 1.8 + 32));
            System.out.println("Pressure Pa: " + FORMAT.format(measurement.getPressure()));
            System.out.println("Pressure mbar: " + FORMAT.format(measurement.getPressure() / 100));
            System.out.println("Pressure InHg: " + FORMAT.format(measurement.getPressure() / 3386));
        });

        commandHandler.setDefault("read");
        commandHandler.process(args);

        // Shutdown Pi4J
        pi4j.shutdown();
    }

    /**
     * Use the state from the Sensor config object and the state pi4j to create
     * a BMP280 device instance
     */
    private static Spi createSPIDevice(Context pi4j, SpiBus spiBus, int channel) {
        var spiConfig = Spi.newConfigBuilder(pi4j)
            .id("SPI" + spiBus + "_BMP280")
            .name("D/A converter")
            .bus(spiBus)
            .channel(channel)
            //1 20 19 18 17 16 15 14 13 12 11 10  9  8  7  6  5  4  3  2  1  0
            //b  b  b  b  b  b  R  T  n  n  n  n  W  A u2 u1 u0 p2 p1 p0  m  m
            // .flags(0b0000000000000000100000L)  // MODE0, ux GPIO not used for chip select
            .baud(Spi.DEFAULT_BAUD)    // Max 10MHz
            .mode(SpiMode.MODE_0)
            .build();
        return pi4j.create(spiConfig);
    }
}