package tr.edu.gtu.cse222.project.group8.adt;
import java.util.*;

/**
 *Heap works like priorityqueue
 */
public class MinHeap < E > extends AbstractQueue < E >  {

    private ArrayList < E > theData;    //ArrayList of datas

    Comparator < E > comparator = null; //For compare() method

    /**
     * No parameter constructor
     */
    public MinHeap() {
        theData = new ArrayList < E > ();
    }

    /**
     *  Constructor
     * @param capacity capacity of list will be created
     * @param comp comparator object of given type
     */
    public MinHeap(int capacity, Comparator < E > comp) {
        if (capacity < 1)
            throw new IllegalArgumentException();
        theData = new ArrayList < E > (capacity + 1);
        comparator = comp;
    }

    /**
     * Merges two heaps
     * @param rv heap that will merged
     * @return true if heaps merged
     */
    public boolean merge(MinHeap<E> rv){
        if (rv==null) return false;

        Iterator iter = rv.iterator();

        //Adding each element one by one
        while (iter.hasNext()) {
            this.add((E)iter.next());
        }
        return true;
    }

    /**
     * Searches given element
     * @param item item that will be searched
     * @return if heap contains item
     */
    public boolean search (E item){
        if (item == null || theData.size() == 0 ) return false;

        for (int i = 0; i< theData.size() ; i++){
            if (compare(item,theData.get(i))==0){
                return true;
            }
        }

        return false;
    }

    /**
     * removes i th largest element
     * @param spesific represents i th largest element
     * @return removed element
     */
    public E remove_spesific_largest(int spesific){
        if (theData.size() ==0 || theData.size() < spesific) return null;

        ArrayList<E> temp = (ArrayList<E>) theData.clone();
        //cloning list so that we can find element that we wanted
        Collections.sort(temp,comparator);

        E item = temp.get(temp.size()-spesific);
        int indexItem=theData.indexOf(item);
        //removing element
        theData.set(indexItem, theData.remove(theData.size() - 1));
        //fixing heap
        fixHeap(indexItem);

        return item;
    }

    /**
     *  fixes heap with given item index
     * @param indexItem takes index of item that may need fixing position on heap
     */
    private void fixHeap(int indexItem){
        int child = indexItem;
        int parent = get_parent(child);
        // Fixing heap for upperbounds via comparing items
        while (parent >= 0 && compare(theData.get(parent), theData.get(child)) > 0) {
            swap(parent, child);
            child = parent;
            parent = get_parent(child);
        }

        parent = indexItem;
        // Fixing heap for lowerbounds via comparing items
        while (true) {
            int leftChild = get_left_child(parent);
            if (leftChild >= theData.size()) {
                break;
            }
            int rightChild = leftChild + 1;
            int minChild = leftChild;

            if (rightChild < theData.size()  && compare(theData.get(leftChild), theData.get(rightChild)) > 0)
                minChild = rightChild;

            if (compare(theData.get(parent), theData.get(minChild)) > 0) {
                swap(parent, minChild);
                parent = minChild;
            } else {
                break;
            }
        }
    }

    /**
     * Iterator with set method
     */
    public class Itr implements Iterator<E> {
        int cursor = 0;
        int lastRet = -1;

        public boolean hasNext() {
            return cursor != theData.size();
        }

        public E next() {
            lastRet = cursor;
            return theData.get(cursor++);
        }

        /**
         * sets item after next()
         * @param item item will setted
         * @return setted element
         */
        public E set(E item){
            if (lastRet < 0)
                throw new IllegalStateException();
            if (item == null) return null;
            theData.set(lastRet, item);

            fixHeap(lastRet);
            return item;
        }

        /**
         * removes element just like poll() method
         */
        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();

            //case that if size == 1
            if (size() == 1){
                theData.remove(theData.size()-1);
                if (lastRet < cursor)
                    cursor--;
                lastRet = -1;
            }
            else { //removes and fixes element
                theData.set(lastRet, theData.remove(theData.size()-1));
                fixHeap(lastRet);
                if (lastRet < cursor)
                    cursor--;
                lastRet = -1;
            }
        }
    }

    /**
     * returns left child of index
     * @param index index of element
     * @return left child of index
     */
    private int get_left_child(int index){
        return 2 * index + 1;
    }

    /**
     * return right child of index
     * @param index index of element
     * @return right child of index
     */
    private int get_right_child(int index){
        return 2 * index + 2;
    }

    /**
     *  returns parent of index
     * @param index index of element
     * @return parent of index
     */
    private int get_parent(int index){
        return (index-1)/2;
    }

    /**
     * adds element in heap
     * @param item item will be added
     * @return if succesfully added
     */
    public boolean offer(E item) {
        theData.add(item);

        fixHeap(theData.size()-1);

        return true;
    }

    /**
     * deletes first element of heap
     * @return first element of heap
     */
    public E poll() {
        if (isEmpty()) {
            return null;
        }
        //first element of heap
        E result = theData.get(0);

        if (theData.size() == 1) {
            theData.remove(0);
            return result;
        }
        //deleting first element of heap
        theData.set(0, theData.remove(theData.size() - 1));
        //fixing heap
        fixHeap(0);

        return result;
    }

    /**
     *  compares items
     * @param left item1
     * @param right item2
     * @return output like compareTo() method
     */
    private int compare(E left, E right) {
        if (comparator != null) { // A Comparator is defined.
            return comparator.compare(left, right);
        }
        else { // Use left’s compareTo method.
            return ( (Comparable < E > ) left).compareTo(right);
        }
    }

    /**
     * swaps values on index a and b
     * @param a value index will swapped
     * @param b value index will swapped
     */
    public void swap(int a, int b) {
        E temp = theData.get(a);
        theData.set(a,theData.get(b));
        theData.set(b,temp);
    }

    /**
     * returns size of heap
     * @return size of heap
     */
    public int size() {
        return theData.size();
    }

    /**
     * iterator we created
     * @return iterator we created
     */
    public Itr iterator() {
        return new Itr();
    }

    /**
     *  return first element of heap
     * @return first element of heap
     */
    public E peek() {
        return theData.get(0);
    }
}
