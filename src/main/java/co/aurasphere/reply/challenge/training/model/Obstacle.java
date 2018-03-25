package co.aurasphere.reply.challenge.training.model;

import java.awt.Point;
import java.util.List;

/**
 * Represents a triangle-shaped path obstacle.
 * 
 * @author Donato Rimenti
 *
 */
public class Obstacle {

	/**
	 * First vertex.
	 */
	private Point a;

	/**
	 * Second vertex.
	 */
	private Point b;

	/**
	 * Third vertex.
	 */
	private Point c;

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
	}

	private boolean sign(Point p1, Point p2, Point p3) {
		return ((p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y)) < 0;
	}

	public boolean isPointInsideWithSign(int x, int y) {
		Point point = new Point(x, y);
		boolean b1 = sign(point, a, b);
		boolean b2 = sign(point, b, c);
		boolean b3 = sign(point, c, a);
		return ((b1 == b2) && (b2 == b3));
	}

	public boolean isPointInsideWithBaricentricCoords(int x, int y) {
		int s = a.y * c.x - a.x * c.y + (c.y - a.y) * x + (a.x - c.x) * y;
		int t = a.x * b.y - a.y * b.x + (a.y - b.y) * x + (b.x - a.x) * y;

		if ((s < 0) != (t < 0))
			return false;

		int A = -b.y * c.x + a.y * (c.x - b.x) + a.x * (b.y - c.y) + b.x * c.y;
		if (A < 0.0) {
			s = -s;
			t = -t;
			A = -A;
		}
		return s > 0 && t > 0 && (s + t) <= A;
	}

}
