// MergeSort
// Written by Tobias Beidler-Shenk

public class MergeSort<T extends Comparable<? super T>> implements Sorter<T> {
    
    private int MIN_SIZE;

    public MergeSort() {
        MIN_SIZE = 5;
    }
	
    public void sort(T[] a, int size) {
        mergeSort(a, 0, size - 1);
    }

    private void mergeSort(T[] a, int first, int last) {
        T[] tempArray = (T[])new Comparable<?>[a.length];
	    mergeSort(a, tempArray, first, last);
    }

    private void mergeSort(T[] a, T[] tempArray, int first, int last) {
        if (last - first < MIN_SIZE)
            insertionSort(a, first, last);
        else {
            int mid = (first + last) / 2;
	        
            mergeSort(a, tempArray, first, mid);
	        mergeSort(a, tempArray, mid + 1, last);

			if (a[mid].compareTo(a[mid + 1]) > 0)      
	     	 	merge(a, tempArray, first, mid, last); 
	    }
    }
   
    private void merge(T[] a, T[] tempArray, int first, int mid, int last) {
        int beginHalf1 = first;
		int endHalf1 = mid;
		int beginHalf2 = mid + 1;
		int endHalf2 = last;
		int index = beginHalf1; 
		
        for (; (beginHalf1 <= endHalf1) && (beginHalf2 <= endHalf2); index++) { 
	        if (a[beginHalf1].compareTo(a[beginHalf2]) <= 0) {  
	      	    tempArray[index] = a[beginHalf1];
	            beginHalf1++;
	        } else {  
	      	    tempArray[index] = a[beginHalf2];
	            beginHalf2++;
	        }  
        }

	    for (; beginHalf1 <= endHalf1; beginHalf1++, index++)
	        tempArray[index] = a[beginHalf1];

		for (; beginHalf2 <= endHalf2; beginHalf2++, index++)
	        tempArray[index] = a[beginHalf2];
		
	    for (index = first; index <= last; index++)
	        a[index] = tempArray[index];
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
