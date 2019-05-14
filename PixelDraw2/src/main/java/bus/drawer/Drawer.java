package bus.drawer;

import bus.Tool;
import bus.shape.IShape;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dpf
 */
public abstract class Drawer extends Tool {

	public abstract IShape computeShape(Color color);

	public static List<Drawer> getAll() {
		List<Drawer> drawers = new ArrayList<>();
		drawers.add(DrawLine.getInstance());
		return drawers;
	}

}
