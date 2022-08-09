// SimplePivot (pivot = last index)
// Written by Tobias Beidler-Shenk

public class SimplePivot<T extends Comparable<? super T>> implements Partitionable<T> {

    public int partition(T[] a, int first, int last) {
        
        int pivInd = last;
        T pivot = a[pivInd];
        int leftInd = first;
        int rightInd = last-1;
        
        boolean done = false;

        //System.out.println(first + " " + last); 
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
		return leftInd; 
    }

    private void swap(Object [] a, int i, int j) {
		Object temp = a[i];
		a[i] = a[j];
		a[j] = temp; 
	}

}
