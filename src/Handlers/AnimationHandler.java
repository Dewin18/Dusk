package Handlers;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import Main.GamePanel;

public class AnimationHandler
{
    //Game Over animation
    private static  int gameOverTextPosition;
    
    //Blink animation
    private static boolean isCompletelyVisible;
    private static float BLINK_MAX_LIMIT; 
    private static float BLINK_MIN_LIMIT; 
    private static float blinkScale;
    private static float BLINK_SPEED;
    
    static
    {
        gameOverTextPosition = 70;
        
        isCompletelyVisible = true;
        BLINK_MAX_LIMIT = 1f;
        BLINK_MIN_LIMIT = 0.1f;
        blinkScale = 1f;
        BLINK_SPEED = 0.02f;
    }
    
    public static void startBlinkAnimation(Graphics2D g)
    {
        if(blinkScale == BLINK_MAX_LIMIT) isCompletelyVisible = true;
        
        if(isCompletelyVisible)
        {
            if(blinkScale > BLINK_MIN_LIMIT) blinkScale -= BLINK_SPEED;
            else if(blinkScale <= BLINK_MIN_LIMIT) isCompletelyVisible = false;
        }
        else if(blinkScale < BLINK_MAX_LIMIT) blinkScale += BLINK_SPEED;

        g.setComposite(
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, blinkScale));
    }
    
    public static void drawEndingText(Graphics2D g, String message)
    {
        g.setColor(new Color(0, 0, 0, 80));
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        g.setColor(Color.WHITE);
        FontHandler.drawCenteredString(g, message,
                new Rectangle(0,
                        GamePanel.HEIGHT / 2 + gameOverTextPosition - FontHandler.GAMEOVER_TITLE_SIZE,
                        GamePanel.WIDTH, FontHandler.GAMEOVER_TITLE_SIZE),
                FontHandler.getGameOverFont());
    }
    
    public static int getEndingTextPosition()
    {
        return gameOverTextPosition;
    }
    
    public static void decreaseEndingTextPosition()
    {
        gameOverTextPosition --;
    }
    
    public static void resetGameOverTextPosition()
    {
        gameOverTextPosition = 70;
    }
}
