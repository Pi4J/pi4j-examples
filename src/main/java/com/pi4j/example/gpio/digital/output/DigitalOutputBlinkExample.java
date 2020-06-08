package com.pi4j.example.gpio.digital.output;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: EXAMPLE  :: Sample Code
 * FILENAME      :  DigitalOutputBlinkExample.java
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
import com.pi4j.io.IOType;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfig;
import com.pi4j.io.gpio.digital.DigitalOutputProvider;
import com.pi4j.io.gpio.digital.DigitalStateChangeListener;
import com.pi4j.platform.Platform;

import java.util.concurrent.TimeUnit;

/**
 * <p>DigitalOutputBlinkExample class.</p>
 *
 * @author Robert Savage (<a href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 * @version $Id: $Id
 */
public class DigitalOutputBlinkExample {

    /** Constant <code>DIGITAL_OUTPUT_PIN=4</code> */
    public static final int DIGITAL_OUTPUT_PIN = 4;

    /**
     * <p>Constructor for DigitalOutputBlinkExample.</p>
     */
    public DigitalOutputBlinkExample() {
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     * @throws java.lang.Exception if any.
     */
    public static void main(String[] args) throws Exception {

        // Initialize Pi4J with an auto context
        // An auto context includes AUTO-DETECT BINDINGS enabled
        // which will load all detected Pi4J extension libraries
        // (Platforms and Providers) in the class path
        var pi4j = Pi4J.newAutoContext();

        // get default runtime platform
        Platform platform = pi4j.platforms().getDefault();

        // get default digital output provide for this platform
        DigitalOutputProvider provider = platform.provider(IOType.DIGITAL_OUTPUT);

        // create I/O instance configuration using the config builder
        DigitalOutputConfig config = DigitalOutputConfig.newBuilder(pi4j).address(3).build();

        // use factory to create/register  I/O instance
        DigitalOutput output = provider.create(config);

        // setup a digital output listener to listen for any state changes on the digital output
        output.addListener((DigitalStateChangeListener) event -> {
            System.out.println(event);
        });

        // lets toggle the digital output state a few times
        output.toggle()
              .toggle()
              .toggle();

        // another friendly method of setting output state
        output.high()
              .low();

        // blink the output for 10 seconds
        output.blink(1, 10, TimeUnit.SECONDS);

        // shutdown Pi4J
        pi4j.shutdown();
    }
}


