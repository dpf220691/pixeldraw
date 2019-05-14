package bus;

/**
 *
 * @author dpf
 */
public class Point {

	public int row, col;

	public Point(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public Point(){
		this(0, 0);
	}

	@Override
	public String toString() {
		return "(" + row + ", " + col + ")";
	}
	
	public String toStringCenter0_0(int canvasHeight, int canvasWidth) {
		return toCenter0_0(this, canvasHeight, canvasWidth).toString();
	}

	public static Point toCenter0_0(Point p, int canvasHeight, int canvasWidth) {
		return new Point( p.row - (canvasHeight / 2), (canvasWidth/2) - p.col);
	}

	public static Point fromCenter0_0(Point p, int canvasHeight, int canvasWidth) {
		return new Point((canvasHeight/2+p.row), (canvasWidth/2-p.col));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Point other = (Point) obj;
		if (this.row != other.row) {
			return false;
		}
		if (this.col != other.col) {
			return false;
		}
		return true;
	}

}
