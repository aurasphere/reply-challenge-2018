package co.aurasphere.reply.challenge.training.model;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.List;

/**
 * Represents a triangle-shaped path obstacle.
 * 
 * @author Donato Rimenti
 *
 */
public class Obstacle {

	/**
	 * First vertex of the obstacle.
	 */
	private Point a;

	/**
	 * Second vertex of the obstacle.
	 */
	private Point b;

	/**
	 * Third vertex of the obstacle.
	 */
	private Point c;

	/**
	 * First segment of the obstacle.
	 */
	private Line2D firstSegment;
	
	/**
	 * Second segment of the obstacle.
	 */
	private Line2D secondSegment;
	
	/**
	 * Third segment of the obstacle.
	 */
	private Line2D thirdSegment;

	/**
	 * Instantiates a new obstacle.
	 *
	 * @param vertices
	 *            the vertices of the obstacle. For simplicity, the vertices are
	 *            provided in form of a list of 3 elements. If more elements are
	 *            provided, they are ignored. If less elements are provided, an
	 *            exception is thrown
	 */
	public Obstacle(List<Point> vertices) {
		this.a = vertices.get(0);
		this.b = vertices.get(1);
		this.c = vertices.get(2);

		// Initializes the segments.
		this.firstSegment = new Line2D.Float(a, b);
		this.secondSegment = new Line2D.Float(b, c);
		this.thirdSegment = new Line2D.Float(c, a);
	}

	/**
	 * Checks if a point is inside an obstacle by taking advantage of the fact
	 * that it's a triangle and using baricentric coordinates.
	 * 
	 * @param x
	 *            the x coordinate of the point to check
	 * @param y
	 *            the y coordinate of the point to check
	 * @return true if the point is inside the obstacle, false otherwise
	 */
	public boolean isPointInside(long x, long y) {
		long s = a.y * c.x - a.x * c.y + (c.y - a.y) * x + (a.x - c.x) * y;
		long t = a.x * b.y - a.y * b.x + (a.y - b.y) * x + (b.x - a.x) * y;

		if ((s < 0) != (t < 0))
			return false;

		long A = -b.y * c.x + a.y * (c.x - b.x) + a.x * (b.y - c.y) + b.x * c.y;
		if (A < 0.0) {
			s = -s;
			t = -t;
			A = -A;
		}
		return s > 0 && t > 0 && (s + t) <= A;
	}

	/**
	 * Checks if this obstacle obstructs the path between two points.
	 * 
	 * @param origin
	 *            the first point of the path
	 * @param destination
	 *            the second point of the path
	 * @return true if the path is obstructed by this obstacle, false otherwise
	 */
	public boolean isPathObstructed(Point origin, Point destination) {
		Line2D pathToCheck = new Line2D.Float(origin, destination);

		return firstSegment.intersectsLine(pathToCheck) || secondSegment.intersectsLine(pathToCheck)
				|| thirdSegment.intersectsLine(pathToCheck);
	}

}
