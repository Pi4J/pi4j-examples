# Sensor Display Example

![Sensor data rendered on a 1602 LCD dispaly](sensor_dispay_1602.jpg)

This example renders the sensor values provided by autodetected I2C sensors
on a display attached to the Raspberry Pi.

## Hardware Setup

Connect any available I2C sensor(s) and a HD44780+PCF8574 based 16x2 LCD to I2C bus 1 
(Pins 1-3 for power, SDA and SCL and pin 6 for Ground).

## Example Code

The example code groups sensor values by the types of the available sensor values,
then aggregates the values if multiple are available.

Finally, the values are rendered on a character display (such as a 16x2 LCD).
If no display is provided, the measured values are printed to the console via stdout.

- SensorAggregator.java: The code example for aggregating sensor values and rendering
  then formatted to the screen space available.

- SensorDisplay1602App.java: The main program providing the setup code for a 
  HD44780-driven 16x2 LDC connected via a PCF8574 I2C IO expander.

## Expansion Ideas

- Connect a different display, e.g. a graphics display via the corresponding adapter.

- Connect a Red/Green/Yellow LED to illustrate the CO2 level in the room.
