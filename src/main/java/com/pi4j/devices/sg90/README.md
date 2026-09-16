#  SG90  servo motor

https://www.scribd.com/document/420327552/Servo
This device does not have a customary DataSheet


 The SG90 defines a specific PWM interface and exactly how the PWN signal
 controls the servo

  https://www.friendlywire.com/projects/ne555-servo-safe/SG90-datasheet.pdf

  SG90 Duty Cycle Reference TableAssuming a standard 50Hz frequency (20ms total wave period),
  here is how the pulse width and duty cycle map to standard angles for the SG90 Servo Motor Guide:
  |  Angle (Degrees)  |  Pulse Width (Us)     |        Duty Cycle (%)     |
  |-----------------------------------------------------------------------|
  |     0             |      0.5 ms           |         2.5 %             |
  |     45            |      1.0 ms           |         5.0 %             |
  |     90            |      1.5 ms           |         7.5 %             |
  |-----------------------------------------------------------------------|




  SG90                Pi
Grey/Brown          Ground
Red                 5V
Yellow              GPIO18  config.sys contains dtoverlay=pwm-2chan



1. ./mvnw clean package
2. cd target/distribution
3. ./runSG90.sh   args

-h  channel   (default 2)   set the channel, depending on config.txt 1 2 3 4  
-d  degree    (default 90) 


Set ouput shaft at 120 degree

./runSG90.sh   -d 120     



If your configuration uses a channel other than 2
./runSG90.sh -c 1 -d 120     
