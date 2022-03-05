/**
 * 
 */
package tr.edu.gtu.cse222.project.group8.adt;

import java.util.Arrays;
import java.util.NoSuchElementException;


/**
 * @author selman
 * extends Comparable<E>
 */
public class MSYHeap <E> implements MSYHeapInterface<E> {
	
	// Data Fields
	/**
	 * 
	 */
	private KWArrayList<E> heap;
	

	/**
	 * Constructor
	 */
	public MSYHeap() {
		super();
		this.heap = new KWArrayList<E>();
	}

	@Override
	public int search(E element) {
		return heap.indexOf(element);
	}

	@Override
	public boolean merge(MSYHeapInterface<E> element) {
		for (int i = 0; i < element.size(); i++) {
			this.add(element.remove());
		}
		return false;
	}
	
	@Override
	public void removeLargestIndexOf(int index) {

		if (index > heap.size()) throw new IndexOutOfBoundsException();
		
		E temp[] = Arrays.copyOf((E[]) heap.getArray(), heap.size());
		// SORTING START 
		int i = 0;
		while(i < temp.length-1) {
			if (((Comparable<E>) temp[i]).compareTo(temp[i+1]) == -1) {
				E x;
				x = temp[i];
				temp[i] = temp[i+1];
				temp[i+1] = x;
				i = -1;
			}
			i = i +1;
		}
		// SORTING DONE 
	
		int indx = search(temp[index-1]);
		E temp1 = heap.get(heap.size()-1);
		heap.set(indx,temp1 );
		remove(heap.size()-1);	
		heapifyAdvanced(indx);

	}
	
	private void heapifyAdvanced(int indx) {
		if ( ((Comparable<E>) heap.get(parent(indx))).compareTo(heap.get(indx)) == 1) {
			heapify(indx+1);
		}
		while (true) {
	    	int leftChild = leftChild(indx);
	    	if (leftChild >= heap.size()) {
	    		break; // Out of heap.
	    	}
	    	int rightChild = rightChild(indx);
	    	int minChild = leftChild; // Assume leftChild is smaller.
	
	    	if (rightChild < heap.size() && compare(leftChild,rightChild) < 0) {
	    		minChild = rightChild;
	    	}
	
	    	if (compare(indx,minChild) < 0) { // index ==> indx
	    		swap(indx, minChild);
	    		indx = minChild;
	    	}
	    	else { // Heap property is restored.
	    		break;
	    	}
		}
	}

	@Override
	public E remove() {
		E temp = heap.get(0);
		heap.set(0, heap.remove(heap.size()-1));
		heapify(0);
		return temp;
	}
	
	private E remove(int index) {
		E temp = heap.get(index);
		heap.remove(index);
		return temp;
	}

	@Override
	public void add(E element) {
		heap.add(element);
		heapify(heap.size());

	}

	@Override
	public int size() {
		return heap.size();
	}
	
	@Override
	public String toString() {
		StringBuilder output = toStringHelper();
		return output.toString();
	}
	
	/**
	 * @param parent
	 * @param output
	 */
	private StringBuilder toStringHelper() {
		StringBuilder output = new StringBuilder();
        for (int i = 0; i <= size() / 2; i++) {
            for (int j = 0; j < Math.pow(2, i) && j + Math.pow(2, i) <= size(); j++) { // Each row has 2^n nodes
                output.append(heap.get(j + (int) Math.pow(2, i) - 1) + " ");
            }
            output.append("\n");
        }
        return output;
	}
	
	/**
	 * @param index1
	 * @param index2
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int compare(int index1, int index2) {
		return ((Comparable<E>) heap.get(index1)).compareTo(heap.get(index2));
	}
	
	/**
	 * @param index1
	 * @param index2
	 */
	private void swap(int index1, int index2) {
		E temp;
		temp = heap.get(index1);
		heap.set(index1, heap.get(index2));
		heap.set(index2, temp);
	}
	
	/**
	 * @param index
	 */
	private void heapify(int index) {
		if (index == 0) {
			//down heap remove call
		    while (true) {
		    	int leftChild = leftChild(index);
		    	if (leftChild >= heap.size()) {
		    		break; // Out of heap.
		    	}
		    	int rightChild = rightChild(index);
		    	int minChild = leftChild; // Assume leftChild is smaller.

		    	if (rightChild < heap.size() && compare(leftChild,rightChild) < 0) {
		    		minChild = rightChild;
		    	}

		    	if (compare(index,minChild) < 0) {
		    		swap(index, minChild);
		    		index = minChild;
		    	}
		    	else { // Heap property is restored.
		    		break;
		    	}
		    }
		}else {
			//up heap call add
		    // child is newly inserted item.
		    int child = index-1;
		    int parent = parent(child); // Find child�s parent (child - 1) / 2
		    // Reheap
		    while (parent >= 0 && compare(parent, child) < 0) {
		    	swap(parent, child);
		    	child = parent;
		    	parent = parent(child);
		    }
		}
	}
	
	
	/**
	 * @param index
	 * @return
	 */
	public int parent(int index) {
        if (index % 2 == 1) return index / 2;
        return ((index-1) / 2);
	}
	
	/**
	 * @param index
	 * @return
	 */
	public int leftChild(int index) {
		return 2 * index + 1;
	}
	
	/**
	 * @param index
	 * @return
	 */
	public int rightChild(int index) {
		return 2 * index + 2;
	}
	
	public MSYHeapIteratorX<E> iterator(){
		return new Iterator();
	}

	private class Iterator implements MSYHeapIteratorX<E>{
		
		private E lastItem = null;
		private int index = 0;

		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return this.index < size();
		}

		@Override
		public E next() {
			// TODO Auto-generated method stub
			if (!hasNext()) throw new NoSuchElementException();
			
			lastItem = heap.get(this.index++); // keep this because of the set
			return lastItem;
		}

		@Override
		public void set(E item) {
			// TODO Auto-generated method stub
			if (lastItem == null) throw new IllegalStateException();
			
			heap.set(this.index-1, item);
			heapifyAdvanced(this.index-1);

			
		}
	}
	
	public E peek() {
		return heap.get(0);
	}
}
