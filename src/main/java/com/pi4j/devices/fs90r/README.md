##Device application to operate a Fs90r stepping servo. This servo can rotate 360 degree continuously.
The PWM signal sent to the servo device set the direction and RPM of rotation.

Driver for a Fs90r servo motor.
The Fs90r defines a specific PWM interface and exactly how the PWN signal
controls the servo

https://www.pololu.com/product/2820

These devices do not have a customary DataSheet. Most servo devices provide limited specifications
for required pulse width, they just say 1 - 2 millisecond.

The FS90 R 360 defines a specific PWM interface and exactly how the PWN signal
controls the servo. There are many different manufacturers of the FS90 servo.
The pulse width required for setting the servo at 0 degrees MAX Clockwise (CW) or 180 degrees
MAX CounterClockWise (CCW) varies by manufacturer (Note 1). This implementation use SE determination.
The driver by default uses the pulse width timing as (low)1000 - (high)2000 microseconds. The user of
this example application can change these values

If the servo does not rotate to the expected RPM,Example to rotate faster CW reduce the -low
argument, to rotate CCW faster increase the -high argument. The low high commands change should be balanced
or 90 degree will not properly align. Meaning if you reduce the low by 200, you should increase the high by 200,
or visa-versa.

Note 1  :Direction of rotation, the definition of CW and CCW has two distinct versions. These define your
perspective of observation.

- Shaft End (SE / Drive End): The end where the motor shaft and load (like a fan blade or pulley) attach. Industry
  convention usually assumes you are looking straight at the shaft end.
- Lead End (LE / Bell End / Opposite Drive End - ODE): The back side of the motor where the electrical wires or leads
  exit.

https://www.pololu.com/product/2820

Fs90r Pi
Grey/Brown Ground
Red 5V
Yellow GPIO18 config.sys contains dtoverlay=pwm-2chan

1. ./mvnw clean package
2. cd target/distribution
3. ./runFs90r.sh args

-c channel   (default 2)   set the channel, depending on config.txt 1 2 3 4

-       Program will contunue to run, the following commands are possible

Commands

direction cw/ccw set servo direction of rotation
percent double Set to this percentage of max rotation
degree double degree to set the servo
low double pulse width for 0 degree microseconds
high double pulse width for 180 degree microseconds
x quit servo will stop, program exist
h help Print commands and their range of input values

./runFs90r.sh - application running, awaiting command input

./run*Fs90r.sh -c 1 PWM device will use channel 1

./runFs90r.sh -c 1 - PWM device will use channel 1, application running, awaiting command input

Command direction validates the servo is stopped before permitting changing direction

Command degree
Setting rotation by degree assumes the user understands the spec
and does NOT test the servo being stopped nor that the degree range
coincides with the present direction of rotation.

My Fs90r appears faster rotation wih these values for the low and high limmits
low 550 high 2450

Follow
start app

./runFs90r.sh - Start app in mode to accept commands
low 550 Provided best performance in my test setup
high 2450 Provided best performance in my test setup
direction cw clockwise
percent 75 75% cw rotation speed
direction ccw exception as servo is rotating cw
percent 0 stop rotation
direction ccw counterclockwise
percent 50 50% ccw rotation speed
degree 145 automatic change ccw speed of rotation based on degree
x exit

