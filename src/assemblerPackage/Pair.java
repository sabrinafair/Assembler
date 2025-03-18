package assemblerPackage;

public class Pair<T1, T2> {
	 private final T1 first;
	    private final T2 second;

	    public Pair(T1 first, T2 second) {
	        this.first = first;
	        this.second = second;
	    }

	    public T1 getFirst() {
	        return first;
	    }

	    public T2 getSecond() {
	        return second;
	    }

	    @Override
	    public String toString() {
	        return "(" + first + ", " + second + ")";
	    }

	    @Override
	    public boolean equals(Object obj) {
	        if (this == obj) {
	            return true;
	        }
	        if (obj == null || getClass() != obj.getClass()) {
	            return false;
	        }
	        Pair<?, ?> other = (Pair<?, ?>) obj;
	        return (first == null ? other.first == null : first.equals(other.first)) &&
	               (second == null ? other.second == null : second.equals(other.second));
	    }

	    @Override
	    public int hashCode() {
	        int result = first != null ? first.hashCode() : 0;
	        result = 31 * result + (second != null ? second.hashCode() : 0);
	        return result;
	    }
}
