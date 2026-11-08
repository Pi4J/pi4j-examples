# Pi4J_V2-TemperatureSensor

Implement Temperature/Pressure BMP280

https://www.digikey.com/en/datasheets/bosch-sensortec/bosch-sensortec-bst-bmp280-ds001-19

The I2C example program has the assumption the BMP280 is connected to Pi bus 1, and the device is factory configured to
operate as device address 0x77. If these assumptions are not possible, the bus and device address value can be passed
as parms. Or modify the BMP280I2cExample program to use your specific bus and device address.

The SPI example assumes SPI bus 0, ChipSelect 0.


The I2C connection functions as written in the Phillips spec.


I2C
1. ./mvnw clean package
2. cd target/distribution
3. sudo ./runBMP280I2C.sh
   Args    
       -bus  
       -address  
       -       application will start and wait for input   

   sudo ./runBMP280I2C.sh -bus 1 -address 0x77   -              ( or -address 119)
 
Commands
| Input   |                               |
|---------|-------------------------------|
| read    | read sensor and print results |
| help    | display help text              |
| exit    | program exit                  |
| reset   | reset chip                    |


The SPI connection per the Datasheet supports MODE0 or MODE3.

SPI 
1. ./mvnw clean package
2. cd target/distribution
3. sudo ./runBMP280SPI.sh  args

  Args 
    -chipselect 
    -bus    0..6
    -       application will start and wait for input


sudo ./runBMP280SPI.sh -bus 0 -chipselect 0   -              ( or -address 119)

Commands
| Input   |                               |
|---------|-------------------------------|
| read    | read sensor and print results |
| help    | display help text              |
| exit    | program exit                  |
| reset   | reset chip                    |

Will create the BMP280 device driver then Call the various temperature and pressure methods
defined in the interface. 


The file BMP280.pdf documents a method of connecting an Adafruit BMP280 to a Pi.

I2C connection

| RPi        | Color  | Sensor |
|:-----------|:-------|:-------|
| Pin1  3.3V | 	Red   | Vin    |
| N/C        |        | 3v	    |
| Pin6  Gnd  | Brown  | Gnd    |
| Pin5  SCL  | Green  | SCK    |
| N/C        | Yellow | SDO    |
| Pin3  SDA  | Blue   | SDI    |
| Pin17 3.3v | Orange | CS     |

SPI connection using 4-wire SPI

| RPi          | Color  | Sensor |
|:-------------|:-------|:-------|
| Pin19 MOSI   | Blue   | SDI    |
| Pin21 MISO   | Yellow | SDO    |
| Pin23 SCLK   | Green  | SCK    |
| Pin24 SPICE0 | Orange | CS     |
| Pin6  Gnd    | Brown  | GND    |
| Pin1  3.3V   | Red    | Vin    |


