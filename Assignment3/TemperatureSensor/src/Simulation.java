import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Simulation {
    static final int NUMWORKERS = 8;
    static final int HOUR = 60;
    private static ExecutorService executorService = Executors.newFixedThreadPool(NUMWORKERS);
    static int[] readings = new int[60 * 8];
    static boolean[] busy = new boolean[8];
    static Lock readingsLock = new ReentrantLock();
    static Random rand = new Random();
    static int numHours;
    static int curHour;

    public void runSimulation(int num) throws InterruptedException {
        numHours = num;
        curHour = 0;
        for (int i = 0; i < NUMWORKERS; i++) {
            executorService.execute(new Sensor(i));
        }
        executorService.shutdown();
        if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
    }

    private class Sensor implements Runnable {
        int id;
        public Sensor(int id) {
            this.id = id;
        }
        @Override
        public void run() {
            while (curHour < numHours) {
                for (int min = 0; min < HOUR; min++) {
                    busy[id] = true;
                    readingsLock.lock();
                    try {
                        readings[id * HOUR + min] = rand.nextInt(71 - (-100)) + (-100);
                    } finally {
                        readingsLock.unlock();
                    }
                    busy[id] = false;

                    while (threadsBusy()) {
                        // Wait
                    }
                }

                if (id == 0) {
                    readingsLock.lock();
                    try {
                        report(curHour);
                        curHour++;
                    } finally {
                        readingsLock.unlock();
                    }
                }
            }
        }

        private boolean threadsBusy() {
            for (int i = 0; i < busy.length; i++) {
                if (busy[i]) {
                    return true;
                }
            }
            return false;
        }

        private void report(int hour) {
            System.out.println("Hour " + (hour+1) + " Report");
            reportDiff();
            Arrays.sort(readings);
            System.out.println("Lowest 5 readings: ");
            for (int i = 0; i < 5; i++) {
                System.out.print(readings[i] + "\u00B0 ");
            }
            System.out.println("\nHighest 5 readings: ");
            for (int i = readings.length - 5; i < readings.length; i++) {
                System.out.print(readings[i] + "\u00B0 ");
            }
            System.out.println("\n");
        }

        private void reportDiff() {
            int interval = 10;
            int maxDiff = 0;
            int maxStart = 0;
            int maxEnd = 0;
            for (int i = 0; i < NUMWORKERS; i++) {

                for (int j = 0; j < HOUR - interval + 1; j++) {
                    int max = 0;
                    int min = 1000000;
                    for (int k = 0; k < interval; k++) {
                        int num = readings[i * j + k];
                        max = Math.max(num, max);
                        min = Math.min(num, min);
                    }
                    if (maxDiff < max - min) {
                        maxDiff = max - min;
                        maxStart = j;
                        maxEnd = j + interval;
                    }
                }
            }
            System.out.println("Largest temperature difference was " + maxDiff
                    + "\u00B0 from minute " + (maxStart+1) + " to minute " + (maxEnd+1));
        }
    }
}
