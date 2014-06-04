/*
 * Created on Nov 7, 2004 by @author Tom Jacobs
 *
 */
package tjacobs.animation;

import java.awt.Point;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Simple math routines spelled out
 * 
 */
public class MathUtils {

	private MathUtils() {
		super();
	}
	
	static double zScoreMinValue = -3.49;
	static double[][] zScores = new double[][] {
			{0.0002, 0.0003, 0.0003, 0.0003, 0.0003, 0.0003, 0.0003, 0.0003, 0.0003, 0.0003},
			{0.0003, 0.0004, 0.0004, 0.0004, 0.0004, 0.0004, 0.0004, 0.0005, 0.0005, 0.0005},
			{0.0005, 0.0005, 0.0005, 0.0006, 0.0006, 0.0006, 0.0006, 0.0006, 0.0007, 0.0007},
			{0.0007, 0.0007, 0.0008, 0.0008, 0.0008, 0.0008, 0.0009, 0.0009, 0.0009, 0.0010},
			{0.0010, 0.0010, 0.0011, 0.0011, 0.0011, 0.0012, 0.0012, 0.0013, 0.0013, 0.0013},
			{0.0014, 0.0014, 0.0015, 0.0015, 0.0016, 0.0016, 0.0017, 0.0018, 0.0018, 0.0019},
			{0.0019, 0.0020, 0.0021, 0.0021, 0.0022, 0.0023, 0.0023, 0.0024, 0.0025, 0.0026},
			{0.0026, 0.0027, 0.0028, 0.0029, 0.0030, 0.0031, 0.0032, 0.0033, 0.0034, 0.0035},
			{0.0036, 0.0037, 0.0038, 0.0039, 0.0040, 0.0041, 0.0043, 0.0044, 0.0045, 0.0047},
			{0.0048, 0.0049, 0.0051, 0.0052, 0.0054, 0.0055, 0.0057, 0.0059, 0.0060, 0.0062},
			{0.0064, 0.0066, 0.0068, 0.0069, 0.0071, 0.0073, 0.0075, 0.0078, 0.0080, 0.0082},
			{0.0084, 0.0087, 0.0089, 0.0091, 0.0094, 0.0096, 0.0099, 0.0102, 0.0104, 0.0107},
			{0.0110, 0.0113, 0.0116, 0.0119, 0.0122, 0.0125, 0.0129, 0.0132, 0.0136, 0.0139},
			{0.0143, 0.0146, 0.0150, 0.0154, 0.0158, 0.0162, 0.0166, 0.0170, 0.0174, 0.0179},
			{0.0183, 0.0188, 0.0192, 0.0197, 0.0202, 0.0207, 0.0212, 0.0217, 0.0222, 0.0228},
			{0.0233, 0.0239, 0.0244, 0.0250, 0.0256, 0.0262, 0.0268, 0.0274, 0.0281, 0.0287},
			{0.0294, 0.0301, 0.0307, 0.0314, 0.0322, 0.0329, 0.0336, 0.0344, 0.0351, 0.0359},
			{0.0367, 0.0375, 0.0384, 0.0392, 0.0401, 0.0409, 0.0418, 0.0427, 0.0436, 0.0446},
			{0.0455, 0.0465, 0.0475, 0.0485, 0.0495, 0.0505, 0.0516, 0.0526, 0.0537, 0.0548},
			{0.0559, 0.0571, 0.0582, 0.0594, 0.0606, 0.0618, 0.0630, 0.0643, 0.0655, 0.0668},
			{0.0681, 0.0694, 0.0708, 0.0721, 0.0735, 0.0749, 0.0764, 0.0778, 0.0793, 0.0808},
			{0.0823, 0.0838, 0.0853, 0.0869, 0.0885, 0.0901, 0.0918, 0.0934, 0.0951, 0.0968},
			{0.0985, 0.1003, 0.1020, 0.1038, 0.1056, 0.1075, 0.1093, 0.1112, 0.1131, 0.1151},
			{0.1170, 0.1190, 0.1210, 0.1230, 0.1251, 0.1271, 0.1292, 0.1314, 0.1335, 0.1357},
			{0.1379, 0.1401, 0.1423, 0.1446, 0.1469, 0.1492, 0.1515, 0.1539, 0.1562, 0.1587},
			{0.1611, 0.1635, 0.1660, 0.1685, 0.1711, 0.1736, 0.1762, 0.1788, 0.1814, 0.1841},
			{0.1867, 0.1894, 0.1922, 0.1949, 0.1977, 0.2005, 0.2033, 0.2061, 0.2090, 0.2119},
			{0.2148, 0.2177, 0.2206, 0.2236, 0.2266, 0.2296, 0.2327, 0.2358, 0.2389, 0.2420},
			{0.2451, 0.2483, 0.2514, 0.2546, 0.2578, 0.2611, 0.2643, 0.2676, 0.2709, 0.2743},
			{0.2776, 0.2810, 0.2843, 0.2877, 0.2912, 0.2946, 0.2981, 0.3015, 0.3050, 0.3085},
			{0.3121, 0.3156, 0.3192, 0.3228, 0.3264, 0.3300, 0.3336, 0.3372, 0.3409, 0.3446},
			{0.3483, 0.3520, 0.3557, 0.3594, 0.3632, 0.3669, 0.3707, 0.3745, 0.3783, 0.3821},
			{0.3859, 0.3897, 0.3936, 0.3974, 0.4013, 0.4052, 0.4090, 0.4129, 0.4168, 0.4207},
			{0.4247, 0.4286, 0.4325, 0.4364, 0.4404, 0.4443, 0.4483, 0.4522, 0.4562, 0.4602},
			{0.4641, 0.4681, 0.4721, 0.4761, 0.4801, 0.4840, 0.4880, 0.4920, 0.4960, 0.5000 }};

