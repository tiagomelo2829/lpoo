/*
 * Created on Aug 8, 2005 by @author Tom Jacobs
 *
 */
package tjacobs.animation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A class for building a DAG
 */
public class DirectedAcyclicGraph<T> implements Graph<T> {

	HashMap<T, Set<T>> mLinks;
	
	public DirectedAcyclicGraph() {
		super();
	}
		
	public boolean contains(Object o) {
		return mLinks.keySet().contains(o);
	}
	
	public Iterator<T> iterator() {
		return mLinks.keySet().iterator();
	}
	
	public boolean isEmpty() {
		return mLinks.isEmpty();
	}
	
	public void addNode(T node) {
		Set<T> s = mLinks.get(node);
		if (s == null) {
			s = new HashSet<T>();
			mLinks.put(node, s);
		}
	}
	
	public void addLink (T from, T to) {
		Set<T> s = getLinksFrom(from);
		if (s == null) {
			
		}
	}
	
	public Set<T> getLinksFrom(T node) {
		Set<T> s = mLinks.get(node);
		return (s == null) ? new HashSet<T>() : s;
	}
	
	void removeAll() {
		mLinks.clear();
	}

	public void clear() {
		mLinks.clear();
	}
	
//	/**
//	 * @deprecated
//	 */
//	public Object[] toArray() {
//		throw new RuntimeException("This method is invalid");
//	}
}