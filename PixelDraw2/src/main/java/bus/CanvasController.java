package bus;

import bus.Point;
import bus.shape.IShape;
import java.util.List;

/**
 *
 * @author dpf
 */
public interface CanvasController {

          public List<IShape> getShapes();

          public void selectPoint(Point p);

          public void refresh();

}
