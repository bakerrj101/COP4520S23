import java.util.Random;
import java.util.concurrent.*;

public class VaseQueue {
    // NUMWORKERS symbolizes the amount of available processors
    // NUMJOBS symbolizes the amount of guests
    // This implementation allows for large numbers of guests on low power computers
    private static final int NUMWORKERS = 8;
    private static final int NUMJOBS = 100;
    private static final BlockingQueue<Job> jobs = new ArrayBlockingQueue<>(NUMJOBS);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(NUMWORKERS);
    private final Random rand = new Random();

    public void queue() throws InterruptedException {

        // jobs is our guest queue, executorService handles assigning threads jobs
        for (int i = 0; i < NUMJOBS; i++) {
            jobs.put(new Job(i + 1));
        }
        for (int i = 0; i < NUMWORKERS; i++) {
            executorService.submit(new Worker());
        }

        // Let all threads finish
        // If not finished within 5 seconds, force shutdown
        executorService.shutdown();
        if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
    }

    private class Job {
        private int id;

        public Job(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    private class Worker implements Runnable {

        @Override
        public void run() {
            while (true) {
                //  System.out.println(executorService);
                Job job;
                // If there are no more guests who want to view the vase, the thread can shut down
                if (jobs.isEmpty()) {
                    return;
                }
                try {
                    // Take job for guest who wants to view vase
                    job = jobs.take();
                    // View the vase
                    System.out.println("guest " + job.getId() + " viewed the vase");
                    // Decide if guest wants to view the vase again (50% chance)
                    if (rand.nextBoolean()) {
                        // If so, get back in the queue
                        jobs.put(job);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
