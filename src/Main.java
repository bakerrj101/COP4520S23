import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Magic numbers, change to change output
        int upperBound = (int) Math.pow(10, 8);
        int numPrimesToPrint = 10;

        // Build and execute prime sieve that returns list of primes
        PrimeSieve sieve = new PrimeSieve(upperBound);
        List<Integer> primes;
        long start = System.currentTimeMillis();
        try {
            primes = sieve.runPrimeSieve();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        long end = System.currentTimeMillis();
        long runtime = end - start;
        BigInteger primesSum = BigInteger.ZERO;
        for (int i = 0; i < primes.size(); i++) {
            primesSum = primesSum.add(BigInteger.valueOf(primes.get(i)));
        }

        // Threads are running concurrently, not sequentially
        // Sorting list is needed to print largest 10 numbers in order
        Collections.sort(primes);

        // Print required info to "primes.txt"
        PrintWriter writer;
        try {
            writer = new PrintWriter("primes.txt", StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writer.println(runtime + " " + primes.size() + " " + primesSum);
        for (int i = numPrimesToPrint; i > 0; i--) {
            writer.print(primes.get(primes.size()-i));
            if (i > 1) {
                writer.print(" ");
            }
        }
        writer.close();
    }
}
