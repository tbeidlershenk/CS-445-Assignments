// MedOfFive
// Written by Tobias Beidler-Shenk

public class MedOfFive<T extends Comparable<? super T>> implements Partitionable<T>{
    
    public int partition (T[] a, int first, int last) {

        int mid = (first + last) / 2;
        int second = (first + mid) / 2;
        int fourth = (mid + last) / 2;
        int[] arr = {first, second, mid, fourth, last};

        medOfFive(a, arr);
        
        swap(a, mid, last - 1);
	    int pivInd = last - 1;
	    T pivot = a[pivInd];
        
        int leftInd = first + 1; 
	    int rightInd = last - 2;
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

    // call order on every pair
    private void medOfFive(T[] a, int[] arr) {
        for (int i = 0; i < arr.length-1; i++) {
            for (int j = i+1; j < arr.length; j++)
                order(a, arr[i], arr[j]);
        }
    }

    // order method
    private void order(T[] a, int i, int j) {
        if (a[i].compareTo(a[j]) > 0)
	        swap(a, i, j);
    }

    private void swap(Object [] a, int i, int j) {
		Object temp = a[i];
		a[i] = a[j];
		a[j] = temp; 
	}
}
