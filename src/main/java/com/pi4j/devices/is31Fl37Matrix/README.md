Implementation of the Is31fl3731 matrix controller.  
Uses a BME280 and system time to alternately display the temperature and time.

Note: Shutdown pin SDB not implemented

The program has the tables to translate numbers to matrix commands, it does not
translate alphabetic characters.

1. ./mvnw clean package
2. cd target/distribution
3. Execute command to perform desired Matrix operation

// Test board
./runIs31fl37Matrix.sh -b 0x01 -a 0x74 -bmpB 0x01 -bmpA 0x76 -g 24 -w 20 -i 128 -c 1 -l 1 -r 16 -z 12 -t off

// Lab display device
./runIs31fl37Matrix.sh -b 0x1 -a 0x74 -bmpB 0x1 -bmpA 0x76 -g 23 -w 24 -i 128 -c 0 -l 1 -r 19 -z 21 -t off

H3 white gpio17   
H4 grey gpio19 -r
E1 red gpio21 -z
E2 blue SDA
E3 purple SCL
E4 yellow gpio 24 -w
E5 green gpio23 -g
J side 5v
A side 3.3v
B side grnd

GPIO pin

x -g ----------->  processing indicator LED

x -w ------------>  warning indicator LED

x -r ------------------------------------------->   Is31fl3731 SD
--

x -z    <------------------------------------------- Is31fl3731 INTB

x I2C -b bus -a address ----------------------- Is31fl3731

x I2C -bmpB bus -bmpA address ------------------ BMP280

Debug utilities


// Lab display uses the following GPIOs
// Special regression test. Only displays time. Bogus but valid address passed on for -bmpA

./runIs31fl37MatrixTest.sh -b 0x1 -a 0x74 -bmpB 0x1 -bmpA 0x76 -g 23 -w 24 -i 128 -c 2 -l 1 -r 19 -z 21 -t off
