# ParallelPrimeSieve
This program utilizes the Seive of Eratosthenes to calculate primes from 1 to 10^8.
The program increments until it finds a non-composite number and saves it. It then marks all of the multiples of this number as composite.
I split this task into smaller jobs by dividing the array into chunks for each job to handle marking composites.
These jobs are then queued up and assigned to available threads.
I utilized the Java libraries BlockingQueue and ExecutorService to handle this process.

The program only needs do this process for primes that are less than or equal to the square root of the upper bound due to the sieve's properties.
Afterwards, the remaining primes are gathered sequentially and the requested output is written to primes.txt.

If the user wants to change the number of threads available and the upper bound of the primes (within integer size), they can do so in Main.java.

This approach runs in around 0.7 seconds on my personal computer. Each job should be of a similar size, and 
each thread should be taking on a roughly equivalent amount of jobs. 
The time complexity of the Sieve of Eratosthenes is O(nlog(log(n))), but this is reduced by our multithreading.
This program therefore seems reasonably efficient for our needs.

### How to Compile and Run Using Command Prompt
1. Install the JDK (Java Development Kit)
2. Open the command prompt and navigate to C:\...\ParallelPrimeSieve\src based on where you saved the project on your computer.
3. Run the command "javac PrimeSieve.java Main.java" (without the quotes) to compile the program.
4. Run the command "java Main" (without the quotes) to run the program.
5. The output file "primes.txt" will be found in the C:\...\ParallelPrimeSieve\src folder.
