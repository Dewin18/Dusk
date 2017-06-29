package Main;

import Entity.Player;
import TileMap.TileMap;
import TileMap.Vector2;
import TileMap.Vector2i;

import java.awt.*;

public class Camera
{
    // references
    private TileMap tileMap;
    private Player player;

    // position and size
    private Vector2 position = new Vector2(0, 0);
    private Vector2i size;
    private double scale;
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

    public Camera(TileMap tileMap, Player player)
    {
        this.tileMap = tileMap;
        this.player = player;
        size = new Vector2i(GamePanel.WIDTH, GamePanel.HEIGHT);
        tileSize = tileMap.getTileSize();

        setBounds(tileMap.getWidth() - tileSize, tileMap.getHeight() - tileSize, 0, 0);
        System.out.println(tileMap.getHeight());
        System.out.println(tileMap.getWidth());

        numRowsToDraw = size.y / tileSize + 2;
        numColsToDraw = size.x / tileSize + 2;
        numRows = tileMap.getNumRows();
        numCols = tileMap.getNumCols();
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
        tileMap.cameraPos = this.position;

    }
}
