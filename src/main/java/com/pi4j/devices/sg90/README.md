##Device application to operate a SG90 servo. This servo can rotate 180.  The PWM signals   
pulse width being sent to the servo device determines to what position the armature rotates.  

https://www.pololu.com/product/2820

These devices do not have a customary DataSheet. Most servo devices provide limited specifications for required pulse widths,  
they just say 1 - 2 millisecond.

The SG90  defines a specific PWM interface and exactly how the PWN signal controls the servo. There are many  
different manufacturers of the SG90 servo. The pulse width required for setting the servo at MAX Clockwise (CW)  
or MAX CounterClockWise (CCW) varies by manufacturer  This implementation use SE determination for the  
direction of rotation  (Note1).     

The driver by default uses the pulse width timing as (minimalpulsewidth)1000 - (maximumpulsewidth)2000 microseconds. The user of this example    
application can change these values. If the servo does not stop at the expected position, to rotate further clockwise    
reduce the minimalpulsewidth argument, to rotate counterclockwise increase the maximumpulsewidth argument. These changes should be balanced      
or else when the servo is at 90&deg; the position will not be correct. Meaning if you reduce the minimalpulsewidth by 200, you should increase  
the maximumpulsewidth by 200, or visa-versa.  

Note 1 :Direction of movement, the definition of clockwise and counterclockwise have two distinct versions. These define  
your perspective of observation.  
1. Shaft End (SE / Drive End): The end where the motor shaft and load (like a fan blade or pulley) attach. Industry  
convention usually assumes you are looking straight at the shaft end.  
2. Lead End (LE / Bell End / Opposite Drive End - ODE): The back side of the motor where the electrical wires or leads exit.  


Typical wiring  when config.sys  contains dtoverlay=pwm-2chan       
SG90 connections to a Raspberry Pi   

| SG90       | Raspberry Pi |
|------------|--------------|
| Grey/Brown | Ground       |  
| Red        | 5V           | 
| Yellow     | GPIO18 PWM   | 



Building the Example application  
./mvnw clean package  
cd target/distribution  
./runSg90.sh args  
-c channel (application default is 2) set the channel, depending on config.txt 1 2 3 4  
**-**  when starting this program, it will continue to run and the following commands are possible  
      
| Command           | options             | Note                                 |
|-------------------|---------------------|--------------------------------------|
| degree            | double 0 ... 180    | set armature position                |
| minimalpulsewidth | double 500 ...1499  | pulse width for max clockwise        |   
| maximumpulsewidth | double 1501..2500   | pulse width for max counterclockwise |  
| exit              |                     | servo will stop, program exist       |   
| help              |                     | print help                           |   





./runSg90.sh -        application running, awaiting command input  

./run*Sg90.sh -channel 1    PWM device will use channel 1  

./runSg90.sh -channel 1 -   PWM device will use channel 1, application running, awaiting command input  

Command examples

My Sg90 appears faster rotation wih these values minimalpulsewidth 550 maximumpulsewidth 2450  

**./runSg90.sh -**       Start app in mode to accept commands  
                                                                               
                                                                                        
| command                | Note                                                          |      
|------------------------|---------------------------------------------------------------|         
| minimalpulsewidth  550 | Provided best performance in my test setup                    |   
| maximumpulsewidth 2450 | Provided best performance in my test setup                    | 
| direction cw           | clockwise                                                     |   
| percent 75             | 75% clockwise rotation speed                                  |   
| direction ccw          | exception as servo is currently rotating clockwise            |   
| percent 0              | stop rotation                                                 |   
| direction ccw          | change to counterclockwise permitted as servo is not rotating |  
| percent 50             | 50% counterclockwise rotation speed                           |  
| exit                   | exit                                                          |  

