
package pixeldraw;

import tools.SlopeIntercept2;
import tools.Point;
import tools.ITool;
import tools.Bresenham2;
import tools.Dda;
import java.util.ArrayList;
import java.util.List;
import tools.Bresenham2;
import tools.Bresenham1;
import tools.SlopeIntercept1;
import ui.Gui;

/**
 *
 * @author dpf
 */
public class I
{
    private final static int CANVAS_COLS = 161;
    private final static int CANVAS_ROWS = 81;
    public static final int PIXEL_SIZE = 10;
    
    public static final String APP_NAME = "Pixel Draw";

    public static void main(String[] args)
    {
        // Tools
        List<ITool> tools = new ArrayList<>();
        tools.add(new Point());
        tools.add(new SlopeIntercept1());
        tools.add(new SlopeIntercept2());
        tools.add(new Dda());
        tools.add(new Bresenham1());
        tools.add(new Bresenham2());
        // Run UI
        new Gui(CANVAS_COLS, CANVAS_ROWS, PIXEL_SIZE, tools); 
    }
    
}
