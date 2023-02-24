import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Maze {
    // NUMWORKERS symbolizes the amount of available processors
    // NUMJOBS symbolizes the amount of guests
    // This implementation allows for large numbers of guests on low power computers
    private static final int NUMWORKERS = 8;
    private static final int NUMJOBS = 100;
    private static final BlockingQueue<Job> jobs = new ArrayBlockingQueue<>(NUMJOBS);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(NUMWORKERS);
    private final Random rand = new Random();

    private static boolean[] eaten = new boolean[NUMJOBS];
    private static boolean cakeState;
    private static int counter;

    private static Lock mazeLock;
    private static Lock cakeLock;

    public void party() throws InterruptedException {
        mazeLock = new ReentrantLock();
        cakeLock = new ReentrantLock();
        cakeState = true;
        counter = 0;

        for (int i = 0; i < NUMWORKERS; i++) {
            executorService.submit(new Worker());
        }

        while (counter < NUMJOBS) {
            // Prevents queue overflow
            if (!(jobs.size() >= NUMJOBS)) {
                int id = rand.nextInt(NUMJOBS);
                jobs.add(new Job(id));
            }
        }

        // Let all threads finish
        // If not finished within 5 seconds, force shutdown
        executorService.shutdown();
        if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
    }

    private class Job {
        // Guest ID 0 is the designated counter, other guests ignore it
        int id;

        public Job(int id) {
            this.id = id;
        }
    }

    private class Worker implements Runnable {

        @Override
        public void run() {
            while (counter < NUMJOBS) {
                Job job;
                // Hang while waiting on jobs if necessary
                if (jobs.isEmpty()) {
                    continue;
                }
                mazeLock.lock();
                try {
                    // Take job for guest entering maze
                    job = jobs.take();
                    // If designated counter (ID 0) sees that cake is gone, get new cake and increment counter
                    if (!cakeState && job.id == 0) {
                        cakeLock.lock();
                        cakeState = true;
                        counter++;
                        // System.out.println("counter incremented to " + counter);
                        cakeLock.unlock();
                    }
                    // If cake is present and guest has not eaten, guest will eat
                    if (cakeState && !eaten[job.id]) {
                        // System.out.println("guest " + job.id + " is eating for the first time.");
                        cakeState = false;
                        eaten[job.id] = true;
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                mazeLock.unlock();
            }
        }
    }
}
