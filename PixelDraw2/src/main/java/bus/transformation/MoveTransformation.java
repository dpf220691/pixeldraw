package bus.transformation;

import bus.Point;
import bus.shape.IShape;
import java.util.List;

/**
 *
 * @author dpf
 */
public class MoveTransformation extends Transformation {

	private int state;
	private final int lastState = 2;

	private final static MoveTransformation INSTANCE = new MoveTransformation();

	public static MoveTransformation getInstance() {
		return INSTANCE;
	}

	private MoveTransformation() {
	}
	
	private Point base;
	private Point end;
	private Point move;

	@Override
	public void compute(List<IShape> canvasShapes) {
		if (state != lastState) {
			throw new IllegalArgumentException();
		}
		move = new Point(end.row - base.row, end.col - base.col);
		for (IShape shape : canvasShapes) {
			for (Point p : shape.getPoints()) {
				p.row = p.row + move.row;
				p.col = p.col + move.col;
			}
		}
	}

	@Override
	public void input(Point arg) {
		switch (state) {
			case 0:
				base = arg;
				break;
			case 1:
				end = arg;
				break;
		}
		if (state < lastState) {
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
				msg = "Base point";
				break;
			case 1:
				msg = "End point";
				break;
			case 2:
				msg = "Moved "+move.toString();
				break;
		}
		return msg;
	}

	@Override
	public String getName() {
		return "Move";
	}

	@Override
	public void onSelect() {
		base = new Point();
		end = new Point();
		state = 0;
	}

}
