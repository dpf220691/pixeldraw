package bus.transformation;

import bus.Tool;
import bus.shape.IShape;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dpf
 */
public abstract class Transformation extends Tool {

	public abstract void compute(List<IShape> canvasShapes);

	public static List<Transformation> getAll() {
		List<Transformation> transformations = new ArrayList<>();
		transformations.add(MoveTransformation.getInstance());
		transformations.add(ScaleTransformation.getInstance());
		transformations.add(RotateTransformation.getInstance());
		// TODO
		return transformations;
	}

}
