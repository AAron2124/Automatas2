import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class optimizacion {

    public static void main(String[] args) {
        int n = 1000000000;
        long startTime = System.nanoTime();
        int numThreads = Runtime.getRuntime().availableProcessors();

       ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        long sum = 0;
        try {
            sum = executor.submit(() -> {
                long partialSum = 0;
                for (int i = 1; i <= n; i++) {
                    partialSum += i;
                }
                return partialSum;
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        long endTime = System.nanoTime() - startTime;
        System.out.println("Duración: " + (endTime - startTime) / 1e6 + " ms");
        
        System.out.println("Suma: " + sum);
       
    }

}
