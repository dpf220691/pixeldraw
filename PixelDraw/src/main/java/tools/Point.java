
package tools;

import pixeldraw.Coords;
import java.util.List;

/**
 *
 * @author dpf
 */
public class Point implements ITool
{ 
    @Override
    public List<Coords> outputPixels(List<Coords> input) 
    {
        if (input.size() != argsCount())
            throw new IllegalArgumentException();
        return input;
    }

    @Override
    public String getName() 
    {
        return "Point";
    }

    @Override
    public int argsCount()
    {
        return 1;
    }

}
