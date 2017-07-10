package Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class HUD
{
    private BufferedImage fullHeart;
    private BufferedImage emptyHeart;
    private final int HEART_WIDTH;
    private final int HEART_HEIGHT;

    private int maxHealth;
    private int currentHealth;

    public HUD(int maxHealth)
    {
        try
        {
            fullHeart = ImageIO.read(getClass().getResourceAsStream("/HUD/heart_full.png"));
            emptyHeart = ImageIO.read(getClass().getResourceAsStream("/HUD/heart_empty.png"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        this.maxHealth = maxHealth;
        currentHealth = maxHealth;
        HEART_HEIGHT = fullHeart.getHeight();
        HEART_WIDTH = fullHeart.getWidth();
    }

    public void draw(Graphics2D g)
    {
        for (int i = 0; i < currentHealth; i++)
        {
            g.drawImage(fullHeart, (int)(HEART_WIDTH*0.3 + HEART_WIDTH*1.3*i), (int)(HEART_HEIGHT*0.3), null);
        }
        for (int i = currentHealth; i < maxHealth; i++)
        {
            g.drawImage(emptyHeart, (int)(HEART_WIDTH*0.3 + HEART_WIDTH*1.3*i), (int)(HEART_HEIGHT*0.3), null);
        }
    }

    public void setCurrentHealth(int currentHealth)
    {
        this.currentHealth = currentHealth;
    }
}
