# Pi4J Sensor Drivers 

All sensors in the Pi4j drivers repository implement a shared Sensor interface.

In addition to a method for reading the sensor values, this interface provides a generic description of 
the sensor, accessible via the getDescriptor() method. 

As most sensors provide access to multiple values, the sensor descriptor contains 
a list of value descriptors. Each value descriptor describes the kind of measurement contained
in the corresponding value filled in by readMeasurement() at the corresponding index.

For instance, the BMP280 sensor descriptor value list contains three entries for temperature,
pressure and humidity. 

Sensors connected via I2C often are detectable via special identification commands or register 
readouts. These sensors can be enumerated via SensorDetector.detectI2cSensors().

For more details about using sensor drivers and autodetection with Pi4J, please refer to our 
sensor examples:

- [Autodetect](autodetect/): Detecting Sensors
- [Sensor Display](display/): Rendering sensor values on a small display
- [Homeassistant](homeassistant/): Integrating senseor values in a home assistant setup





