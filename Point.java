package algoTest;

public class Point {
	public double x, y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	// Eucledian distance
	public double getDist(Point p) {
		double dx = p.x - x;
		double dy = p.y - y;
		return Math.sqrt(dx * dx + dy * dy);
	}
}