package tjacobs.animation;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public interface MovingAnimation {
	/**
	 * Get the size of the MovingAnimation at the specified time
	 * @param time
	 * @return
	 */
	public Dimension getSizeAtTime(int time);
	/**
	 * Render the graphics of the MovingAnimation to the given
	 * Graphics2D object at the specified time
	 * @param g
	 * @param time
	 */
	public void paintAtTime(Graphics2D g, int time);
	/**
	 * Get the location of the MovingAnimation at the specified time
	 * @param time
	 * @return
	 */
	public Point2D getLocationAtTime(int time);
}