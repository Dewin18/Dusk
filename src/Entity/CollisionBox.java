package Entity;

import TileMap.Vector2;

public class CollisionBox
{
    public Vector2 center;
    public Vector2 halfSize;

    public CollisionBox(Vector2 center, Vector2 halfSize)
    {
        this.center = center;
        this.halfSize = halfSize;
    }

    public CollisionBox()
    {

    }

    public boolean overlaps(CollisionBox other)
    {
        if (Math.abs(center.x - other.center.x) > halfSize.x + other.halfSize.x) return false;
        if (Math.abs(center.y - other.center.y) > halfSize.y + other.halfSize.y) return false;
        return true;
    }

    public int[] toXYWH()
    {
        int[] a = new int[4];
        Vector2 topLeft = center.sub(halfSize);
        a[0] = (int) topLeft.x;
        a[1] = (int) topLeft.y;
        a[2] = (int) halfSize.x * 2;
        a[3] = (int) halfSize.y * 2;
        return a;
    }

    public void setCenter(Vector2 center)
    {
        this.center = center;
    }

    public void setHalfSize(Vector2 halfSize)
    {
        this.halfSize = halfSize;
    }
}
