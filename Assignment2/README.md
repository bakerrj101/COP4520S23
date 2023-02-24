# Assignment 2
These are my solutions for the two problems presented in Assignment 2.
Neither program requires an input. 

### MinotaurMaze
This algorithm should satisfy the requirement that all guests visit the maze at least once before announcing their success.

If a guest finds the cupcake on the plate and they have not eaten a cupcake yet, they will eat the cupcake.
One guest, who I tracked as guest ID 0, is in charge of counting how many cupcakes have been eaten. They, and only they, will replace the cupcake if it is gone when they enter the maze. They will increment a counter every time they enter the maze and there is no cupcake. When the counter equals the number of guests, the announcement can be made.

The code tracks this counter as a static, but keep in mind that in real life it would be this one guest keeping track of it. The static implementation still follows the rules while being easier to work with.

### MinotaurVase
We chose between 3 different implementation methods.

Method 1 is very uncontrolled and can result in either very fast or very slow runtimes depending on luck.

Method 2 uses a basic lock to prevent guests from trying to enter when the room is unavailable. However, it does not handle which guests get priority in entering once the space becomes available. This can result in varying runtimes.

Method 3 uses a queue to control when each guest will enter the room. This is the most consistent solution to the problem, but it does not have the speed potential of the other methods. It also struggles when a single guest queues up multiple times and wastes runtime.

I chose to implement Method 3 as I am already familiar with using queues to handle threads.

### How to Compile and Run Using Command Prompt
1. Install the JDK (Java Development Kit)
2. Open the command prompt and navigate to C:\...\MinotaurMaze\src or C:\...\MinotaurVase\src based on where you saved the project on your computer.
3. Run the command "javac Maze.java Main.java" (without the quotes) to compile MinotaurMaze.
   Run the command "javac VaseQueue.java Main.java" (without the quotes) to compile MinotaurVase.
5. Run the command "java Main" (without the quotes) to run the program.
6. The output will be recieved in the standard output.
