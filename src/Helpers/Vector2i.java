package Helpers;

/** PTP 2017
 * Main struct of the project defining a 2D integer Vector and its operations.
 *
 * @author Ali Popa
 * @version 13.08.
 * @since 06.06.
 */
public class Vector2i
{
    public int x = 0;
    public int y = 0;

    public Vector2i(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Vector2i(int[] coords)
    {
        this.x = coords[0];
        this.y = coords[1];
    }

    public Vector2i addToThis(Vector2i other)
    {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Vector2i add(Vector2i other)
    {
        Vector2i a = new Vector2i(x, y);
        a.x += other.x;
        a.y += other.y;
        return a;
    }

    public String toString()
    {
        return "X: " + x + "  Y: " + y;
    }
}
