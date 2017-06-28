package TileMap;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class Background
{
    private BufferedImage image;

    private Vector2 position = new Vector2(0, 0);
    private Vector2 scrollSpeed = new Vector2(0.3,0.3);
    private Vector2i dimension;
    private Vector2 scale;

    public Background(String s)
    {
        try
        {
            image = ImageIO.read(getClass().getResourceAsStream("/Backgrounds/" + s));
            dimension = new Vector2i(image.getWidth(), image.getHeight());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setPosition(Vector2 position)
    {
        setPosition(position.x, position.y);
    }

    public void setPosition(double x, double y)
    {
        position.x = (x * scrollSpeed.x) % dimension.x;
        position.y = (y * scrollSpeed.y) % dimension.y;
    }

    public void setScrollSpeed(Vector2 scrollSpeed)
    {
        this.scrollSpeed = scrollSpeed;
    }

    public void draw(Graphics2D g)
    {
        g.drawImage(image, (int)position.x, (int)position.y, null);
        g.drawImage(image, (int)position.x + dimension.x, (int)position.y, null);
    }
}
