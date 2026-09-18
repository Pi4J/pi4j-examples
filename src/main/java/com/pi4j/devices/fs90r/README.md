

##Device application to operate a FS90R stepping servo.  This servo can rotate 360 degree continuously. 
The PWM signal sent to the servo device set the direction and RPM of rotation.





 The Fs90R defines a specific PWM interface and exactly how the PWN signal
 controls the servo

 https://www.pololu.com/product/2820

  FS90R Duty Cycle Reference TableAssuming a standard 50Hz frequency
  (20ms total wave period),  here is how the pulse width and duty cycle
  map to direction and rotation speed for the FS90R Servo Motor:
  |  Direction Rotation       |  Pulse Width (us)  |    Duty Cycle (%)    |
  |-----------------------------------------------------------------------|
  |   clockwise max           |      0.5 us        |     2.5 %            |
  |   stopped                 |      1.5 us        |     7.5 %            |
  |   counter clockwise max   |      2.5 us        |     12.5 %           |
  |-----------------------------------------------------------------------|



FS90R               Pi
Grey/Brown          Ground
Red                 5V
Yellow              GPIO18  config.sys contains dtoverlay=pwm-2chan



1. ./mvnw clean package
2. cd target/distribution
3. ./runFS90R.sh   args

-h    help
-c    channel   (default 2)   set the channel, depending on config.txt 1 2 3 4  
-d    degree   
-cw   clockwise S M F
-ccw  counter-clockwise S M F
-q    quit      servo will stop

Set output shaft stationary
./runFS90R.sh   -d 90     

Set output shaft max rotation Counter Clockwise
./runFS90R.sh   -d 180     

Set output shaft max Clockwise
./runFS90R.sh   -d 0     


Set output shaft max Clockwise
./runFS90R.sh   -cw F     

Set output shaft slow Counter-Clockwise
./runFS90R.sh   -ccw S     
