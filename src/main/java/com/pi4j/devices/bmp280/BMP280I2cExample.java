package com.pi4j.devices.bmp280;

/*
 *
 *
 *  #%L
 *  **********************************************************************
 *  ORGANIZATION  :  Pi4J
 *  PROJECT       :  Pi4J ::  Providers
 *  FILENAME      :  BMP280I2cExample.java
 *
 *  This file is part of the Pi4J project. More information about
 *  this project can be found here:  https://pi4j.com/
 *  **********************************************************************
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU General Lesser Public
 *  License along with this program.  If not, see
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 *  #L%
 *
 */


import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.drivers.sensor.environment.bmx280.Bmx280Driver;
import com.pi4j.io.i2c.I2C;
import com.pi4j.util.Console;

import java.text.DecimalFormat;


/**
 * Class to test and demonstrate the BMP280 device.
 */
public class BMP280I2cExample {



   private static final int DEFAULT_ADDRESS = 0x77;
   private static final int DEFAULT_BUS = 0x1;

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


        int busNum = DEFAULT_BUS;
        int address = DEFAULT_ADDRESS;
        I2C i2c;

        // ------------------------------------------------------------
        // Initialize the Pi4J Runtime Context
        // ------------------------------------------------------------
        // Before you can use Pi4J you must initialize a new runtime
        // context.
        //
        // The 'Pi4J' static class includes a few helper context
        // creators for the most common use cases.  The 'newAutoContext()'
        // method will automatically load all available Pi4J
        // extensions found in the application's classpath which
        // may include 'Platforms' and 'I/O Providers'


        Context pi4j = Pi4J.newAutoContext();

        // print installed providers
        System.out.println("----------------------------------------------------------");
        System.out.println("PI4J PROVIDERS");
        System.out.println("----------------------------------------------------------");
        pi4j.providers().describe().print(System.out);
        System.out.println("----------------------------------------------------------");


        final Console console = new Console();
        console.print("==============================================================");
        console.print("startup  BMP280I2cExample ");
        console.print("==============================================================");


        String helpString = " parms: -b hex value bus    -a hex value address  " ;
            for (int i = 0; i < args.length; i++) {
            String o = args[i];
            if (o.contentEquals("-b")) { // bus
                String a = args[i + 1];
                busNum = Integer.parseInt(a.substring(2), 16);
                i++;
            } else if (o.contentEquals("-a")) { // device address
                String a = args[i + 1];
                i++;
                address = Integer.parseInt(a.substring(2), 16);
            }  else if (o.contentEquals("-h")) {
                console.println(helpString);
                System.exit(39);
            } else {
                console.println("  !!! Invalid Parm " + args);
                console.println(helpString);
                System.exit(42);
            }
        }
       i2c =  createI2cDevice(pi4j, busNum, address);
        Bmx280Driver bmpDev = new Bmx280Driver(i2c);

        bmpDev.reset();
        DecimalFormat df = new DecimalFormat("0.###");

        Bmx280Driver.Measurement measurement = bmpDev.readMeasurement();

        console.println(" Temperature C = " + df.format(measurement.getTemperature()) );

        console.println(" Temperature F = " +  df.format(measurement.getTemperature()  * 1.8 + 32)  );

        console.println(" Pressure Pa = " + df.format(measurement.getPressure()));

        console.println(" Pressure mbar = " + df.format(measurement.getPressure() / 100));

        console.println(" Pressure InHg = " + df.format(measurement.getPressure() / 3386));

        console.println("Humidity: " + df.format(measurement.getHumidity()) + " %");

        console.println("  Dev I2C detail    bus : " + busNum + "  address : " + String.format("%02x", address));


        // Shutdown Pi4J
        pi4j.shutdown();
    }


    /**
     * Use the state from the Sensor config object and the state pi4j to create
     * a BMP280 device instance
     */
    private static I2C createI2cDevice(Context pi4j, int busNum, int address) {

        String id = String.format("0X%02x: ", busNum);
        String name = String.format("0X%02x: ", address);
        var i2cDeviceConfig = I2C.newConfigBuilder(pi4j)
            .bus(busNum)
            .device(address)
            .id(id + " " + name)
            .name(name)
            .build();
        return  pi4j.create(i2cDeviceConfig);
        }
}