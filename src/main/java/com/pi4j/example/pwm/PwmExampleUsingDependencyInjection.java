package com.pi4j.example.pwm;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: EXAMPLE  :: Sample Code
 * FILENAME      :  PwmExampleUsingDependencyInjection.java
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
import com.pi4j.io.pwm.Pwm;
import com.pi4j.io.pwm.PwmType;
import com.pi4j.plugin.pigpio.provider.pwm.PiGpioPwmProvider;
import com.pi4j.util.Console;

import java.util.concurrent.Callable;

/**
 * <p>PwmExampleUsingDependencyInjection class.</p>
 *
 * @author Robert Savage (<a href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 * @version $Id: $Id
 */
public class PwmExampleUsingDependencyInjection {

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

        // create a digital output instance using the default digital output provider
        @Register("My PWM Pin")
        @Address(13)  // pin number 13
        @WithProvider(type=PiGpioPwmProvider.class)
        @WithPwmType(PwmType.HARDWARE)
        @ShutdownValue(0)    // 0% duty-cycle on shutdown
        @InitialValue(10)    // 10% duty-cycle on startup
        @DutyCycle(50)       // 50% duty-cycle by default (when called with 'on()')
        @Frequency(500)      // 500 Hz frequency used by default (until overwritten)
        @AddPwmPreset(name = "one-quarter", dutyCycle = 25.5f )   // 25%
        @AddPwmPreset(name = "three-quarter", dutyCycle = 75.0f ) // 75%
        @AddPwmPreset(name = "1KHZ", frequency = 1000)
        @AddPwmPreset(name = "10KHZ", frequency = 10000, dutyCycle = 25.5f)
        private Pwm pwm;

        @Override
        public Void call() throws Exception {

            // create Pi4J console wrapper/helper
            // (This is a utility class to abstract some of the boilerplate stdin/stdout code)
            final var console = new Console();

            // print program title/header
            console.title("<-- The Pi4J Project -->", "PWM Example using Dependency Injection");

            // Initialize Pi4J with an auto context
            // An auto context includes AUTO-DETECT BINDINGS enabled
            // which will load all detected Pi4J extension libraries
            // (Platforms and Providers) in the class path
            // ...
            // Also, inject this class instance into the Pi4J context
            // for annotation processing and dependency injection
            var contextBuidler = Pi4J.newContextBuilder().autoDetect();

            // TODO :: REMOVE TEMPORARY PROPERTIES WHEN NATIVE PIGPIO LIB IS READY
            // this temporary property is used to tell
            // PIGPIO which remote Raspberry Pi to connect to
            contextBuidler.property("host", "rpi3bp.savage.lan");

            // create Pi4J context
            Context pi4j = contextBuidler.build().inject(this);

            pi4j.describe().print(System.out);

            console.println("[PWM I/O INSTANCE] : ");
            pwm.describe().print(System.out);
            console.println();
            console.println(" ... PWM SIGNAL SHOULD BE <--ON--> USING INITAL DUTY-CYCLE VALUE: " + pwm.config().initialValue().floatValue());
            console.println();
            console.println(" - GPIO PIN   : " + pwm.address());
            console.println(" - PWM TYPE   : " + pwm.pwmType());
            console.println(" - FREQUENCY  : " + pwm.frequency() + " Hz");
            console.println(" - DUTY-CYCLE : " + pwm.dutyCycle() + "%");
            console.println(" - IS-ON      : " + pwm.isOn());
            console.println();
            console.println(" ... WAITING 10 SECONDS TO CALL ON() METHOD");

            // wait 10 seconds
            Thread.sleep(10000);

            // turn on the PWM pin
            pwm.on();

            console.println("[PWM I/O INSTANCE] : ");
            pwm.describe().print(System.out);
            console.println();
            console.println(" ... PWM SIGNAL SHOULD BE <--ON--> USING CONFIGURED DUTY-CYCLE: " + pwm.config().dutyCycle().floatValue());
            console.println();
            console.println(" - GPIO PIN   : " + pwm.address());
            console.println(" - PWM TYPE   : " + pwm.pwmType());
            console.println(" - FREQUENCY  : " + pwm.frequency() + " Hz");
            console.println(" - DUTY-CYCLE : " + pwm.dutyCycle() + "%");
            console.println(" - IS-ON      : " + pwm.isOn());
            console.println();
            console.println(" ... WAITING 10 SECONDS TO APPLY PRESET: 10KHZ");

            // wait 10 seconds
            Thread.sleep(10000);

            // turn off PWM pin
            pwm.applyPreset("10KHZ");

            console.println();
            console.println(" ... PWM SIGNAL SHOULD BE <--10KHZ-->");
            console.println();
            console.println(" - GPIO PIN   : " + pwm.address());
            console.println(" - PWM TYPE   : " + pwm.pwmType());
            console.println(" - FREQUENCY  : " + pwm.frequency() + " Hz");
            console.println(" - DUTY-CYCLE : " + pwm.dutyCycle() + "%");
            console.println(" - IS-ON      : " + pwm.isOn());
            console.println();
            console.println(" ... WAITING 10 SECONDS TO TURN PWM SIGNAL OFF");

            // wait 10 seconds
            Thread.sleep(10000);

            // turn off PWM pin
            pwm.off();

            console.println();
            console.println(" ... PWM SIGNAL SHOULD BE <--OFF-->");
            console.println();
            console.println(" - GPIO PIN   : " + pwm.address());
            console.println(" - PWM TYPE   : " + pwm.pwmType());
            console.println(" - FREQUENCY  : " + pwm.frequency() + " Hz");
            console.println(" - DUTY-CYCLE : " + pwm.dutyCycle() + "%");
            console.println(" - IS-ON      : " + pwm.isOn());

            // shutdown Pi4J
            pi4j.shutdown();

            // we are done
            return null;
        }
    }
}
