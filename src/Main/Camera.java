package Main;

import Entity.Player;
import Helpers.OpenSimplexNoise;
import TileMap.TileMap;
import Helpers.Vector2;
import Helpers.Vector2i;

import java.awt.*;

public class Camera
{
    // references
    private TileMap tileMap;
    private Player player;

    // position and size
    private Vector2 position = new Vector2(0, 0);
    private Vector2i size;
    //private double scale;
    private double tween = 0.1;
    private int tileSize;

    // bounds
    private double xmin;
    private double ymin;
    private double xmax;
    private double ymax;
    private int numRows;
    private int numCols;

    // offsets
    private int rowOffset;
    private int colOffset;
    private int numRowsToDraw;
    private int numColsToDraw;

    // shake
    private int duration = 10;
    private int elapsed = duration;
    private float magnitude = 1;
    private OpenSimplexNoise noise;

    // slice
    private int durationSlice = 10;
    private int elapsedSlice = durationSlice;
    private float magnitudeSlice = 10;

    public Camera(TileMap tileMap, Player player)
    {
        this.tileMap = tileMap;
        this.player = player;
        size = new Vector2i(GamePanel.WIDTH, GamePanel.HEIGHT);
        tileSize = tileMap.getTileSize();

        setBounds(tileMap.getWidth() - tileSize, tileMap.getHeight() - tileSize, 0, 0);

        numRowsToDraw = size.y / tileSize + 2;
        numColsToDraw = size.x / tileSize + 2;
        numRows = tileMap.getNumRows();
        numCols = tileMap.getNumCols();

        noise = new OpenSimplexNoise();
    }

    /**
     * Set position of the camera and update the new column and row offset.
     * Takes in account the tween, for smooth panning.
     *
     * @param x new x position
     * @param y new y position
     */
    public void setPosition(double x, double y)
    {
        position.x += (x - position.x) * tween * Time.deltaTime;
        position.y += (y - position.y) * tween * Time.deltaTime;
        fixBounds();
        colOffset = (int) -position.x / tileSize;
        rowOffset = (int) -position.y / tileSize;
    }

    /**
     * Set position of the camera and update the new column and row offset.
     * Takes in account the tween, for smooth panning but ignores time slows etc.
     *
     * @param x new x position
     * @param y new y position
     */
    public void setPositionNoDeltaTime(double x, double y)
    {
        position.x += (x - position.x) * tween;
        position.y += (y - position.y) * tween;
        colOffset = (int) -position.x / tileSize;
        rowOffset = (int) -position.y / tileSize;
    }

    public void setPositionNoTween(double x, double y)
    {
        position.x += (x - position.x);
        position.y += (y - position.y);
        fixBounds();
        colOffset = (int) -position.x / tileSize;
        rowOffset = (int) -position.y / tileSize;
    }

    /**
     * Set Bounds of the camera.
     *
     * @param i1 xmin bound
     * @param i2 ymin bound
     * @param i3 xmax bound
     * @param i4 ymax bound
     */
    public void setBounds(int i1, int i2, int i3, int i4)
    {
        xmin = size.x - i1;
        ymin = size.y - i2;
        xmax = i3;
        ymax = i4;
    }

    /**
     * Check if camera is out of bounds and put it in place.
     */
    private void fixBounds()
    {
        if (position.x < xmin) position.x = xmin;
        if (position.x > xmax) position.x = xmax;
        if (position.y < ymin) position.y = ymin;
        if (position.y > ymax) position.y = ymax;
    }

    /**
     * Iterate through every tile on screen (plus other 2 for padding) and renders them.
     *
     * @param g graphic context to draw on
     */
    public void draw(Graphics2D g)
    {
        for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++)
        {
            if (row >= numRows) break;
            for (int col = colOffset; col < colOffset + numColsToDraw; col++)
            {
                if (col >= numCols) break;
                if (tileMap.isTransparent(row, col)) continue;
                g.drawImage(tileMap.printTile(col, row), (int) position.x + col * tileSize, (int) position.y + row * tileSize, null);
            }
        }
    }

    /**
     * Update the current position so that it follows the Player.
     */
    public void update()
    {
        setPosition(GamePanel.WIDTH / 2 - player.getPosition().x, GamePanel.HEIGHT / 2 - player.getPosition().y);
        if (player.isHitByEnemy())
        {
            shake(50, 12);
            player.setHitByEnemy(false);
        }
        if (player.getHitEnemy())
        {
            sliceAnim(18, 10);
            player.setHitEnemy(false);
        }
        if (elapsed < duration)
        {
            float percentComplete = (float)elapsed/(float)duration;
            float damper = 1.0f - Math.min(Math.max(4.0f * percentComplete - 3.0f, 0.0f), 1.0f);
            double x = noise.eval(((double)Time.getCurrentTime()) * 0.0008, 0) * magnitude * damper;
            double y = noise.eval(0, ((double)Time.getCurrentTime()) * 0.0009) * magnitude * damper;
            setPositionNoDeltaTime(position.x + x, position.y + y);
            elapsed++;
        }
        if (elapsedSlice < durationSlice)
        {
            float percentComplete = (float)elapsedSlice/(float)durationSlice;
            double x = 2 * magnitudeSlice;
            double y = Math.random() * -0.2 * magnitudeSlice;
            if (percentComplete <= 0.5)
            {
                x *= percentComplete * 2;
                y *= percentComplete * 2;
            }
            else
            {
                x *= 2 - percentComplete * 2;
                y *= 2 - percentComplete * 2;
            }
            if (!player.isFacingRight())
            {
                setPositionNoDeltaTime(position.x + x, position.y - y);
            }
            else
            {
                setPositionNoDeltaTime(position.x - x, position.y - y);
            }
            elapsedSlice++;
        }
        tileMap.cameraPos = this.position;
    }

    public void updateNoDeltaTime()
    {
        setPositionNoTween(GamePanel.WIDTH / 2 - player.getPosition().x, GamePanel.HEIGHT / 2 - player.getPosition().y);
        tileMap.cameraPos = this.position;
    }

    public void shake(float magnitude, int duration)
    {
        this.magnitude = magnitude;
        this.duration = duration;
        elapsed = 0;
    }

    public void sliceAnim(float magnitude, int duration)
    {
        this.magnitudeSlice = magnitude;
        this.durationSlice = duration;
        elapsedSlice = 0;
    }

    public Vector2 getPosition() {
        return position;
    }
}
