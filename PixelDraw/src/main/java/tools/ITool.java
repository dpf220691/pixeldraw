
package tools;

import pixeldraw.Coords;
import java.util.List;

/**
 *
 * @author dpf
 */
public interface ITool
{
    public List<Coords> outputPixels(List<Coords> input);
    public String getName();
    public int argsCount();  
}
