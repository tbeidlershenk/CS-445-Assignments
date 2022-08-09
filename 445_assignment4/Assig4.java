// Assign4.java
// Written by Tobias Beidler-Shenk

import java.util.ArrayList;
import java.util.Random;

public class Assig4 {
    public static void main(String[] args) {
        
        try {

            int N = Integer.parseInt(args[0]);
            int runs = Integer.parseInt(args[1]);
            Assig4.runTests(N, runs);
            

        } catch (Exception e) {
            System.out.println("Please enter the following command line arguments as ints:\n" + 
                               "Size of array input N\n" + "Number of runs");
        }
    }

    public static void runTests(int N, int runs) {
        
        ArrayList<Sorter<Integer>> sorters = new ArrayList<Sorter<Integer>>();
        
        sorters.add(new QuickSort(new SimplePivot()));
		sorters.add(new QuickSort(new MedOfThree()));
		sorters.add(new QuickSort(new RandomPivot()));
		sorters.add(new QuickSort(new MedOfFive()));
		sorters.add(new MergeSort());

        Integer[] list = new Integer[N];
        
        for (int i = 0; i < N; i++)
            list[i] = i+1;
        list = Assig4.shuffle(list, 0);
        

        // three-dimensional array to store all times
        long[][][] times = new long[5][6][runs];
        // for x runs
        for (int run = 0; run < runs; run++) {
           
            // for each sorter
            for (int sor = 0; sor < 5; sor++) {
                
                int minSize = 5;

                // for each minSize value
                int iteration = 0;
                while (minSize <= 160) {

                    sorters.get(sor).setMin(minSize);

                    // time the algorithm for current config
                    long start = System.nanoTime();
                    sorters.get(sor).sort(list, list.length);
                    long end = System.nanoTime();
                    
                    // store the time in the current config location
                    times[sor][iteration][run] = end - start;
                    
                    iteration++;
                    minSize *= 2;
                    list = Assig4.shuffle(list, run); // using run as seed

                }
            }
        }

        Assig4.formatResults(times, runs, N);

    }

    public static Integer[] shuffle(Integer[] list, int seed) {
        Random rand = new Random();
        // Seed will be the same for each iteration of the inner loops (each run of all algorithms), 
        // but will change with each outer loop, so there will be a new seed for each run
        rand.setSeed(seed);
        int size = list.length;
        for (int i = 0; i < size; i++) {
            int j = rand.nextInt(size);
            Integer temp = list[i];
            list[i] = list[j];
            list[j] = temp;
        }
        return list;
    }

    public static void formatResults(long[][][] times, int runs, int N) {
        
        final double BIL = 1000000000.0;
        long[][] averages = new long[5][6];
        double[][] bestTimes = new double[5][2];
        double[][] worstTimes = new double[5][2];
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                long total = 0;
                for (int run = 0; run < runs; run++)
                    total += times[i][j][run];
                averages[i][j] = total / runs;
            }
            
            long max = averages[i][0];
            int maxMinSize = 5;
            long min = averages[i][0];
            int minMinSize = 5;

            
            for (int j = 1; j < 6; j++) {
                if (max < averages[i][j]) {
                    max = averages[i][j];
                    maxMinSize = (int)(5 * Math.pow(2, j));
                } else if (min > averages[i][j]) {
                    min = averages[i][j];
                    minMinSize = (int)(5 * Math.pow(2, j));
                }
            }

            worstTimes[i][0] = max/BIL;
            worstTimes[i][1] = maxMinSize;
            bestTimes[i][0] = min/BIL;
            bestTimes[i][1] = minMinSize;

        }
        int worst = 0;
        int best = 0;
        for (int i = 0; i < 5; i++) {
            if (worstTimes[worst][0] < worstTimes[i][0])
                worst = i;
            if (bestTimes[best][0] > bestTimes[i][0])
                best = i;
        }

        String[] algos = {"QuickSort - Simple Pivot", 
                          "QuickSort - Median of Three", 
                          "QuickSort - Random Pivot", 
                          "QuickSort - Median of Five", 
                          "MergeSort"};

        // Header
        System.out.println("RESULTS FOR ARRAY SIZE = " + N);
        System.out.println("Number of runs: " + runs);

        for (int i = 0; i < algos.length; i++) {
            
            // Algorithm
            System.out.println("\n" + algos[i]);
            
            // Best
            System.out.print("Best average time: " + bestTimes[i][0] + " with " + bestTimes[i][1] + " as minSize");
            if (i == best)
                System.out.println(" (Best overall)");
            else System.out.println();
            
            // Worst
            System.out.print("Worst average time: " + worstTimes[i][0] + " with " + worstTimes[i][1] + " as minSize");
            if (i == worst)
                System.out.println(" (Worst overall)");
            else System.out.println();
        }
    }
}
