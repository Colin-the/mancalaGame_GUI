# Project Title

Board Game Mancala with a GUI. This project was done in Java for CIS*2430 (object-oriented programming) at the University of Guelph and is an extension of the text-based version.

## Description

This program is a Java implementation of the Mancala board game. It consists of classes representing the pits, stores, players, and the board. The game logic is implemented in the textUI file and handles player moves, stone distribution, capturing opponent stones, and determining the end of the game. This program includes exception handling to manage invalid moves and pit accesses. There is also a GUI built on top of all of this to make the user experience better

## Getting Started
Make sure you are in the top-level directory and then see the Executing program from there

### Dependencies

java,  java.util.ArrayList,  org.junit.jupiter.api.BeforeEach, org.junit.jupiter.api.Test, static org.junit.jupiter.api.Assertions.*,
java.util.Scanner, java.util.List, java.awt, javax.swing.*, java.io.*

### Executing program

gradle build
gradle echo 
    > Task :echo
    To run the program from jar:
    java -jar build/libs/Mancala.jar
    To run the program from class files:
    java -cp build/classes/java/main ui.TextUI

Then go to build/libs/ and open Mancala.jar
from there save it to your downloads folder
then open up a new terminal on your pc (PowerShell works for Windows)
java -jar Downloads/mancalaGame.jar

Expected output
A window with the GUI will pop up

## Limitations

The save feature does not fully work
the save and load functions both works but there is an error somwhere in the UI class that is not setting up the board from the save

## Author Information
Colin Campbell
1235313
ccampb47@uoguelph.ca

## Development History

*04
    *Added a GUI to replace the old text UI
* 0.3
    * Fixed text UI class so it outputs and loops correctly
* 0.2
    * Various bug fixes and optimizations
    * Added missing methods to board and mancala game classes
* 0.1
    * Used Chat GPT 3.5 to make the AI version of the assignment

## Acknowledgments

Inspiration, code snippets, etc.
*Chat GPT 3.5 was used for the AI portion of this assignment



