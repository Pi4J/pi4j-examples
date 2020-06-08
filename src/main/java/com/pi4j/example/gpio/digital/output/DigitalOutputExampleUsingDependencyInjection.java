package com.pi4j.example.gpio.digital.output;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: EXAMPLE  :: Sample Code
 * FILENAME      :  DigitalOutputExampleUsingDependencyInjection.java
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
import com.pi4j.annotation.*;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.DigitalStateChangeEvent;
import com.pi4j.io.gpio.digital.DigitalStateChangeListener;
import com.pi4j.util.Console;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * <p>DigitalOutputExampleUsingDependencyInjection class.</p>
 *
 * @author Robert Savage (<a href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 * @version $Id: $Id
 */
public class DigitalOutputExampleUsingDependencyInjection {

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     * @throws java.lang.Exception if any.
     */
    public static void main(String[] args) throws Exception {

        // Pi4J cannot perform dependency injection on static classes
        // we will create a container instance to run our example
        new RuntimeContainer().call();
    }

    public static class RuntimeContainer implements Callable<Void> {

        public static final int DIGITAL_OUTPUT_PIN = 4;
        public static final String DIGITAL_OUTPUT_PIN_ID = "4";

        // create a digital output instance using the default digital output provider
        @Register(DIGITAL_OUTPUT_PIN_ID)
        @Address(DIGITAL_OUTPUT_PIN)
        @Name("My Digi Out")
        @ShutdownState(DigitalState.HIGH)
        @InitialState(DigitalState.HIGH)
        private DigitalOutput output;

        @Inject
        private Context pi4j;

        // register a digital output listener to listen for any state changes on the digital output
        @Register(DIGITAL_OUTPUT_PIN_ID)
        private DigitalStateChangeListener digitalChangeListener = event -> System.out.println(" (LISTENER #1) :: " + event);

        // setup a digital output event listener to listen for any state changes on the digital output
        // using a custom method with a single event parameter
        @OnEvent(DIGITAL_OUTPUT_PIN_ID)
        private void onDigitalOutputChange(DigitalStateChangeEvent event){
            System.out.println(" (LISTENER #2) :: " + event);
        }

        @Override
        public Void call() throws Exception {

            // create Pi4J console wrapper/helper
            // (This is a utility class to abstract some of the boilerplate stdin/stdout code)
            final var console = new Console();

            // print program title/header
            console.title("<-- The Pi4J Project -->", "Basic Digital Output Example Using Dependency Injection");

            // allow for user to exit program using CTRL-C
            console.promptForExit();

            // Initialize Pi4J with an auto context
            // An auto context includes AUTO-DETECT BINDINGS enabled
            // which will load all detected Pi4J extension libraries
            // (Platforms and Providers) in the class path
            // ...
            // Also, inject this class instance into the Pi4J context
            // for annotation processing and dependency injection
            Pi4J.newAutoContext().inject(this);

            // lets invoke some changes on the digital output
            output.state(DigitalState.HIGH)
                    .state(DigitalState.LOW)
                    .state(DigitalState.HIGH)
                    .state(DigitalState.LOW);

            // lets toggle the digital output state a few times
            output.toggle()
                  .toggle()
                  .toggle();

            // additional friendly methods for setting output state
            output.high()
                  .low();

            // lets read the digital output state
            console.print("CURRENT DIGITAL OUTPUT [" + output + "] STATE IS [");
            console.println(output.state() + "]");

            // pulse to HIGH state for 3 seconds
            console.println("PULSING OUTPUT [" + output + "] STATE TO HIGH FOR 3 SECONDS");
            output.pulse(3, TimeUnit.SECONDS, DigitalState.HIGH);

            // friendly pulsing methods for setting output state
            console.println("PULSING OUTPUT [" + output + "] STATE TO HIGH FOR 2 SECONDS");
            output.pulseHigh(2, TimeUnit.SECONDS);
            console.println("PULSING OUTPUT [" + output + "] STATE COMPLETE");

            // shutdown Pi4J
            console.println("ATTEMPTING TO SHUTDOWN/TERMINATE THIS PROGRAM");
            pi4j.shutdown();

            // we are done
            return null;
        }
    }
}
