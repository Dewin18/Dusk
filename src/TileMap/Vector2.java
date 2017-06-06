package TileMap;

public class Vector2 {
    public double x = 0;
    public double y = 0;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(double[] coords) {
        this.x = coords[0];
        this.y = coords[1];
    }
}
