
package tools;

import pixeldraw.Coords;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dpf
 */
public class SlopeIntercept2 implements ITool
{

    @Override
    public List<Coords> outputPixels(List<Coords> input) 
    {
        if (input.size() != argsCount())
            throw new IllegalArgumentException();
        int x1 = input.get(0).row;
        int y1 = input.get(0).col;
        int xf = input.get(1).row;
        int yf = input.get(1).col;  
        List<Coords> l = new ArrayList<>();        
        //

        int dx = xf - x1;
        int dy = yf - y1;
        
        float m, b;
        int x, y;

        if(Math.abs(dx) > Math.abs(dy))
        {
            if(x1>xf)
            {
                x = xf;
                y = yf;
                xf = x1;
                yf = y1;
            }
            else
            {
                x = x1;
                y = y1;
            }
            dx = xf - x;
            dy = yf - y;
            
            m = (float)dy / (float)dx;
            b = (float)y - ((float)(m*x));

            while(x <= xf)
            {
                l.add(new Coords(x, y));
                x++;
                y = Math.round(m*x+b);
            }
        }
        else
        {
            if(y1 > yf)
            {
                x = xf;
                y = yf;
                xf = x1;
                yf = y1;
            }
            else
            {
                x = x1;
                y = y1;
            }    
            dx = xf - x;
            dy = yf - y;
            
            m = (float)dx / (float)dy;
            b = (float)x - (float)(m*y);  
            
            while(y <= yf)
            {
                l.add(new Coords(x, y));
                y++;
                x = Math.round(m*y+b);
            }
        }
        
        //
        return l;
    }

    @Override
    public String getName() {
        return "Slope Intercept +";
    }

    @Override
    public int argsCount() {
        return 2;
    }
    
}
