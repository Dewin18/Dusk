package TileMap;

public class Vector2i {
    public double x = 0;
    public double y = 0;

    public Vector2i(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2i(double[] coords) {
        this.x = coords[0];
        this.y = coords[1];
    }

    public Vector2i addThis(Vector2i other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Vector2i add(Vector2i other) {
        Vector2i a = new Vector2i(x, y);
        a.x += other.x;
        a.y += other.y;
        return a;
    }

    public String toString() {
        return "X: " + x + "  Y: "+ y;
    }
}
