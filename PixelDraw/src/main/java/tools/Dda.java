
package tools;

import pixeldraw.Coords;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dpf
 */
public class Dda implements ITool {

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
        
        int d = Math.max(Math.abs(dx), Math.abs(dy));
        
        double ix = (double)dx/(double)d;
        double iy = (double)dy/(double)d;

        double inc = 0.5;
        double x = x1 + inc;
        double y = y1 + inc;
        
        int i = 0;
        while(i<=d)
        {
            l.add(new Coords((int)Math.floor(x),(int)Math.floor(y)));
            x = x + ix;
            y = y + iy;
            i++;
        }  
        
        //
        return l;
    }

    @Override
    public String getName() 
    {
        return "DDA";
    }

    @Override
    public int argsCount() 
    {
        return 2;
    }
    
}
