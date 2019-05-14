
package tools;

import pixeldraw.Coords;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dpf
 */
public class Bresenham3 implements ITool 
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
        
        double m, e;
        int x, y;

        if(Math.abs(dx) > Math.abs(dy))
        {
            if(x1>xf)
            {
                x = xf;
                y = yf;
                xf = x1;
                yf = y1;
                System.out.println("1");
            }
            else
            {
                x = x1;
                y = y1;
                System.out.println("2");
            }
            dx = xf - x;
            dy = yf - y;
            
            m = (double)dy / (double)dx;
            
            e = m - 0.5;
            int i = 0;
            while (i <= dx) // main loop
            {
                l.add(new Coords(x, y));
                while (e > 0) // when e>0
                {
                    y++; // update y
                    e--;  // compensate e
                }
                x++;   // update x
                e+=m;   // update e
                i++;
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
                System.out.println("3");
            }
            else
            {
                x = x1;
                y = y1;
                System.out.println("4");
            }
            dx = xf - x;
            dy = yf - y;

            m = (double)dx / (double)dy;
            
            e = m - 0.5;
            int i = 0;
            while (i <= dy) // main loop
            {
                l.add(new Coords(x, y));
                while (e > 0) // when e>0
                {
                    x++; // update x
                    e--;  // compensate e
                }
                y++;   // update y
                e+=m;   // update e
                i++;
            }
        }
        
        //
        return l;
    }

    @Override
    public String getName() 
    {
        return "Bresenham*";
    }

    @Override
    public int argsCount() 
    {
        return 2;
    }

}
