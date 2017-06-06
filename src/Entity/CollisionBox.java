package Entity;

import TileMap.Vector2;

public class CollisionBox {
    public Vector2 center;
    public Vector2 halfSize;

    public CollisionBox(Vector2 center, Vector2 halfSize) {
        this.center = center;
        this.halfSize = halfSize;
    }

    public boolean overlaps(CollisionBox other) {
        if (Math.abs(center.x - other.center.x) > halfSize.x + other.halfSize.x) return false;
        if (Math.abs(center.y - other.center.y) > halfSize.y + other.halfSize.y) return false;
        return true;
    }
}
