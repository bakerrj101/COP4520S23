import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PresentSort {

    private static final int NUMWORKERS = 4;
    private static ExecutorService executorService = Executors.newFixedThreadPool(NUMWORKERS);
    private static PresentLinkedList list = new PresentLinkedList();
    private static Iterator<Integer> bag;
    private static int numCards= 0;

    private static Lock bagLock = new ReentrantLock();
    private static Lock cardLock = new ReentrantLock();

    public int sort(Iterator<Integer> iter) throws InterruptedException {
        bag = iter;
        for (int i = 0; i < NUMWORKERS; i++) {
            executorService.execute(new Worker());
        }
        executorService.shutdown();
        if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }

        return numCards;
    }

    private class Worker implements Runnable {
        @Override
        public void run() {
            while (numCards < Main.NUMPRESENTS) {
                int job = (int)(Math.random() * 3);
                if (job == 0) {
                    int id;
                    bagLock.lock();
                    try {
                        if (bag.hasNext()) {
                            id = bag.next();
                            boolean temp = list.add(id);
                        }

                    } finally {
                        bagLock.unlock();
                    }


                } else if (job == 1) {
                    boolean temp = list.remove();
                    if (temp) {
                        cardLock.lock();
                        try {
                            numCards++;
                        } finally {
                            cardLock.unlock();
                        }
                    }

                } else {
                    int id = (int)(Math.random() * Main.NUMPRESENTS);
                    // System.out.println(id + ": " + list.contains(id));
                    // Uncomment above statement to see minotaur's requests (printing will increase runtime)
                }
            }
        }
    }
}
