package Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class HUD
{
    private Player player;
    
    private int lives;
    private float healthBar;
    private float healthInterval;
    private Image liveSymbol;
    private int liveSymbolPosition;
    private boolean liveLost;
    private boolean gameOver = false;
    
    //health blink animation
    private boolean healthVisibility = true;
    private float blinkSpeedLimit = 1f;
    private final float BLINK_SPEED = 0.02f;
    
    private Font hudFont;

    private boolean pause = false;
    
    public HUD(Player player, Font hudFont)
    {
        this.player = player;
        this.hudFont = hudFont;
        
        healthBar = 100;
        healthInterval = 80; // the player lose 20 health every hit
        
        //dusk life symbol position at Y-AXIS
        liveSymbolPosition = 10;
        
        try
        {
            liveSymbol = ImageIO.read(
                    getClass().getResourceAsStream("/Backgrounds/lsymbol.png"));

            liveSymbol = liveSymbol.getScaledInstance(50, 40,
                    Image.SCALE_SMOOTH);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g)
    {
        drawHUD(g);
    }
    
    public void handleHealthBar()
    {
        healthBar -= 1;

        if (healthBar <= healthInterval)
        {
            player.setHitByEnemy(false);
            healthInterval -= 20;
        }

        if (healthBar == 0)
        {
            lives--;
            healthBar = 100; // fill HealthBar slowly
            healthInterval = 80;
        }

        if (lives < 0)
        {
            lives = 0;
            gameOver = true;
        }
    }

    private void drawHUD(Graphics2D g)
    {
        g.drawImage(liveSymbol, 55, 10, null);

        g.setFont(hudFont);

        g.drawString(String.valueOf(lives) + " x", 10, 40);

        g.setColor(Color.BLACK);
        g.drawRect(120, 20, 100, 20);

        if (healthBar >= 65)
            g.setColor(Color.GREEN);
        else if (healthBar >= 35)
            g.setColor(Color.YELLOW);
        else
        {
            g.setColor(Color.RED);
            if (!pause ) blink(g);
        }

        g.fillRect(120, 20, (int) healthBar, 20);

        if (healthBar <= 1)
        {
            liveLost = true;
        }

        if (liveLost)
        {
            float op = 0.5f;
            g.setComposite(
                    AlphaComposite.getInstance(AlphaComposite.SRC_OVER, op));
            g.drawImage(liveSymbol, 55, liveSymbolPosition, null);
            drawLiveLostAnimation(g);
        }

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        g.setColor(Color.BLACK);
        for (int i = 140; i <= 200; i += 20) g.drawLine(i, 20, i, 40);
    }

    private void blink(Graphics2D g)
    {
        if(blinkSpeedLimit == 1.0f) healthVisibility = true;
        
        if(healthVisibility)
        {
            if(blinkSpeedLimit > 0.1f) blinkSpeedLimit -= BLINK_SPEED;
            if(blinkSpeedLimit <= 0.1f) healthVisibility = false;
        }
        else if(blinkSpeedLimit < 1.0f) blinkSpeedLimit += BLINK_SPEED;

        g.setComposite(
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, blinkSpeedLimit));
    }

    private void drawLiveLostAnimation(Graphics2D g)
    {
        liveSymbolPosition -= 1;

        if (liveSymbolPosition == -100)
        {
            liveSymbolPosition = 10;
            liveLost = false;
        }
    }
    
    public void setLives(int lives)
    {
        this.lives = lives;
    }
    
    public boolean isGameOver()
    {
        return gameOver;
    }
    
    public void setGameOver(boolean state)
    {
        gameOver = state;
    }
    
    public void setHealthBlinking(boolean blinking)
    {
        pause = blinking;
    }
}
