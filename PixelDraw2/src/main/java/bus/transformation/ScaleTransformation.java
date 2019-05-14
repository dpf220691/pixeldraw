
package bus.transformation;

import bus.Point;
import bus.shape.IShape;
import java.util.List;
import pixelDraw.I;

/**
 *
 * @author dpf
 */
class ScaleTransformation extends Transformation{

	private int state;
	private final int lastState = 1;

	private final static ScaleTransformation INSTANCE = new ScaleTransformation();

	public static ScaleTransformation getInstance() {
		return INSTANCE;
	}

	private ScaleTransformation() {
	}
	
	private double factor;
	
	@Override
	public void compute(List<IShape> canvasShapes) {
		if (state != lastState) {
			throw new IllegalArgumentException();
		}
		Point temp;
		for (IShape shape : canvasShapes) {
			for (Point p : shape.getPoints()) {
				temp = Point.toCenter0_0(p, I.CANVAS_COLS, I.CANVAS_ROWS);
				temp.row = (int) Math.round((((double)temp.row) * factor));
				temp.col = (int) Math.round((((double)temp.col) * factor));
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
		factor = arg;
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
				msg = "Scale factor";
				break;
			case 1:
				msg = "Scaled to "+ factor;
				break;
		}
		return msg;
	}

	@Override
	public String getName() {
		return "Scale";
	}

	@Override
	public void onSelect() {
		factor = 1.0;
		state = 0;
	}
	
}
