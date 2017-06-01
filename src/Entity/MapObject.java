package Entity;

import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
public class MapObject {

    // tile map stuff
    protected TileMap tileMap;
    protected int tileSize;
    protected double xmap;
    protected double ymap;

    // Position
    protected double[] position = new double[2];

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
        this.position[0] = x;
        this.position[1] = y;
    }

    public void draw(Graphics2D g) {
        g.drawImage(sprite, (int)position[0], (int)position[1], null);
    }

    public double[] getPosition() {
        return this.position;
    }
}
