
package bus.transformation;

import bus.Point;
import bus.shape.IShape;
import java.util.List;
import pixelDraw.I;

/**
 *
 * @author dpf
 */
public class RotateTransformation extends Transformation{
	
	private int state;
	private final int lastState = 1;

	private final static RotateTransformation INSTANCE = new RotateTransformation();

	public static RotateTransformation getInstance() {
		return INSTANCE;
	}

	private RotateTransformation() {
	}

	private double alpha;
	
	@Override
	public void compute(List<IShape> canvasShapes) {
		if (state != lastState) {
			throw new IllegalArgumentException();
		}
		Point temp;
		double arad = (alpha * Math.PI)/180.0;
		for (IShape shape : canvasShapes) {
			for (Point p : shape.getPoints()) {
				temp = Point.toCenter0_0(p, I.CANVAS_COLS, I.CANVAS_ROWS);
				temp = (new Point((int)Math.round(temp.row*Math.cos(arad)-temp.col*Math.sin(arad)),(int)Math.round(temp.row*Math.sin(arad)+temp.col*Math.cos(arad))));
				temp = Point.fromCenter0_0(temp, I.CANVAS_COLS, I.CANVAS_ROWS);
				p.row = temp.row;
				p.col = temp.col;
			}
		}
	}

	@Override
	public void input(Point arg) {
		throw new IllegalArgumentException();
	}

	@Override
	public void input(double arg) {
		alpha = arg;
		state++;
	}

	@Override
	public Arg nextArg() {
		if (state < lastState) {
			return Arg.VALUE;
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
				msg = "Angle";
				break;
			case 1:
				msg = "Rotated "+ alpha + " degrees";
				break;
		}
		return msg;
	}

	@Override
	public String getName() {
		return "Rotate";
	}

	@Override
	public void onSelect() {
		alpha = 0.0;
		state = 0;
	}
	
}
