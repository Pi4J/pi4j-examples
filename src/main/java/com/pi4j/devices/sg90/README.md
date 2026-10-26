##Device application to operate a Fs90r servo. This servo can rotate 360 degree continuously. The PWM signals   
pulse width being sent to the servo device determines the direction and RPM of rotation.  

https://www.pololu.com/product/2820

These devices do not have a customary DataSheet. Most servo devices provide limited specifications for required pulse widths,  
they just say 1 - 2 millisecond.

The FS90 R 360 defines a specific PWM interface and exactly how the PWN signal controls the servo. There are many  
different manufacturers of the FS90 servo. The pulse width required for setting the servo at MAX Clockwise (CW)  
or MAX CounterClockWise (CCW) varies by manufacturer  This implementation use SE determination for the  
direction of rotation  (Note1).     

The driver by default uses the pulse width timing as (low)1000 - (high)2000 microseconds. The user of this example    
application can change these values. If the servo does not rotate to the expected RPM, to rotate faster clockwise    
reduce the low argument, to rotate CCW faster increase the high argument. The low high commands change should be balanced      
or else when the servo is stopped will not be correct. Meaning if you reduce the low by 200, you should increase  
the high by 200, or visa-versa.  

Note 1 :Direction of rotation, the definition of clockwise and counterclockwise have two distinct versions. These define  
your perspective of observation.  
1. Shaft End (SE / Drive End): The end where the motor shaft and load (like a fan blade or pulley) attach. Industry  
convention usually assumes you are looking straight at the shaft end.  
2. Lead End (LE / Bell End / Opposite Drive End - ODE): The back side of the motor where the electrical wires or leads exit.  

https://www.pololu.com/product/2820

Typical wiring  when config.sys  contains dtoverlay=pwm-2chan       
FS90R connections to a Raspberry Pi   

| FS90_R     | Raspberry Pi |
|------------|--------------|
| Grey/Brown | Ground       |  
| Red        | 5V           | 
| Yellow     | GPIO18 PWM   | 



Building the Example application  
./mvnw clean package  
cd target/distribution  
./runFs90r.sh args  
-c channel (application default is 2) set the channel, depending on config.txt 1 2 3 4  

If **-** is included when starting this program, it will continue to run and the following commands are possible  
      
| Command    | options            | Note                                 |
|------------|--------------------|--------------------------------------|
| direction  | cw/ccw             | set servo direction of rotation      |  
| percent    | double 0 ... 100   | set RPM                              |
| low        | double 500 ...1499 | pulse width for max clockwise        |   
| high       | double 1501..2500  | pulse width for max counterclockwise |  
| x          |                    | servo will stop, program exist       |   
| h          |                    | print help                           |   





./runFs90r.sh -        application running, awaiting command input  

./run*Fs90r.sh -c 1    PWM device will use channel 1  

./runFs90r.sh -c 1 -   PWM device will use channel 1, application running, awaiting command input  

Command examples

My Fs90r appears faster rotation wih these values for the low and high values low 550 high 2450  

**./runFs90r.sh -**       Start app in mode to accept commands  
                                                                               
                                                                                        
| command        | Note                                                          |      
|----------------|---------------------------------------------------------------|         
| low  550       | Provided best performance in my test setup                    |   
| high 2450      | Provided best performance in my test setup                    | 
| direction cw   | clockwise                                                     |   
| percent 75     | 75% clockwise rotation speed                                  |   
| direction ccw  | exception as servo is currently rotating clockwise            |   
| percent 0      | stop rotation                                                 |   
| direction ccw  | change to counterclockwise permitted as servo is not rotating |  
| percent 50     | 50% counterclockwise rotation speed                           |  
| x              | exit                                                          |  

