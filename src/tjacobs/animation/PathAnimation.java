package tjacobs.animation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import tjacobs.animation.MathUtils;
import tjacobs.animation.PaintUtils;
import tjacobs.animation.WindowUtilities;

public abstract class PathAnimation implements MovingAnimation {
	private Path2D mPath;
	private double totalLength;
	
	/**
	 * Be careful to call path.closePath before invoking this constructor
	 * @param path
	 */
	public PathAnimation(Path2D path) {
		mPath = path;
		totalLength = 0;
		PathIterator iterator = mPath.getPathIterator(null);
		//Point2D currentLocation;// = path.getCurrentPoint();
		double[] location = new double[6];
		iterator.currentSegment(location);
		while (!iterator.isDone()) {
			double[] loc = new double[6];
			iterator.next();
			iterator.currentSegment(loc);
			if (loc[0] == 0 && loc[1] == 0) continue;
			double distance = MathUtils.distance(location[0], location[1], loc[0], loc[1]);
			totalLength += distance;
			location = loc;
		}
	}
	
	@Override
	public Point2D getLocationAtTime(int time) {
		return getLocationAtTime(time / (double) getTotalAnimationTime());
	}
	
	public Point2D getLocationAtTime(double pctTime) {
		double len = totalLength * pctTime;
		PathIterator iterator = mPath.getPathIterator(null);
		double[] location = new double[6];
		iterator.currentSegment(location);
		while (!iterator.isDone()) {
			double[] loc = new double[6];
			iterator.next();
			iterator.currentSegment(loc);
			double distance= MathUtils.distance(location[0], location[1], loc[0], loc[1]);
			if (distance > len) {
				double pctThere = len / distance;
				double xSpot = location[0] * (1 - pctThere) + loc[0] * pctThere;
				double ySpot = location[1] * (1 - pctThere) + loc[1] * pctThere;
				return new Point2D.Double(xSpot, ySpot);
			}
			len -= distance;
			location = loc;
		}
		throw new ArrayIndexOutOfBoundsException("Path is too short or time is too long!");
	}

	/**
	 * Number of milliseconds that this animation spans
	 * @return
	 */
	public abstract int getTotalAnimationTime();
	
	public static void main(String args[]) {
		Rectangle rect = new Rectangle(10,10,20,20);
		final Path2D.Double myPath = new Path2D.Double((Shape)rect);
		myPath.closePath();
		final PathAnimation myAnimation = new PathAnimation(myPath) {
			Area star = new Area(PaintUtils.createStandardStar(15, 15, 5, .5, 0));
			@Override
			public Dimension getSizeAtTime(int time) {
				return new Dimension(15,15);
			}

			@Override
			public void paintAtTime(Graphics2D g, int time) {
				Area toPaint = star;
				if ((time / 150) % 2 == 1) {
					Dimension size = getSizeAtTime(0);
					toPaint = new Area(toPaint);
					PaintUtils.rotateArea(toPaint, Math.PI / 6);
				}
				g.setColor(Color.YELLOW);
				g.fill(toPaint);
				g.setColor(Color.RED);
				g.draw(toPaint);
			}

			@Override
			public int getTotalAnimationTime() {
				return 10000;
			}
		};
		System.out.println(myAnimation.getLocationAtTime(0));
		System.out.println(myAnimation.getLocationAtTime(2500));
		System.out.println(myAnimation.getLocationAtTime(4000));
		System.out.println(myAnimation.getLocationAtTime(5000));
		System.out.println(myAnimation.getLocationAtTime(7000));
		System.out.println(myAnimation.getLocationAtTime(7500));
		System.out.println(myAnimation.getLocationAtTime(9000));
		System.out.println(myAnimation.getLocationAtTime(10000));
		
		final JPanel jp = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				int time = ((int) System.currentTimeMillis()) % myAnimation.getTotalAnimationTime();
				int time2 = (time + myAnimation.getTotalAnimationTime() / 2) % myAnimation.getTotalAnimationTime();
				Point2D pt = myAnimation.getLocationAtTime(time);
				Point2D pt2 = myAnimation.getLocationAtTime(time2);
				Dimension size = myAnimation.getSizeAtTime(time);
				g2.translate(pt.getX() - size.width / 2, pt.getY() - size.height / 2);
				myAnimation.paintAtTime(g2, time);
				g2.translate(- (pt.getX() - size.width / 2), - (pt.getY() - size.height / 2));
				g2.translate(pt2.getX() - size.width / 2, pt2.getY() - size.height / 2);
				myAnimation.paintAtTime(g2, time2);
				g2.translate(- (pt2.getX() - size.width / 2), - (pt2.getY() - size.height / 2));				
				g2.setColor(Color.BLACK);
				g2.draw(myPath);
			}
		};
		WindowUtilities.visualize(jp);
		AbstractAction action = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				jp.repaint();
			}
		};
		javax.swing.Timer t = new javax.swing.Timer(30, action);
		t.start();
	}
}