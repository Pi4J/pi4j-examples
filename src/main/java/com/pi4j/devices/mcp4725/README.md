### This repository contains device specific support for various devices.

### This is an example implementation and will need to be adjusted to fit your needs.

Project by Tom Aarts
==========================================================================

                   Adafruit MCP4725 

Pi 3.3V Vin
Pi Grnd GND
Pi Pin5 SCL
Pi Pin3 SDA
Pi Grnd A0
        Vout  MCP3008 ch7


1. ./mvnw clean package
2. cd target/distribution
3. sudo ./runMcp4725.sh parms........

https://datasheetspdf.com/pdf-file/634126/MicrochipTechnology/MCP4725/1



-b 0x? hex value bus    
-a 0x?? hex value address  
-vref float reference voltage
-pv true/false.  Persist values, default false.
-r  reset chip   
-d  digital input   0 - 4095
-v  voltage  0 - vref

Note    -v -d  mutually exclusive

-v cannot exceed -vref


Updates using raw binary value
To Update DAC and EEPROM, set to 50 % of reference voltage 1.6v Vin of 3.3 volts
sudo ./runMcp4725.sh -b 0x01 -a 0x62 -d 2047 -vref 3.3 -pv true

To Update DAC Fast, set to 100 % of reference voltage   
sudo ./runMcp4725.sh -b 0x01 -a 0x62 -d 4095 -r -vref 3.3  -pv false

To Update DAC Fast, set to 50 % of reference voltage     
sudo ./runMcp4725.sh -b 0x01 -a 0x62 -d 2047 -vref 3.3 -pv false



Updates using voltage value

To Update DAC Fast, set to 1.8v of reference voltage     
sudo ./runMcp4725.sh -b 0x01 -a 0x62 -v 1.8 -vref 3.3  -pv false

To Update DAC and EEPROM, set to 3.3v
sudo ./runMcp4725.sh -b 0x01 -a 0x62 -v 3.3 -vref 3.3  -pv true

To Update DAC Fast, reset chip,  set to 1.8     
sudo ./runMcp4725.sh -b 0x01 -a 0x62 -v 1.8 -r -vref 3.3  -pv false





Test program.  Loop indefinitely executing the shell command

./runLoop.sh  program.sh args ......

Example usage:
./runLoop.sh ./runMcp3008.sh -vref 3.3 -p 0x0