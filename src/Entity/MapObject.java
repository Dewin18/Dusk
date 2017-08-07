package Entity;

import TileMap.TileMap;
import TileMap.Vector2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public abstract class MapObject extends ObservableEntity
{

    // tile map stuff
    protected TileMap tileMap;
    protected int tileSize;
    protected double xmap;
    protected double ymap;

    // Position
    protected Vector2 position = new Vector2(0, 0);
    protected Vector2 velocity = new Vector2(0, 0);

    // dimensions
    protected int width;
    protected int height;
    protected double rotation = 0;

    // collision box
    protected CollisionBox collisionBox;
    protected Vector2 collisionOffset;

    // force stuff
    protected int forceCurrentTime, forceMaxTime = 0;
    protected Vector2 forceToAdd = Vector2.ZERO;

    // animation
    Animation animation;
    int currentAction;
    ArrayList<BufferedImage[]> sprites;

    BufferedImage sprite;

    public MapObject(TileMap tm)
    {
        tileMap = tm;
        tileSize = tileMap.getTileSize();
        animation = new Animation();
        collisionBox = new CollisionBox();
    }

    public void setPosition(double x, double y)
    {
        if (position == null)
        {
            position = new Vector2(x, y);
            return;
        }
        this.position.x = x;
        this.position.y = y;
    }

    /**
     * Returns the current position for a MapObject
     *
     * @return Vector2
     */
    public Vector2 getPosition()
    {
        return this.position;
    }

    public void setPosition(Vector2 position)
    {
        this.position = position;
    }

    /**
     * Loads the sprites from a file
     *
     * @param fileName  the spritesheet name
     * @param numframes number of frames per row
     * @param widths    width of the sprites per row
     * @param heights   height of the sprites per row
     */
    void loadSprites(String fileName, int[] numframes, int[] widths, int[] heights)
    {
        try
        {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/" + fileName));
            int count = 0;
            sprites = new ArrayList<>();
            for (int i = 0; i < numframes.length; i++)
            {
                BufferedImage[] bi = new BufferedImage[numframes[i]];
                for (int j = 0; j < numframes[i]; j++)
                {
                    bi[j] = spritesheet.getSubimage(j * widths[i], count, widths[i], heights[i]);
                }
                sprites.add(bi);
                count += heights[i];
            }
        } catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }

    /**
     * Add force to a current object that decays over the specified amount of time.
     *
     * @param force force to add
     * @param time  time for the force to decay over
     */
    void addForce(Vector2 force, int time) {
        forceCurrentTime = 0;
        forceMaxTime = time;
        forceToAdd = force;
    }

    /**
     * Update the current force.
     */
    void updateForce() {
        if (forceCurrentTime < forceMaxTime)
        {
            forceCurrentTime++;
            velocity.addToThis(forceToAdd);
        }
    }

    /**
     * Main update method.
     */
    public void update() {
        updateForce();
    }

    public abstract void draw(Graphics2D g);

}
