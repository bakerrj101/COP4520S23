// Implementing method 3, check other file for main code

public class Main {
    public static void main(String[] args) {
        VaseQueue q = new VaseQueue();
        try {
            long startTime = System.currentTimeMillis();
            q.queue();
            long endTime = System.currentTimeMillis();
            long runtime = endTime - startTime;
            System.out.println("all guests have visited the vase as many times as they wanted");
            System.out.println("runtime: " + runtime + " ms");
        } catch (InterruptedException e) {
            System.out.println("program interrupted");
        }
    }
}
