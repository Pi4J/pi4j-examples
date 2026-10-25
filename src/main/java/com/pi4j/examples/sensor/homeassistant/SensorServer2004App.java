package com.pi4j.examples.sensor.homeassistant;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.drivers.display.character.CharacterDisplay;
import com.pi4j.drivers.display.character.hd44780.Hd44780Driver;
import com.pi4j.examples.sensor.display.SensorAggregator;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfigBuilder;

public class SensorServer2004App {
    private static final int I2C_BUS = 1;
    private static final int DISPLAY_I2C_ADDRESS = 0x27;
    private static final int DISPLAY_WIDTH = 20;
    private static final int DISPLAY_HEIGHT = 4;
    private static final int HTTP_PORT = 8147;

    public static void main(String[] args) {
        Context pi4j = Pi4J.newAutoContext();
        I2C displayI2c = pi4j.create(I2C.newConfigBuilder(pi4j).bus(I2C_BUS).device(DISPLAY_I2C_ADDRESS));
        CharacterDisplay characterDisplay = Hd44780Driver.withPcf8574Connection(displayI2c, DISPLAY_WIDTH, DISPLAY_HEIGHT);

        new SensorServer(pi4j, I2C_BUS, characterDisplay, HTTP_PORT).run();
    }
}
