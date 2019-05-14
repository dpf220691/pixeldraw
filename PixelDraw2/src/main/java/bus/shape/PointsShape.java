
package bus.shape;

import bus.Point;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dpf
 */
public class PointsShape implements IShape
{
    
    private List<Point> points;
    private Color color;

    public PointsShape(Color color, Point... points)
    {
        this.color = color;
        this.points = Arrays.asList(points);
    }

    @Override
    public List<Point> compute()
    {
        return points;
    }

    @Override
    public Color getColor()
    {
        return color;
    }

    @Override
    public List<Point> getPoints()
    {
        return points;
    }
    
}
