import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class PrimeSieve {
    private static final int NUMWORKERS = 8;
    private static boolean[] isComposite;

    private BlockingQueue<Job> jobs = new ArrayBlockingQueue<>(NUMWORKERS);
    private ExecutorService executorService = Executors.newFixedThreadPool(NUMWORKERS);

    private int upperBound;

    public PrimeSieve (int upperBound) {
        this.upperBound = upperBound;
    }

    public List<Integer> runPrimeSieve() throws InterruptedException {
        int upperBoundSqrt = (int)Math.sqrt(upperBound);
        isComposite = new boolean[upperBound + 1];
        List<Integer> primes = new ArrayList<>();

        // Set up NUMWORKERS threads
        for (int i = 0; i < NUMWORKERS; i++) {
            executorService.submit(new Worker());
        }
        // Find all necessary primes for range
        for (int i = 2; i <= upperBoundSqrt; i++) {
            if (!isComposite[i]) {
                primes.add(i);

                // Make chunks to divide the isComposite array into
                // Each chunk must start at a multiple of the current prime for the thread to compute correctly
                // Some overlap due to this, but it is minimized
                int chunkSize = (int) Math.ceil((double)upperBound/ (double)NUMWORKERS);
                for (int j = 0; j < NUMWORKERS; j++) {
                    int min;
                    if (j == 0) {
                        min = 2 * i;
                    } else {
                        min = (j * chunkSize) - ((j * chunkSize) % i);
                    }
                    int max = ((j+1) * chunkSize);
                    Job job = new Job(min, max, i);
                    jobs.put(job);
                }

            }
        }

        // Create finished jobs to trigger threads to shut down
        for (int i = 0; i < NUMWORKERS; i++) {
            jobs.put(new Job());
        }

        // Shut down threads
        executorService.shutdown();
        if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }

        // Collect remaining primes
        for (int i = upperBoundSqrt; i <= upperBound; i++) {
            if (!isComposite[i]) {
                primes.add(i);
            }
        }

        return primes;
    }

    // Each job takes which chunk of the array to start and end at and how much to increment by
    // Finish is used to shut down the threads at the end
    private class Job {
        int min;
        int max;
        int prime;

        boolean finish;
        public Job(int min, int max, int prime) {
            this.min = min;
            this.max = max;
            this.prime = prime;
            this.finish = false;
        }

        public Job() {
            this.min = 0;
            this.max = 0;
            this.prime = 0;
            this.finish = true;
        }

        public boolean isFinished() {
            return finish;
        }
    }

    // This class represents each thread
    private class Worker implements Runnable {

        @Override
        public void run() {
            while (true) {
                Job job;
                try {
                    job = jobs.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (job.isFinished()) {
                    return;
                }
                // Label each multiple of the prime in this chunk as composite
                for (int i = job.min; i <= job.max && i < isComposite.length; i += job.prime) {
                    isComposite[i] = true;
                }
            }
        }
    }

}