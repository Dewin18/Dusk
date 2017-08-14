package Entity;

import javax.imageio.ImageIO;

import Handlers.AnimationHandler;
import Handlers.FontHandler;

import java.awt.*;

public class HUD
{
    private float healthBar;
    private Image liveSymbol;
    private int liveSymbolPosition;
    private boolean liveLost;
    
    public HUD()
    {
        healthBar = 100;
        
        //dusk life symbol position at Y-AXIS
        liveSymbolPosition = 10;
        
        try
        {
            liveSymbol = ImageIO.read(
                    getClass().getResourceAsStream("/HUD/lsymbol.png"));

            liveSymbol = liveSymbol.getScaledInstance(50, 40,
                    Image.SCALE_SMOOTH);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g, int health, int lives)
    {
        drawHUD(g, lives);
        handleHealthBar(g, health);
        
        if(liveLost)
        {
            drawLiveLostAnimation(g);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        }
    }
    
    public void handleHealthBar(Graphics2D g, int border)
    {
        if(healthBar > border)
        {
            healthBar -= 1;
        }
        
        if(healthBar <= 0)
        {
            liveLost = true;
        }
    }
    
    private void drawHUD(Graphics2D g, int lives)
    {
        g.drawImage(liveSymbol, 55, 10, null);
        g.setFont(FontHandler.getHudFont());
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
            AnimationHandler.startBlinkAnimation(g);
        }

        g.fillRect(120, 20, (int) healthBar, 20);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        g.setColor(Color.BLACK);
        for (int i = 140; i <= 200; i += 20) g.drawLine(i, 20, i, 40);
    }

    private void drawLiveLostAnimation(Graphics2D g)
    {
        float op = 0.5f;
        g.setComposite(
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, op));
        g.drawImage(liveSymbol, 55, liveSymbolPosition, null);
        
        liveSymbolPosition -= 1;

        if (liveSymbolPosition == -100)
        {
            liveSymbolPosition = 10;
            liveLost = false;
        }
    }
    
    public void setHealthBarValue(float health)
    {
        healthBar = health;
    }
    
    public float getHealthBarValue()
    {
        return healthBar;
    }
}
