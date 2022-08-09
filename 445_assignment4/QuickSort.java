// QuickSort
// Written by Tobias Beidler-Shenk

public class QuickSort<T extends Comparable<? super T>> implements Sorter<T> {
    
    private Partitionable<T> partAlgo;
    private int MIN_SIZE;  

    public QuickSort(Partitionable<T> part) {
        partAlgo = part;
        MIN_SIZE = 5;
    }
	
    // stub method
    public void sort(T[] a, int size) {
        quick(a, 0, size-1);
    }

    // divide and conquer quicksort
    private void quick (T[] a, int first, int last) {
        // base case: if below minimum size use insertion sort
        if (last > first + MIN_SIZE) {
            // partitioning based on Partitionable obj
            int piv = partAlgo.partition(a, first, last);
            // divide and conquer with partition
            quick(a, first, piv-1);
            quick(a, piv+1, last);
        // otherwise, use divide and conquer
        } else 
            insertionSort(a, first, last);
    }
   
    private void insertionSort (T[] a, int first, int last) {
        int unsorted;

        for (unsorted = first + 1; unsorted <= last; unsorted++) {
            T element = a[unsorted];
            insertInOrder(element, a, first, unsorted - 1);
        }
    }

    private void insertInOrder(T element, T[] a, int begin, int end) {
        int index;
        for (index = end; (index >= begin) && (element.compareTo(a[index]) < 0); index--) 
            a[index + 1] = a[index];
        
        a[index + 1] = element;
    }

    public void setMin(int minSize) {
        MIN_SIZE = minSize;
    }

}
