package com.pi4j.example.gpio.digital.input;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: EXAMPLE  :: Sample Code
 * FILENAME      :  DigitalInputExampleWithMockProvider.java
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
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.DigitalStateChangeListener;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInput;
import com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInputProvider;
import com.pi4j.util.Console;


/**
 * <p>DigitalInputExampleWithMockProvider class.</p>
 *
 * @author Robert Savage (<a href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 * @version $Id: $Id
 */
public class DigitalInputExampleWithMockProvider {

    /** Constant <code>DIGITAL_INPUT_PIN=4</code> */
    public static final int DIGITAL_INPUT_PIN = 4;

    /**
     * <p>Constructor for DigitalInputExampleWithMockProvider.</p>
     */
    public DigitalInputExampleWithMockProvider() {
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
        console.title("<-- The Pi4J Project -->", "Basic Digital Input Example With Mock Provider");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        // Initialize Pi4J with an auto context
        // An auto context includes AUTO-DETECT BINDINGS enabled
        // which will load all detected Pi4J extension libraries
        // (Platforms and Providers) in the class path
        var pi4j = Pi4J.newAutoContext();

        // get the Mock Digital Input provider by ID
        console.println("ATTEMPTING TO GET MOCK DIGITAL INPUT PROVIDER FROM Pi4J");
        var provider = pi4j.providers().get(MockDigitalInputProvider.ID, MockDigitalInputProvider.class);

        // display acquired provider
        console.println("--> ACQUIRED PROVIDER: ");
        console.print("--> ");
        console.print(provider);
        console.println();

        // create a digital input instance using the default digital input provider
        console.println("ATTEMPTING TO CREATE A MOCK DIGITAL INPUT INSTANCE");
        MockDigitalInput input = provider.create(DIGITAL_INPUT_PIN);

        // display created instance
        console.print("--> CREATED IO INSTANCE: ");
        console.print(input);
        console.println();

        // setup a digital output listener to listen for any state changes on the digital input
        input.addListener((DigitalStateChangeListener) event -> {
            console.print("DIGITAL INPUT [");
            console.print(event.source());
            console.print("] STATE CHANGE: ");
            console.println(event.state());
        });

        // lets read the digital output state
        console.print("CURRENT DIGITAL INPUT STATE IS [");
        console.println(input.state() + "]");

        // change mock state of mock digital input so we can see some events firing
        input.mockState(DigitalState.HIGH)
             .mockState(DigitalState.LOW)
             .mockState(DigitalState.HIGH)
             .mockState(DigitalState.LOW);

        // shutdown Pi4J
        console.println("ATTEMPTING TO SHUTDOWN/TERMINATE THIS PROGRAM");
        pi4j.shutdown();
    }
}