	public static double getZScoreProbability(double zScore) {
		boolean positive = zScore > 0;
		double scoreToLookUp = positive ? -zScore : zScore;
		double firstTwoDigits = ((int)(scoreToLookUp * 10) / 10.0);
		int lastDigit = Math.abs((int) ((scoreToLookUp - firstTwoDigits)* 100));
		double percent = 0;
		if (firstTwoDigits < zScoreMinValue) {
			percent = zScores[0][0];
		} else {
			int row = (int) ((scoreToLookUp - zScoreMinValue) * 10); 
			int column = Math.abs(lastDigit - 9);
			percent = zScores[row][column];
		}
		return positive ? 1.0 - percent : percent;
	}
	
	/** This method does not employ an optimal strategy for getting the z score for a probability. If you
	 * Need this to be faster, you should reimplement
	 * @param probability
	 * @return
	 */
	public static double getZScoreForProbability(double probability) {
		if (probability >= 1 || probability <= 0) throw new IllegalArgumentException("Probability must be > 0 and < 1");
		boolean positive = probability > .5;
		if (positive) {
			probability = 1 - probability;
		}
		double diff = 1;
		int row = 0, col = 0;
		for (int i = 0; i < zScores.length; i++) {
			for (int j = 0; j < zScores[i].length; j++) {
				double difference = Math.abs(probability - zScores[i][j]);
				if (difference < diff) {
					row = i;
					col = j;
					diff = difference;
				}
				else if (difference > diff) {
					double zScore = getZScore(row, col);
					if (positive) return zScore; else return -zScore;
				}
			}
		}
		double zScore = getZScore(row, col);
		if (positive) return zScore; else return -zScore;
	}
	
	public static double getZScore(int row, int col) {
		double zScore = (34 - row) * 0.1 + (Math.abs((double)col - 9)) / 100.0; 
		return zScore;
	}
	
