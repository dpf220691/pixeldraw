
package bus.shape;

import bus.Point;
import java.awt.Color;
import java.util.List;

/**
 *
 * @author dpf
 */
public interface IShape
{
    public List<Point> compute();
    public List<Point> getPoints();
    public Color getColor();
}
