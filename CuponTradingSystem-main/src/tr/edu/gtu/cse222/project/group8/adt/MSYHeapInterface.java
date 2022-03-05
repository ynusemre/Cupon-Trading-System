/**
 * 
 */
package tr.edu.gtu.cse222.project.group8.adt;

/**
 * @author selman
 * extends Comparable<E>
 */
public interface MSYHeapInterface <E> {
	
	/**
	 * @param element
	 * @return
	 */
	int search(E element); 
	
	/**
	 * @param element
	 * @return
	 */
	boolean merge(MSYHeapInterface<E> element);
	
	/**
	 * @param index
	 * @return
	 */
	public void removeLargestIndexOf(int index);
	
	/**
	 * @return
	 */
	E remove();
	
	/**
	 * @param element
	 */
	void add(E element);
	
	/**
	 * @return
	 */
	int size();
	
}

