package bus.drawer;

import bus.Point;
import bus.shape.IShape;
import bus.shape.LineShape;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import pixelDraw.I;

/**
 *
 * @author dpf
 */
class DrawLine extends Drawer {

	private List<Point> points;

	private int state;
	private final int lastState = 2;
	
	private String pointsMsg;

	private final static DrawLine INSTANCE = new DrawLine();

	public static DrawLine getInstance() {
		return INSTANCE;
	}

	private DrawLine() {
	}

	@Override
	public IShape computeShape(Color color) {
		if (state != lastState) {
			throw new IllegalArgumentException();
		}
		int x1 = points.get(0).row;
		int y1 = points.get(0).col;
		Point ini = new Point(x1, y1);
		int xf = points.get(1).row;
		int yf = points.get(1).col;
		Point end = new Point(xf, yf);
		
		IShape line =  new LineShape(color, ini, end);
		for(Point p: line.compute()){
			pointsMsg += " " + p.toStringCenter0_0(I.CANVAS_COLS, I.CANVAS_ROWS);
		}
		return line;
	}

	@Override
	public void input(Point arg) {
		if (state < lastState) {
			points.add(arg);
			state++;
		}	
	}

	@Override
	public void input(double arg) {
		throw new IllegalArgumentException();
	}

	@Override
	public Arg nextArg() {
		if (state < lastState) {
			return Arg.POINT;
		}
		throw new IllegalArgumentException();
	}

	@Override
	public boolean hasNextArg() {
		if (state < lastState) {
			return true;
		}
		return false;
	}

	@Override
	public String getNextMessage() {
		String msg = "";
		switch (state) {
			case 0:
				msg = "Set initial point";
				break;
			case 1:
				msg = "Set end point";
				break;
			case 2:
				msg = "Line points" + pointsMsg;
				break;
		}
		return msg;
	}

	@Override
	public String getName() {
		return "Line";
	}

	@Override
	public void onSelect() {
		points = new ArrayList<>(2);
		state = 0;
		pointsMsg = "";
	}

}
