package co.aurasphere.reply.challenge.shortestpath.model;

import java.awt.Point;
import java.util.List;

public class Triangle {

	private Point a;

	private Point b;

	private Point c;

	public Triangle(List<Point> points) {
		this.a = points.get(0);
		this.b = points.get(1);
		this.c = points.get(2);
	}

	float sign(Point p1, Point p2, Point p3) {
		return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
	}

	public boolean isPointInside(int x, int y) {
		Point point = new Point(x, y);
		boolean b1 = sign(point, a, b) < 0.0f;
		boolean b2 = sign(point, b, c) < 0.0f;
		boolean b3 = sign(point, c, a) < 0.0f;
		return ((b1 == b2) && (b2 == b3));
	}
	
	public boolean isPointInsideWithBaricentricCoords(int x, int y) {
		 int s = a.y * c.x - a.x * c.y + (c.y - a.y) * x + (a.x - c.x) * y;
		 int t = a.x * b.y - a.y * b.x + (a.y - b.y) * x + (b.x - a.x) * y;

		    if ((s < 0) != (t < 0))
		        return false;

		    int A = -b.y * c.x + a.y * (c.x - b.x) + a.x * (b.y - c.y) + b.x * c.y;
		    if (A < 0.0)
		    {
		        s = -s;
		        t = -t;
		        A = -A;
		    }
		    return s > 0 && t > 0 && (s + t) <= A;
	}

}
