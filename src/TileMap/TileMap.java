package TileMap;

import Helpers.Vector2;
import Helpers.Vector2i;
import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/** PTP 2017
 * The class representing a level. Includes the position of tiles and their tileset.
 *
 * @author Ali Popa
 * @version 13.08.
 * @since 31.05.
 */
public class TileMap
{
    // position
    public Vector2 cameraPos = new Vector2(0, 0);
    private Vector2 position = new Vector2(0, 0);

    // bounds
    private int xmin;
    private int ymin;
    private int xmax;
    private int ymax;

    // map
    private int[][] map;
    private int tileSize;
    private int numRows;
    private int numCols;
    private int width;
    private int height;

    // tileset
    private BufferedImage tileset;
    private int numTilesAcross;
    private Tile[][] tiles;

    // drawing
    private int rowOffset;
    private int colOffset;
    private int numRowsToDraw;
    private int numColsToDraw;

    public TileMap(int tileSize)
    {
        this.tileSize = tileSize;
        numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
        numColsToDraw = GamePanel.WIDTH / tileSize + 2;
    }

    public void loadTiles(String s)
    {
        try
        {
            tileset = ImageIO.read(getClass().getResourceAsStream(s));
            numTilesAcross = tileset.getWidth() / tileSize;
            tiles = new Tile[4][numTilesAcross];

            BufferedImage subimage;
            for (int col = 0; col < numTilesAcross; col++)
            {
                for (int row = 0; row < 4; row++) {
                    subimage = tileset.getSubimage(col * tileSize, row * tileSize, tileSize, tileSize);
                    if (row == 3) {
                        tiles[row][col] = new Tile(subimage, TileType.ONEWAY);
                    } else if (row == 0) {
                        tiles[row][col] = new Tile(subimage, TileType.EMPTY);
                    } else {
                        tiles[row][col] = new Tile(subimage, TileType.BLOCKED);
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void loadMap(String s)
    {
        try
        {
            InputStream in = getClass().getResourceAsStream(s);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            numCols = Integer.parseInt(br.readLine());
            numRows = Integer.parseInt(br.readLine());
            map = new int[numRows][numCols];
            width = numCols * tileSize;
            height = numRows * tileSize;

            xmin = GamePanel.WIDTH - width;
            xmax = 0;
            ymin = GamePanel.HEIGHT - height;
            ymax = 0;

            String delims = "\\s+";
            for (int row = 0; row < numRows; row++)
            {
                String line = br.readLine();
                String[] tokens = line.split(delims);
                for (int col = 0; col < numCols; col++)
                {
                    map[row][col] = Integer.parseInt(tokens[col]);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public int getTileSize()
    {
        return tileSize;
    }

    public int getX()
    {
        return (int) position.x;
    }

    public int getY()
    {
        return (int) position.y;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public BufferedImage printTile(int col, int row)
    {
        int rc = map[row][col];
        int r = rc / numTilesAcross;
        int c = rc % numTilesAcross;
        BufferedImage derp = tiles[r][c].getImage();
        return derp;
    }

    public TileType getType(int row, int col)
    {
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) return TileType.BLOCKED;
        int rc = map[row][col];
        int r = rc / numTilesAcross;
        int c = rc % numTilesAcross;
        return tiles[r][c].getType();
    }

    public TileType getType(Vector2i tileCoords)
    {
        int rc = map[tileCoords.x][tileCoords.y];
        int r = rc / numTilesAcross;
        int c = rc % numTilesAcross;
        return tiles[r][c].getType();
    }

    public boolean isObstacle(int x, int y)
    {
        return getType(y, x) == TileType.BLOCKED;
    }

    public boolean isGround(int x, int y)
    {
        return (getType(y, x) == TileType.BLOCKED || getType(y, x) == TileType.ONEWAY);
    }

    public boolean isPlatform(int x, int y)
    {
        return getType(y, x) == TileType.ONEWAY;
    }

    public boolean isTransparent(int x, int y)
    {
        return map[x][y] == 0;
    }

    public boolean isEmpty(int x, int y)
    {
        return getType(y, x) == TileType.EMPTY;
    }

    public Vector2i getMapTileAtPoint(Vector2 point)
    {
        return new Vector2i((int) (point.x - position.x + tileSize / 2), (int) (point.y - position.y + tileSize / 2));
    }

    public int getMapTileXAtPoint(double x)
    {
        return (int) Math.round((x - position.x - tileSize / 2) / tileSize);
    }

    public int getMapTileYAtPoint(double y)
    {
        return (int) Math.round((y - position.y - tileSize / 2) / tileSize);
    }

    public Vector2 getMapTileCoords(int row, int col)
    {
        return new Vector2(row * tileSize + position.x, col * tileSize + position.y);
    }

    public Vector2 getMapTileCoords(Vector2i tileCoords)
    {
        return new Vector2(tileCoords.x * tileSize + position.x, tileCoords.y * tileSize + position.y);
    }

    public int getNumRows()
    {
        return numRows;
    }

    public int getNumCols()
    {
        return numCols;
    }

    public int getMapTile(int x, int y)
    {
        return map[x][y];
    }

    public void setPosition(double x, double y)
    {
        this.position.x += (x - this.position.x);
        this.position.y += (y - this.position.y);

        fixBounds();

        colOffset = (int) -this.position.x / tileSize;
        rowOffset = (int) -this.position.y / tileSize;

    }

    private void fixBounds()
    {
        if (position.x < xmin) position.x = xmin;
        if (position.y < ymin) position.y = ymin;
        if (position.x > xmax) position.x = xmax;
        if (position.y > ymax) position.y = ymax;
    }

    public void draw(Graphics2D g)
    {
        for (int row = rowOffset; row < rowOffset + numRowsToDraw; row++)
        {

            if (row >= numRows) break;

            for (int col = colOffset; col < colOffset + numColsToDraw; col++)
            {

                if (col >= numCols) break;

                if (map[row][col] == 0) continue;

                int rc = map[row][col];
                int r = rc / numTilesAcross;
                int c = rc % numTilesAcross;

                g.drawImage(tiles[r][c].getImage(), (int) position.x + col * tileSize, (int) position.y + row * tileSize, null);
            }
        }
    }
}



















