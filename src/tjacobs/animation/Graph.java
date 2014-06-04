/*
 * Created on Aug 8, 2005 by @author Tom Jacobs
 *
 */
package tjacobs.animation;

import java.util.Set;
/**
 * an interface for a graph
 */
public interface Graph<T> {	
	public Set<T> getLinksFrom(T node);
}