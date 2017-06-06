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
    protected int colWidth;
    protected int colHeight;
    protected double colOffset = 0.0;


    // animation
    // TODO
    protected BufferedImage sprite;

    // Constructor
    public MapObject(TileMap tm) {
        tileMap = tm;
        tileSize = tileMap.getTileSize();
    }

    public void setPosition(double x, double y) {
        this.position.x = x;
        this.position.y = y;
    }

    public void draw(Graphics2D g) {
        g.drawImage(sprite, (int)position.x, (int)position.y, null);
    }

    public Vector2 getPosition() {
        return this.position;
    }
}
