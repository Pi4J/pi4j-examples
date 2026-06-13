Pi4J :: Java I/O Library for Raspberry Pi :: Device :: Mcp23008
==========================================================================

### This repository is a first device specific support project for the MCP23008

### This is an example implementation and will need to be adjusted to fit your needs.

Project by Tom Aarts

==========================================================================

# This device example os being rewritten to simplify and use the associate Driver
MCP23008/MCP23S08 8-Bit I/O Expander with Serial I2C Interface

Java classes to access the MCP23008 GPIO controller as an application.

Supported functions.


   Example does not use the MCp23008 interrupt capability. That will be added at a later time

    1. ./mvnw clean package
    2. cd target/distribution 
    3. Execute command to reset Mcp23008
    4. Execute command to perform desired MCP23008 operation


BCM gpio16 configured as output connected MCP23008 pin 4 pdip14

BCM gpio13 configured as output connected MCP23008 (bar) RESET pdip6

BCM gpio27 configured as input connected MCP23008 INT pdip8

BCM gpio22 configured as output connected to LED

Red LED (+) connected to pin0 pdip10
Green LED (+) connected to pin1 pdip11
Yellow LED (+) connected to pin2 pdip12

MCP23008 on Pi BCM 1 address 0x20
All address pins (A0 A1 A2) are strapped to ground for the chips default address 0x20

_______________________           

```
- Pi BCM I2C bus 1 - ______________
  _______________________               |
  | | | |
  | | | |
  | | | |  
  | | | |
  | | |                       ____________________
  | | |__________> RESET >   - MCP23008 0x20 -
  | |                             ____________________
  | |________________ < INT  <        | | | |
  | | LEDs
  |_____________________> Drive GPIO >___|
```

1. Drive MCP23008 pin0 Red Led hi low
   sudo ./runMcp23008.sh -b 0x1 -a 0x20 -d 0 -o ON -m   
   sudo ./runMcp23008.sh -b 0x1 -a 0x20 -d 0 -o OFF

2. Read MCP23008 pin4
   Read 4
   sudo ./runMcp23008.sh -b 0x1 -a 0x20 -r 4 
   This will set pin4 high or low
   python3
   import RPi.GPIO as GPIO
   GPIO.setmode(GPIO.BCM)
   GPIO.setup(16, GPIO.OUT)
   GPIO.output(16,GPIO.LOW)
   GPIO.output( 16 , GPIO.HIGH)

