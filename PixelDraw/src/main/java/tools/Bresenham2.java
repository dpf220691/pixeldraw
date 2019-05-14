


package tools;

import pixeldraw.Coords;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dpf
 */
public class Bresenham2 implements ITool 
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

        int dx = xf - x1 ;
        int dy = yf - y1 ;
        int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
        if (dx<0) dx1 = -1 ; else if (dx>0) dx1 = 1 ;
        if (dy<0) dy1 = -1 ; else if (dy>0) dy1 = 1 ;
        if (dx<0) dx2 = -1 ; else if (dx>0) dx2 = 1 ;
        int longest = Math.abs(dx) ;
        int shortest = Math.abs(dy) ;
        if (!(longest>shortest)) {
            longest = Math.abs(dy) ;
            shortest = Math.abs(dx) ;
            if (dy<0) dy2 = -1 ; else if (dy>0) dy2 = 1 ;
            dx2 = 0 ;            
        }
        int numerator = longest >> 1 ;
        int x = x1;
        int y = y1;
        for (int i=0;i<=longest;i++) {
            l.add(new Coords(x, y));
            numerator += shortest ;
            if (!(numerator<longest)) {
                numerator -= longest ;
                x += dx1 ;
                y += dy1 ;
            } else {
                x += dx2 ;
                y += dy2 ;
            }
        }
        
        //
        return l;
    }

    @Override
    public String getName() 
    {
        return "Bresenham+";
    }

    @Override
    public int argsCount() 
    {
        return 2;
    }

}
