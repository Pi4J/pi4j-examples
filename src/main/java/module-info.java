/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: EXAMPLE  :: Sample Code
 * FILENAME      :  module-info.java
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
module com.pi4j.example {

    // Pi4J MODULES
    requires com.pi4j;
    requires com.pi4j.plugin.pigpio;

    // SLF4J MODULES
    requires org.slf4j;
    requires org.slf4j.simple;

    uses com.pi4j.extension.Extension;
    uses com.pi4j.provider.Provider;

    // Pi4J Mock Platform and Providers
    requires com.pi4j.plugin.mock;
    uses com.pi4j.plugin.mock.platform.MockPlatform;
    uses com.pi4j.plugin.mock.provider.gpio.analog.MockAnalogInput;
    uses com.pi4j.plugin.mock.provider.gpio.analog.MockAnalogInputProvider;
    uses com.pi4j.plugin.mock.provider.gpio.analog.MockAnalogOutput;
    uses com.pi4j.plugin.mock.provider.gpio.analog.MockAnalogOutputProvider;
    uses com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInput;
    uses com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalInputProvider;
    uses com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalOutput;
    uses com.pi4j.plugin.mock.provider.gpio.digital.MockDigitalOutputProvider;
    uses com.pi4j.plugin.mock.provider.pwm.MockPwm;
    uses com.pi4j.plugin.mock.provider.pwm.MockPwmProvider;
    uses com.pi4j.plugin.mock.provider.i2c.MockI2C;
    uses com.pi4j.plugin.mock.provider.i2c.MockI2CProvider;
    uses com.pi4j.plugin.mock.provider.spi.MockSpi;
    uses com.pi4j.plugin.mock.provider.spi.MockSpiProvider;
    uses com.pi4j.plugin.mock.provider.serial.MockSerial;
    uses com.pi4j.plugin.mock.provider.serial.MockSerialProvider;

    // allow access to classes in the following namespaces for Pi4J annotation processing
    opens com.pi4j.example.gpio.analog.input to com.pi4j;
    opens com.pi4j.example.gpio.analog.inputtooutput to com.pi4j;
    opens com.pi4j.example.gpio.analog.output to com.pi4j;
    opens com.pi4j.example.gpio.digital.input to com.pi4j;
    opens com.pi4j.example.gpio.digital.inputtooutput to com.pi4j;
    opens com.pi4j.example.gpio.digital.output to com.pi4j;
    opens com.pi4j.example.pwm to com.pi4j;
    opens com.pi4j.example.i2c to com.pi4j;
    opens com.pi4j.example.serial to com.pi4j;
    opens com.pi4j.example.spi to com.pi4j;
    opens com.pi4j.example to com.pi4j;
}
