package tjacobs.animation;

import java.io.Serializable;

/**
 * Java implementation of a value range / span
 * 
 * @author tomjacobs
 *
 * @param <T> class that this Span is made up of
 */
public class Span<T extends Comparable<? super T>> implements Serializable, PrimaryKey<Span<T>>, Comparable<Span<T>> {
	private static final long serialVersionUID = 1L;

	//private static final String DEFAULT_SEPARATOR ="-";
	//private static final String SEPARATOR ="<==>";
	private static final String SEPARATOR ="--";

	private T start, end;
	private boolean includesStart, includesEnd;

	public Span(T start, T end) {
		this(start, end, true, true);
	}
	
	public Span(T start, T end, boolean includesStart, boolean includesEnd) {
		if (start == null && end == null) throw new IllegalArgumentException("either start or end or both must be non-null");
		if (start != null && end != null && end.compareTo(start) < 0) {
			this.start= end;
			this.end = start;
		}
		else {
			this.start = start;
			this.end = end;
		}
		this.includesStart = includesStart;
		this.includesEnd = includesEnd;
	}
	
	public Object[] getPrimaryKey() {
		return new Object[] {start, end, includesStart, includesEnd};
	}
	
	public boolean equals(Span<T> o) {
		if (o.getClass() != getClass()) {
			return false;
		}
		return PrimaryKey.Impl.equalsImpl( this,  o);
	}
	
	public int hashCode() {
		return PrimaryKey.Impl.hashCode(this);
	}
	
	public boolean contains(T val) {
		return ((start == null || val.compareTo(start) > 0 || (val.compareTo(start) == 0 && includesStart)) && (end == null || val.compareTo(end) < 0 || (val.compareTo(end) == 0 && includesEnd)));
	}
	
	public boolean overlaps(Span<T> s) {
		return s.contains(getSpanStart()) || s.contains(getSpanEnd()) 
			//first 2 checks rule out spans crossing boundries. There are two cases left. Either
			//the this span is completely outside of s, or it is completely inside. Test 3 determines that
		|| contains(s.getSpanStart());  //|| contains(s.getSpanEnd());
	}
		
	public T getSpanStart() {
		return start;
	}
	
	public T getSpanEnd() {
		return end;
	}
	
	public boolean isStartIncluded() {
		return includesStart;
	}
	
	public boolean isEndIncluded() {
		return includesEnd;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(start);
		sb.append(SEPARATOR);
		sb.append(end);
		return sb.toString();
	}
	
	public static Span<Integer> parseIntSpan(String s) {
		//if (s == null) return null;
		String[] parts = s.split(SEPARATOR);
		Span<Integer> sp = new Span<Integer>(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
		return sp;
	}
	
	public static Span<Double> parseDoubleSpan(String s) {
		String[] parts = s.split(SEPARATOR);
		Span<Double> sp = new Span<Double>(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
		return sp;		
	}
	
	public static Span<String> parseStringSpan(String s) {
		String[] parts = s.split(SEPARATOR);
		Span<String> sp = new Span<String>(parts[0], parts[1]);
		return sp;
	}
	
	public int compareTo(Span<T> s) {
		if (overlaps(s)) return 0;
		return getSpanStart().compareTo(s.getSpanStart());
	}

	public static void main(String[] args) {
		Span<Integer> span = new Span<Integer>(0, 10);
		int[] testVals = new int[] {-1,0,4,9,10,11,14};
		for (int i = 0; i < testVals.length; i++) {
			System.out.println("testing " + testVals[i] + ": " + span.contains(testVals[i]));
		}
		Span<String> span2 = new Span<String>(null, "Given");
		Span<String> span3 = new Span<String>("Given", null);
		String[] testVals2 = new String[] {"Andy", "Grape", "Gift", "Flower", "bocce", "Given"};
		for (int i = 0; i < testVals2.length; i++) {
			System.out.println("testing " + testVals2[i] + ": " + span2.contains(testVals2[i]));
		}
		for (int i = 0; i < testVals2.length; i++) {
			System.out.println("testing " + testVals2[i] + ": " + span3.contains(testVals2[i]));
		}
	}
}
