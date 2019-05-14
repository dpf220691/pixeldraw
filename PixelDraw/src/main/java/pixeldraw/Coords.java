
package pixeldraw;

/**
 *
 * @author dpf
 */
public class Coords 
{
    
    public final int row, col;

    public Coords(int row, int col)
    {
        this.row = row;
        this.col = col;
    }    

    public String toStringCenter0_0(int canvasRows, int canvasCols) 
    {
        return "(" + (row-canvasRows/2) + ", " + (canvasCols/2-col) + ")";
    }
    
    public static Coords toCenter0_0(Coords coords, int canvasRows, int canvasCols)
    {
        return new Coords((canvasCols/2-coords.col), (coords.row-canvasRows/2));
    }
    
    public static Coords fromCenter0_0(Coords coords, int canvasRows, int canvasCols)
    {
        return new Coords((canvasRows/2+coords.row), (canvasCols/2-coords.col));
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Coords other = (Coords) obj;
        if (this.row != other.row)
            return false;
        if (this.col != other.col)
            return false;
        return true;
    }
  
}
