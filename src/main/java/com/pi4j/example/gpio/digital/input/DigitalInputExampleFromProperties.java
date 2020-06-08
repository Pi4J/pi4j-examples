package com.pi4j.example.gpio.digital.input;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: EXAMPLE  :: Sample Code
 * FILENAME      :  DigitalInputExampleFromProperties.java
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
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalStateChangeListener;
import com.pi4j.util.Console;

import java.util.Properties;

/**
 * <p>DigitalInputExampleFromProperties class.</p>
 *
 * @author Robert Savage (<a href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 * @version $Id: $Id
 */
public class DigitalInputExampleFromProperties {

    /** Constant <code>DIGITAL_INPUT_PIN=4</code> */
    public static final int DIGITAL_INPUT_PIN = 4;

    /**
     * <p>Constructor for DigitalInputExampleFromProperties.</p>
     */
    public DigitalInputExampleFromProperties() {
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
        console.title("<-- The Pi4J Project -->", "Basic Digital Input Example From Properties");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // Initialize Pi4J with an auto context
        // An auto context includes AUTO-DETECT BINDINGS enabled
        // which will load all detected Pi4J extension libraries
        // (Platforms and Providers) in the class path
        var pi4j = Pi4J.newAutoContext();

        // create a properties map with ".address" and ".shutdown" properties for the digital output configuration
        Properties properties = new Properties();
        properties.put("id", "my_digital_input");
        properties.put("address", DIGITAL_INPUT_PIN);
        properties.put("pull", "UP");
        properties.put("name", "MY-DIGITAL-INPUT");

        // create a digital input instance using the default digital input provider
        // we will use the PULL_DOWN argument to set the pin pull-down resistance on this GPIO pin
        var config = DigitalInput.newConfigBuilder(pi4j)
                .load(properties)
                .build();

        var input = pi4j.din().create(config);

        // setup a digital output listener to listen for any state changes on the digital input
        input.addListener(console::print);

        // lets read the digital output state
        console.print("DIGITAL INPUT [");
        console.print(input);
        console.print("] STATE IS [");
        console.println(input.state() + "]");

        console.print("DIGITAL INPUT [");
        console.print(input);
        console.print("] PULL RESISTANCE IS [");
        console.println(input.pull() + "]");

        console.println();
        console.println("CHANGE INPUT STATES VIA I/O HARDWARE AND CHANGE EVENTS WILL BE PRINTED BELOW:");

        // wait (block) for user to exit program using CTRL-C
        console.waitForExit();

        // shutdown Pi4J
        console.println("ATTEMPTING TO SHUTDOWN/TERMINATE THIS PROGRAM");
        pi4j.shutdown();
    }
}
