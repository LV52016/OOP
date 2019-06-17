# Java-Arduino Communication Project

Made by Luka Vinkesevic in 2019. for Object Oriented Programming class
from University of Zagreb, Faculty of Electrical Engineering and Computing.

### Description
This project was made in C and Java using the [jSerialComm](https://github.com/Fazecast/jSerialComm) library.
The goal of the project is to make communicating with the Arduino board easier and more graphical.

### Features
Everything is achieved using GUI, no coding knowledge is required.

* gives control over digital input/output pins 
* up to 6 analog outputs (Arduino: PWM pins)
* up to 6 analog inputs (Arduino: A0-A5)
* analog I/Os can also be used as digital
* graphing up to 2 different analog inputs
* saving graphs as .png image

### Usage
1. Upload the code from [code/code.ino](https://github.com/LV52016/OOP/blob/master/code/code.ino) to your
Arduino board (tested only with Arduino UNO board, but should also work with other boards).
2. Run the java program (your Arduino board must be connected to your PC via USB).
3. Select the appropriate port for your Arduino board (if there are no available ports, click the Rescan button first) and click Connect.
4. Now you can set each pin to be used as an input or output (lower middle section).
5. After that you can control each digital pin with toggle buttons (upper middle section), or set the analog output values.

#### To make a graph and save it as .png:
1. Enable one or both channels (center of the middle section).
2. Select which analog pin(s) you want to record.
3. Adjust 'Fill graph', 'Show grid' and 'Grid on top' options as you wish.
4. You can also change the timebase right below the graph.
5. Choose a path where you wish to save your graph (default is your Desktop).
6. Press 'Save graph' and you're done!