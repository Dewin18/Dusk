package Entity;

import TileMap.TileMap;
import TileMap.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;
public class MapObject {

    // tile map stuff
    protected TileMap tileMap;
    protected int tileSize;
    protected double xmap;
    protected double ymap;

    // Position
    protected Vector2 position;

    // dimenstions
    protected int width;
    protected int height;

    // collision box
    protected CollisionBox collisionBox;
    protected Vector2 collisionOffset;


    // animation
    // TODO
    protected BufferedImage sprite;

    // Constructor
    public MapObject(TileMap tm) {
        tileMap = tm;
        tileSize = tileMap.getTileSize();
    }

    public void setPosition(double x, double y) {
        if(position == null) {
            position = new Vector2(x, y);
            return;
        }
        this.position.x = x;
        this.position.y = y;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public void draw(Graphics2D g) {
        g.drawImage(sprite, (int)position.x, (int)position.y, null);
        g.setColor(Color.BLUE);
        int[] a = collisionBox.toXYWH();
        g.drawRect(a[0], a[1], a[2], a[3]);
    }
}
