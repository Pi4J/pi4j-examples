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
readouts.

