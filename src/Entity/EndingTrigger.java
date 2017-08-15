package Entity;

import Helpers.Vector2;
import TileMap.TileMap;

import java.awt.*;

public class EndingTrigger extends MapObject
{
    public EndingTrigger(TileMap tm)
    {
        super(tm);
        init();
    }

    private void init()
    {
        setPosition(595, 1705);
        collisionBox = new CollisionBox();
        collisionBox.setCenter(position);
        collisionBox.setHalfSize(new Vector2(tileSize/2, tileSize/2));
        collisionOffset = Vector2.ZERO;
    }

    @Override
    public void draw(Graphics2D g)
    {
        // Collision Box
        g.setColor(Color.GREEN);
        int[] a = collisionBox.toXYWH();
        g.fillRect(a[0] + (int)tileMap.cameraPos.x, a[1] + (int)tileMap.cameraPos.y, a[2], a[3]);
    }


}
