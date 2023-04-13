import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    static final int NUMPRESENTS = 500000;

    public static void main(String[] args) {
        // Create unordered present bag
        Random rand = new Random();
        Set<Integer> bag = new LinkedHashSet<>();
        while (bag.size() < NUMPRESENTS) {
            Integer next = rand.nextInt(NUMPRESENTS);
            bag.add(next);
        }

        PresentSort p = new PresentSort();
        try {
            long startTime = System.currentTimeMillis();

            int numCards = p.sort(bag.iterator());

            long endTime = System.currentTimeMillis();
            long runtime = endTime - startTime;

            System.out.println(bag.size() + " presents were in the bag");
            System.out.println("The workers created " + numCards + " cards");
            System.out.println("runtime: " + runtime + " ms");

        } catch (InterruptedException e) {
            System.out.println("Program interrupted");
        }
    }
}