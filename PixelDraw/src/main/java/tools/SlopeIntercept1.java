package tools;

import java.util.ArrayList;
import java.util.List;
import pixeldraw.Coords;

/**
 *
 * @author dpf
 */
public class SlopeIntercept1 implements ITool {

    @Override
    public List<Coords> outputPixels(List<Coords> input) {
        if (input.size() != argsCount()) {
            throw new IllegalArgumentException();
        }
        int x1 = input.get(0).row;
        int y1 = input.get(0).col;
        int xf = input.get(1).row;
        int yf = input.get(1).col;
        List<Coords> l = new ArrayList<>();
        //

        int dx = xf - x1;
        int dy = yf - y1;

        float m, b;
        int x = x1, y = y1;

        m = (float) dy / (float) dx;
        b = (float) y - ((float) (m * x));

        while (x <= xf) {
            l.add(new Coords(x, y));
            x++;
            y = Math.round(m * x + b);
        }

        //
        return l;
    }

    @Override
    public String getName() {
        return "Slope Intecept";
    }

    @Override
    public int argsCount() {
        return 2;
    }

}
