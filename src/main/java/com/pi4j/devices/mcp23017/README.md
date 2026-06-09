Pi4J :: Java I/O Library for Raspberry Pi :: Device :: Mcp23017
==========================================================================

### This repository is a first device specific support project for the MCP23017

### This is an example implementation and will need to be adjusted to fit your needs.

Project by Tom Aarts


# This device example os being rewritten to simplify and use the associate Driver

==========================================================================

https://ww1.microchip.com/downloads/en/devicedoc/20001952c.pdf
MCP23017/MCP23S17
16-Bit I/O Expander with Serial Interface

Java classes to access the MCP23017 GPIO controller as an application.

Supported functions.

1. reset chip
2. Configure any/all pins (all configurations options)
3.   Example does not use the MCp23017 interrupt capability. This will be added later

Program options
-r read this MCP23xxx pin
-d drive this MCP23xxx pin based on -o ON or OFF

    1. ./mvnw clean package
    2. cd target/distribution
    3. Execute command to reset Mcp23017
    5. Execute command to perform desired MCP23017 operation


BCM gpio12 configured as output connected MCP23017 pin 4 spdip25

BCM gpio18 configured as output connected MCP23017 pin 15 spdip8

BCM gpio5 configured as output connected MCP23017 (bar) RESET spdip18

BCM gpio23 configured as input connected MCP23017 INTA spdip20

BCM gpio21 configured as input connected MCP23017 INTB spdip19

BCM gpio22 configured as output connected to LED

Red LED (+) connected to pin0 spdip21
Green LED (+) connected to pin9 spdip2
Yellow LED (+) connected to pin14 spdip7

All address pins (A0 A2) are strapped to ground, (A1) strapped to 3.3 V for the chip address 0x22

``` 
- Pi BCM I2C bus 1 -  _______________
  _______________________                 |
  | | || | |
  | | || | |
  | | || | |  
  | | || | |
  | | || |                      ____|________________
  | | || |__________> RESET >   - MCP23017 0x22 -
  | | ||_______________< INTA  <   _____________________
  | | |________________ < INTB <      | | | | |
  | |___________________> Drive GPIO > | | LEDs
  |_____________________> Drive GPIO >___|
```


1. Reset MCP23017
   sudo ./runMcp23017.sh -b 0x01 -a 0x22 -r 4 

2. Drive pin0 hi low. Drives Red LED.

sudo ./runMcp23017.sh -b 0x01 -a 0x20 -d 0 -o ON 
sudo ./runMcp23017.sh -b 0x01 -a 0x20 -d 0 -o OFF 

3. Drive pin14 hi low. Drives Yellow LED.

sudo ./runMcp23017.sh -b 0x01 -a 0x22 -d 14 -o ON
sudo ./runMcp23017.sh -b 0x01 -a 0x22 -d 14 -o OFF

4. Read pin4
   sudo ./runMcp23017.sh -b 0x01 -a 0x22 -r 4   

In separate terminal, alter pin4
python3
import RPi.GPIO as GPIO
GPIO.setmode(GPIO.BCM)
GPIO.setup(12, GPIO.OUT)
GPIO.output(12,GPIO.LOW)
GPIO.output( 12 , GPIO.HIGH)

6. Read pin 15
   sudo ./runMcp23017.sh -b 0x01 -a 0x22 -r 15 
   sudo ./runMcp23017.sh -b 0x01 -a 0x22 -r 15 -f 1

In separate terminal, alter pin15
python3
import RPi.GPIO as GPIO
GPIO.setmode(GPIO.BCM)
GPIO.setup(18, GPIO.OUT)
GPIO.output(18,GPIO.LOW)
GPIO.output( 18 , GPIO.HIGH)



