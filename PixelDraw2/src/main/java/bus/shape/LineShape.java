package bus.shape;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import bus.Point;
import pixelDraw.I;

/**
 *
 * @author dpf
 */
public class LineShape implements IShape {

	private List<Point> points;
	private Color color;

	public LineShape(Color color, Point ini, Point fin) {
		this.color = color;
		points = new ArrayList<>(2);
		points.add(ini);
		points.add(fin);
	}

	@Override
	public List<Point> compute() {
		int x1 = points.get(0).row;
		int y1 = points.get(0).col;
		int xf = points.get(1).row;
		int yf = points.get(1).col;
		List<Point> l = new ArrayList<>();
		//
		int dx = xf - x1;
		int dy = yf - y1;
		int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0;
		if (dx < 0) {
			dx1 = -1;
		} else if (dx > 0) {
			dx1 = 1;
		}
		if (dy < 0) {
			dy1 = -1;
		} else if (dy > 0) {
			dy1 = 1;
		}
		if (dx < 0) {
			dx2 = -1;
		} else if (dx > 0) {
			dx2 = 1;
		}
		int longest = Math.abs(dx);
		int shortest = Math.abs(dy);
		if (!(longest > shortest)) {
			longest = Math.abs(dy);
			shortest = Math.abs(dx);
			if (dy < 0) {
				dy2 = -1;
			} else if (dy > 0) {
				dy2 = 1;
			}
			dx2 = 0;
		}
		int numerator = longest >> 1;
		int x = x1;
		int y = y1;
		Point p;
		for (int i = 0; i <= longest; i++) {
			l.add(new Point(x,y));
			numerator += shortest;
			if (!(numerator < longest)) {
				numerator -= longest;
				x += dx1;
				y += dy1;
			} else {
				x += dx2;
				y += dy2;
			}
		}
		//
		return l;
	}

	@Override
	public List<Point> getPoints() {
		return points;
	}

	@Override
	public Color getColor() {
		return color;
	}

}
