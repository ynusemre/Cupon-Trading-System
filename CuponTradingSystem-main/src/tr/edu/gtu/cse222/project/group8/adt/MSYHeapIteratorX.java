package tr.edu.gtu.cse222.project.group8.adt;

public interface MSYHeapIteratorX<E> {
	
	/**
	 * @return
	 */
	boolean hasNext();
	
	/**
	 * @return
	 * @throws NoSuchElementException
	 */
	E next();
	
	/**
	 * @param item
	 */
	void set(E item);

}
