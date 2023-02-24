public class Main {
    public static void main(String[] args) {
        Maze m = new Maze();
        try {
            long startTime = System.currentTimeMillis();
            m.party();
            long endTime = System.currentTimeMillis();
            long runtime = endTime - startTime;
            System.out.println("all guests have gone through the maze at least once");
            System.out.println("runtime: " + runtime + " ms");
        } catch (InterruptedException e) {
            System.out.println("program interrupted");
        }
    }
}