	/**
	 * Get the euclideanDistance between 2 points
	 * formula is Math.sqrt((x1 - x2)^2 + (y1 - y2)^2)
	 * 
	 * http://en.wikipedia.org/wiki/Euclidean_distance
	 * 
	 * @return the distance
	 * @deprecated
	 * @see distance(Point, Point) 
	 */
	public static double euclideanDistance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
	}
	
	/**
	 * Calculate the total distance along a Path2D
	 * @param path
	 * @return
	 */
	public static double totalDistance(Path2D path) {
		double dist = 0;
		PathIterator iter = path.getPathIterator(null);
		double at[] = new double[2];
		double to[] = new double[2];
		iter.currentSegment(at);
		while (!iter.isDone()) {
			iter.next();
			iter.currentSegment(to);
			dist += distance (at[0], at[1], to[0], to[1]);
			at[0] = to[0];
			at[1] = to[1];
		}
		return dist;
	}

	/**
	 * 
	 * http://en.wikipedia.org/wiki/Factorial
	 * 
	 * @return x!
	 */
	public static int factorial(int x) {
		int pow = 1;
		while (x > 1) {
			pow = pow * x;
			x--;
		}
		return pow;
	}
	
	/**
	 * 
	 * http://en.wikipedia.org/wiki/Combinatorics#Enumerative_combinatorics
	 * 
	 * @param total
	 * @param picks
	 * @param replacing
	 * @param ordered
	 * @return
	 */
	public static int getCombinations(int total, int picks, boolean replacing, boolean ordered) {
		int ans = 0;
		if (replacing && ordered) {
			ans = (int)Math.pow(total, picks);
			//total / Math.pow(totalpicks)
		}
		else if (!replacing && ordered){
			ans = factorial (total);
			ans /= factorial(total - picks);
		} else if (replacing && !ordered) {
			ans = factorial(total + picks - 1);
			ans /= factorial(picks);
			ans /= factorial(total - picks);
		}
		else if (!replacing && !ordered) {
			ans = factorial(total);
			ans/= factorial(picks);
			ans/= factorial(total - picks);			
		}
		return ans;
	}

	/**
	 * Get the distance to the origin
	 * @param p1
	 * @return
	 */
	public static double distance(Point2D p1) {
		return distance(p1.getX(), p1.getY());
	}

	
	/**
	 * Get the distance to the origin
	 * @param p1
	 * @return
	 */
	public static double distance(Point p1) {
		return distance((double)p1.x, (double)p1.y);
	}
	
	/**
	 * Get the euclideanDistance between 2 points
	 * formula is Math.sqrt((x1 - x2)^2 + (y1 - y2)^2)
	 * 
	 * http://en.wikipedia.org/wiki/Euclidean_distance
	 * @return the distance
	 */
	public static double distance (Point p1, Point p2) {
		return distance(p1.x, p1.y, p2.x, p2.y);
	}
	
	/**
	 * Get the euclideanDistance between 2 points
	 * formula is Math.sqrt((x1 - x2)^2 + (y1 - y2)^2)
	 * 
	 * http://en.wikipedia.org/wiki/Euclidean_distance
	 * 
	 * @return the distance
	 */
	public static double distance(double x1, double y1) {
		return distance(x1, y1, 0, 0);
	}
	
	/**
	 * Get the euclideanDistance between 2 points
	 * formula is Math.sqrt((x1 - x2)^2 + (y1 - y2)^2)
	 * 
	 * http://en.wikipedia.org/wiki/Euclidean_distance
	 * 
	 * @return the distance
	 */
	public static double distance (double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	/**
	 * This method is mostly a re-branding of Math.atan2 for easier code reading
	 * and to make it a little easier to use
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double angle (Point2D p1, Point2D p2) {
		return angle(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}
	
	/**
	 * This method is mostly a re-branding of Math.atan2 for easier code reading
	 * and to make it a little easier to use
	 * @param p1
	 * @param p2
	 * @return angle in radians going from pt (x2, y2) to pt (x1, y1)
	 */
	public static double angle (double x1, double y1, double x2, double y2) {
		double xdiff = x1 - x2;
		double ydiff = y1 - y2;
		//double tan = xdiff / ydiff;
		double atan = Math.atan2(ydiff, xdiff);
		return atan;
	}
	/**
	 * 
	 * @param ray angle being reflected
	 * @param reflectOff angle of the "wall" being reflected off of
	 * @return
	 */
	public static double reflectOff(double ray, double reflectOff) {
		return reflectOff + reflectOff - ray;
	}
	
	public static final boolean SLOPETEST = true;
	
	/**
	 * Assumes 0 = going right, Math.PI / 2 = going down
	 * @param angle
	 * @return
	 */
	public static double getSlope(double angle) {
		double xchange = Math.cos(angle);
		double ychange = Math.sin(angle);
		if (SLOPETEST) {
			assert(Math.atan2(ychange, xchange) == angle);
		}
		xchange = ((int)(xchange * 1000000)) / 1000000.0;
		ychange = ((int)(ychange * 1000000)) / 1000000.0;
		if (xchange == 0) return Double.NaN;
		return ychange / xchange;
	}
	
	/**
	 * Assuming line1 has angle angle1, line2 have angle angle2, and at x = 0, y1 - y2 = heightdiff
	 * @param angle1 cannot be infinity or NaN
	 * @param angle2 cannot be infinity or NaN
	 * @param heightdiff
	 * @return
	 */
	public static double getIntersectionPoint(double angle1, double angle2, double heightdiff) {
		double slope1 = getSlope(angle1);
		double slope2 = getSlope(angle2);
		double combinedslope = slope2 - slope1;
		return heightdiff / combinedslope;
	}

	/**
	 * Get the Y coordinate for the given x coordinate
	 * @param start
	 * @param slope
	 * @param x
	 * @return
	 */
	public static double getYAtX(Point2D start, double slope, double x) {
		return start.getY() + (x - start.getX()) * (slope);
	}
	
	/**
	 * Get the intersection point between two vectors
	 * @param start1
	 * @param angle1
	 * @param start2
	 * @param angle2
	 * @return 
	 */
	public static Point2D getIntersectionPoint(Point2D start1, double angle1, Point2D start2, double angle2) {
		double slope1 = getSlope(angle1), slope2 = getSlope(angle2);
		if (Double.isInfinite(slope1) || Double.isNaN(slope1)) {
			if (Double.isInfinite(slope2) || Double.isNaN(slope2)) {
				if (start1.getX() != start2.getX()) {
					return null;
				}
				else {
					//returning 0 for y coordinate is arbitary - lines are the same.
					return new Point2D.Double(start1.getX(), 0);
				}
			}
			else {
				return new Point2D.Double(start1.getX(), getYAtX(start2, slope2, start1.getX()));
			}
		}
		if ((Double.isInfinite(slope2)|| Double.isNaN(slope2))) {
			return new Point2D.Double(start2.getX(), getYAtX(start1, slope1, start2.getX()));
		}
		double b1 = -1, b2 = -1;
		double a1 = slope1, a2 = slope2;
		double d1 = start1.getY() - start1.getX() * slope1;
		double d2 = start2.getY() - start2.getX() * slope2;
		
		double x = (b2 * d1 - b1 * d2)/(a1 * b2 - a2 * b1);

		double y = (a1 * d2 - a2 * d1)/(a1 * b2 - a2 *b1);
		if (Double.isNaN(x) || Double.isNaN(y) || Double.isInfinite(x) || Double.isInfinite(y)) return null;
		return new Point2D.Double(-x, -y);
	}
	
	/**
	 * Get the intersection points between a rectangle and a vector
	 * @param r
	 * @param pt
	 * @param angle
	 * @return
	 */
	public static Point2D[] getIntersectionPoints(Rectangle2D r, Point2D pt, double angle) {
		Point2D.Double testPt1 = new Point2D.Double(r.getX(), r.getY());
		Point2D.Double testPt2 = new Point2D.Double(r.getX() + r.getWidth(), r.getY() + r.getHeight());
		
		Point2D bottom = getIntersectionPoint(testPt1, 0, pt, angle);
		Point2D top = getIntersectionPoint(testPt2, Math.PI, pt, angle);
		Point2D left = getIntersectionPoint(testPt1, Math.PI / 2, pt, angle);
		Point2D right = getIntersectionPoint(testPt2, 3 * Math.PI / 2, pt, angle);
		//should have 0 or 2 viable points
		boolean viable1 = bottom != null && bottom.getX() >= r.getX() && bottom.getX() <= r.getX() + r.getWidth();
		boolean viable2 = top != null && top.getX() >= r.getX() && top.getX() <= r.getX() + r.getWidth();
		boolean viable3 = right != null && right.getY() >= r.getY() && right.getY() <= r.getY() + r.getHeight();
		boolean viable4 = left != null && left.getY() >= r.getY() && left.getY() <= r.getY() + r.getHeight();
		int viableCount = (viable1 ? 1 : 0) + (viable2 ? 1 : 0) + (viable3 ? 1 : 0) + (viable4 ? 1 : 0);
		Point2D[] vals = new Point2D.Double[viableCount];
		int idx = 0;
		if (viable1) {
			vals[idx++] = bottom;
		}
		if (viable2) {
			vals[idx++] = top;
		}
		if (viable3) {
			vals[idx++] = right;
		}
		if (viable4) {
			vals[idx++] = left;
		}
		return vals;
	}

	/**
	 * Get the mean of a data set
	 * http://simple.wikipedia.org/wiki/Mean_(statistics)
	 * @param data
	 * @return
	 */
	public static double getMean(double[] data) {
		double sum = 0;
		for (double d : data) {
			sum += d;
		}
		return sum / data.length;
	}
	
	/**
	 * sorts the data, picks out middle element
	 * @param data
	 * @return
	 */
	public static double getMedian(double[] data) {
		if (data == null || data.length == 0) throw new IllegalArgumentException("array must be non null and have length > 0");
		java.util.Arrays.sort(data);
		return data[data.length / 2];
	}
	
	/**
	 * sorts the data, picks out most frequent element
	 * @param data
	 * @return
	 */
	public static double getMode(double[] data) {
		if (data == null || data.length == 0) throw new IllegalArgumentException("array must be non null and have length > 0");
		java.util.Arrays.sort(data);
		int maxCount = 1;
		double maxNum = data[0];
		int count = 1;
		double num = data[0];
		for (int i = 1; i < data.length; i++) {
			if (data[i] == num) {
				count++;
			}
			else {
				count = 1;
				num = data[i];
			}
			if (count > maxCount) {
				maxCount = count;
				maxNum = num;
			}
		}
		return maxNum;
	}
	
	/**
	 * Get the standard deviation for a data set
	 * @param data
	 * @return
	 */
	public static double getSD(double[] data) {
		return Math.sqrt(getVariance(data));
	}
	
	/**
	 * Get the variance for a data set
	 * @param data
	 * @return
	 */
	public static double getVariance(double[] data) {
		double mean = getMean(data);
		double variance = 0;
		for (double d : data) {
			double diff = mean - d;
			variance += diff * diff;
		}
		return variance / data.length;
	}
	/**normalize an angle to be -PI < angle <= Math.PI
	 * 
	 * @param angle
	 * @return normalized angle
	 */
	public static double normalizeAngle(double angle) {
		while (angle > Math.PI) {
			angle -= 2 * Math.PI;
		}
		while (angle <= -Math.PI) {
			angle += 2 * Math.PI;
		}
		return angle;
	}

	/**
	 * This exception indicates that the extrapolate function
	 * which was run cannot get an answer, as the vector is
	 * perpendicular to the end point
	 * @author tjacobs
	 *
	 */
	public static class BadAngleException extends Exception {
		private static final long serialVersionUID = 1L;

		public BadAngleException(double angle) {
			super("" + angle);
		}
	}
	
	public static double extrapolateYValueForLineAt(double angle, double startX, double startY, double atX) throws BadAngleException {
		double cos = Math.cos(angle);
		if (cos == 0) throw new BadAngleException(angle);
		double sin = Math.sin(angle);
		return startY + (atX - startX) / cos * sin;
	}

	public static double extrapolateXValueForLineAt(double angle, double startX, double startY, double atY) throws BadAngleException {
		double sin = Math.sin(angle);
		if (sin == 0) throw new BadAngleException(angle);
		double cos = Math.cos(angle);
		return startX + (atY - startY) / sin * cos;
	}

	public static boolean isPrime(long num) {
		if (num <= 0L) throw new IllegalArgumentException("Number must be positive: " + num);
		if ((num & 0x1) != 1) return num == 2;
		if (num % 5 == 0) return num == 5; 
		double sqrtd = Math.sqrt(num);
		int sqrt = (int) Math.round(sqrtd); // could round up but thats ok. want to make sure numbers like 2.999999998 are corrected upwards
		for (int i = 3; i <= sqrt; i+=2) {
			if (i % 5 == 0) continue;
			long div = num / i;
			if (div * i == num) return false;
		}
		return true;
	}
	
	
	//Testing only
	public static void main(String args[]) {
		double[] vals = new double[] {10,12,14,15,8,10,5,11,10,11,30};
		System.out.println("mean: " + getMean(vals));
		System.out.println("median: " + getMedian(vals));
		System.out.println("mode: " + getMode(vals));
		System.out.println("sd: " + getSD(vals));
		
		System.out.println(reflectOff(0, Math.PI / 4) / Math.PI);
		System.out.println(reflectOff(Math.PI / 2, Math.PI) / Math.PI);
		System.out.println(reflectOff(5 * Math.PI / 4, Math.PI / 2) / Math.PI);
		System.out.println(reflectOff(Math.PI, Math.PI / 2));
		double angle1 = Math.PI / 4;
		//double angle2 = Math.PI / 3;
		double angle2 = Math.atan2(3, 2);
		System.out.println("angle2 = " + angle2 / Math.PI * 180);
		double slope1 = getSlope(angle1), slope2 = getSlope(angle2);
		System.out.println("slope1: " + getSlope(angle1) + "slope2: " + getSlope(angle2) + " intersects: " + getIntersectionPoint(angle1, angle2, 1));
		Point2D.Double origin = new Point2D.Double(0,0);
		System.out.println("getYatX for x = 11: " + getYAtX(origin, slope1, 11) + " : " + getYAtX(origin, slope2, 11));
		Point2D.Double pt2 = new Point2D.Double(2, 3);
		double slope3 = 4;
		double angle3 = Math.atan2(slope3, 1);
		System.out.println("intersection: " + getIntersectionPoint(pt2, angle1, origin, angle3));
		//System.out.println()
		Rectangle2D.Double r = new Rectangle2D.Double(0,0,2,2);
		Point2D[] pts = getIntersectionPoints(r, pt2, angle1);
		System.out.println("pts: " + pts.length);
		for (int i = 0; i < pts.length; i++) {
			System.out.println("pt: " + pts[i]);
		}
	}
	
}
