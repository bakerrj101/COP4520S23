# Assignment 3
These are my solutions for the two problems presented in Assignment 3.
Neither program requires an input. 

### MinotaurPresents
I implemented a concurrent singly linked list that locks and unlocks individual nodes to prevent multiple threads from accessing the same data without locking the whole list.
While the number of cards was less than the number of presents, threads would perform one of 3 randomly chosen jobs on each loop:
1. Pull a present from the unsorted bag and insert it into the sorted list
2. Remove a present from the list and write a card
3. Tell the minotaur if a certain present is in the list or not
I used locks for the bag and the card count to prevent multiple threads from accessing them at the same time.
The program will print the number of presents and cards as well as the runtime in ms to the standard output.

### TemperatureSensor
This program has 8 threads that each simulate taking a temperature reading once a minute. Each thread must finish its check for that minute before all threads move on to the next minute. 
At the end of each hour, the first thread (id 0) compiles a report and prints it to the standard output. A lock is used to prevent other threads from editing the previous hour's data prematurely.
At the end of the simulation, the program will print its runtime in ms to the standard output.

### How to Compile and Run Using Command Prompt
1. Install the JDK (Java Development Kit)
2. Open the command prompt and navigate to C:\...\MinotaurPresents\src or C:\...\TemperatureSensor\src based on where you saved the project on your computer.
3. Run the command "javac PresentLinkedList.java PresentSort.java Main.java" (without the quotes) to compile MinotaurPresents.
   Run the command "javac Simulation.java Main.java" (without the quotes) to compile TemperatureSensor.
5. Run the command "java Main" (without the quotes) to run the program.
6. The output will be recieved in the standard output.
