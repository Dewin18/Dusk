package TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Background
{
    private BufferedImage image;

    private Vector2 position = new Vector2(0, 0);
    private Vector2 scrollSpeed = new Vector2(0.3,0.3);
    private Vector2i dimension;
    private Vector2 scale = new Vector2(1, 1);
    private Vector2 offset = new Vector2(0, 0);

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
        setPosition(position.x + offset.x, position.y + offset.y);
    }

    private void setPosition(double x, double y)
    {
        position.x = (x * scrollSpeed.x) % dimension.x;
        position.y = (y * scrollSpeed.y);
    }

    public void setScrollSpeed(Vector2 scrollSpeed)
    {
        this.scrollSpeed = scrollSpeed;
    }

    public void setScale(Vector2 scale) {
        this.scale = scale;
    }

    public void setOffset(Vector2 offset) {
        this.offset = offset;
    }

    public void draw(Graphics2D g)
    {
        g.drawImage(image,
                (int) position.x,
                (int) position.y,
                (int) (dimension.x * scale.x),
                (int) (dimension.y * scale.y),
                null);
        g.drawImage(image,
                (int) position.x + (int) (dimension.x * scale.x),
                (int) position.y + 0,
                (int) (dimension.x * scale.x) + 0,
                (int) (dimension.y * scale.y) + 0,
                null);
        g.drawImage(image,
                (int) position.x + (int) (dimension.x * scale.x) * 2,
                (int) position.y + 0,
                (int) (dimension.x * scale.x) + 0,
                (int) (dimension.y * scale.y) + 0,
                null);
    }
}
