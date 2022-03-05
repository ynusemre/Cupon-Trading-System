package tr.edu.gtu.cse222.project.group8.adt;

public class Pair<U, V>{
    private U first;
    private V second;

    /**
    * Constructs a new Pair with the given values.
    * @param first  the first element
    * @param second the second element
    */
    public Pair(U first, V second){
        this.first = first;
        this.second = second;
    }

    public U getFirst() {
        return this.first;
    }

    public V getSecond() {
        return this.second;
    }
}   