public class Main {
    static final int NUMHOURS = 72;

    public static void main(String[] args) {
        Simulation s = new Simulation();
        try {
            long startTime = System.currentTimeMillis();
            s.runSimulation(NUMHOURS);
            long endTime = System.currentTimeMillis();
            long runtime = endTime - startTime;
            System.out.println("Simulation finished");
            System.out.println("runtime: " + runtime + " ms");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}