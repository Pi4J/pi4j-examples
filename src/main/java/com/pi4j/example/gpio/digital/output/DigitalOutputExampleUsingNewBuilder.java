package com.pi4j.example.gpio.digital.output;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: EXAMPLE  :: Sample Code
 * FILENAME      :  DigitalOutputExampleUsingNewBuilder.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.pi4j.Pi4J;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.DigitalStateChangeListener;
import com.pi4j.plugin.mock.Mock;
import com.pi4j.util.Console;

import java.util.concurrent.TimeUnit;

/**
 * <p>DigitalOutputExampleUsingNewBuilder class.</p>
 *
 * @author Robert Savage (<a href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 * @version $Id: $Id
 */
public class DigitalOutputExampleUsingNewBuilder {

    /** Constant <code>DIGITAL_OUTPUT_PIN=4</code> */
    public static int DIGITAL_OUTPUT_PIN = 4;

    /**
     * <p>Constructor for DigitalOutputExampleUsingNewBuilder.</p>
     */
    public DigitalOutputExampleUsingNewBuilder() {
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     * @throws java.lang.Exception if any.
     */
    public static void main(String[] args) throws Exception {

        // create Pi4J console wrapper/helper
        // (This is a utility class to abstract some of the boilerplate stdin/stdout code)
        final var console = new Console();

        // print program title/header
        console.title("<-- The Pi4J Project -->", "Basic Digital Output Example");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // Initialize Pi4J with an auto context
        // An auto context includes AUTO-DETECT BINDINGS enabled
        // which will load all detected Pi4J extension libraries
        // (Platforms and Providers) in the class path
        var pi4j = Pi4J.newAutoContext();

        // create a digital output instance using the digital output builder
        var output = DigitalOutput.newBuilder(pi4j)
                        .address(DIGITAL_OUTPUT_PIN)
                        .id("DIGITAL-OUTPUT-PIN-4")
                        .name("My Digital Output Pin")
                        .shutdown(DigitalState.HIGH)
                        .platform(Mock.PLATFORM_ID)
                        .build();

        // setup a digital output listener to listen for any state changes on the digital output
        output.addListener(System.out::println);

        // lets invoke some changes on the digital output
        output.state(DigitalState.HIGH)
              .state(DigitalState.LOW)
              .state(DigitalState.HIGH)
              .state(DigitalState.LOW);

        // lets toggle the digital output state a few times
        output.toggle()
              .toggle()
              .toggle();


        // another friendly method of setting output state
        output.high()
              .low();

        // lets read the digital output state
        System.out.print("CURRENT DIGITAL OUTPUT [" + output + "] STATE IS [");
        System.out.println(output.state() + "]");

        // pulse to HIGH state for 3 seconds
        System.out.println("PULSING OUTPUT STATE TO HIGH FOR 3 SECONDS");
        output.pulse(3, TimeUnit.SECONDS, DigitalState.HIGH);
        System.out.println("PULSING OUTPUT STATE COMPLETE");

        // shutdown Pi4J
        console.println("ATTEMPTING TO SHUTDOWN/TERMINATE THIS PROGRAM");
        pi4j.shutdown();
    }
}
