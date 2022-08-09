// RandomPivot
// Written by Tobias Beidler-Shenk

import java.util.Random;

public class RandomPivot<T extends Comparable<? super T>> implements Partitionable<T> {
    
    public int partition(T[] a, int first, int last) {
        
        Random rand = new Random();
        //System.out.println(first + " "+ last);
	    int pivInd = first + rand.nextInt(last-first+1);
        //System.out.println(pivInd);
	    T pivot = a[pivInd];
        
	    int rightInd = last - 1;
        int leftInd = first; 
        swap(a, pivInd, last);
        pivInd = last;

        boolean done = false;

        while (!done) {

            // smaller side stops once we reach a value to swap
            while (a[leftInd].compareTo(pivot) < 0)
                leftInd++;
            // greater side stops once we reach a value to swap
            while (a[rightInd].compareTo(pivot) > 0 && rightInd > first)
                rightInd--;
            // if needs to swap: swap and run loop again
            if (leftInd < rightInd) {
                swap(a, leftInd, rightInd);
                leftInd++;
                rightInd--;
            // if we got to the pivot: exit while
            } else
                done = true;

        }
        // placing pivot
		swap(a, pivInd, leftInd);
		pivInd = leftInd;
		return pivInd;
    }

    private void swap(Object [] a, int i, int j) {
		Object temp = a[i];
		a[i] = a[j];
		a[j] = temp; 
	}
}
