package Handlers;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;

public class FontHandler
{
    private static String Regular = "Berlin Sans FB Regular.ttf";
    private static String DemiBold = "Berlin Sans FB Demi Bold.ttf";
    
    //###### MENU STATE FONTS ######
    private static Font menuStateSelectionFont;
    public static final int MENUSTATE_SELECTION_SIZE = 27;
    
    //###### OPTION STATE FONTS ######
    private static Font optionStateTitleFont;
    private static final int OPTIONSTATE_TITLE_SIZE = 25;
    
    private static Font optionStateSelectionFont;
    public static final int OPTIONSTATE_SELECTION_SIZE = 20;
    
    private static Font optionStateKeyBindingsFont;
    
    //###### GAME OVER FONTS ######
    private static Font gameOverTitleFont;
    public static final int GAMEOVER_TITLE_SIZE = 45;
    
    private static Font gameOverSelectionFont;
    public static final int GAMEOVER_SELECTION_SIZE = 25;
    
    //###### PAUSE FONTS ######
    private static Font pauseTitleFont;
    public static final int PAUSE_TITLE_SIZE = 45;
    
    private static Font pauseSelectionFont;
    public static final int PAUSE_SELECTION_SIZE = 25;
    
    //###### HUD FONT ######
    private static Font hudFont;
    public static final int HUD_TEXT_SIZE = 25; 
  
    
    static
    {
        initGameFonts();
    }
    
    private static void initGameFonts()
    {
        try
        {
            //MENU STATE FONTS
            menuStateSelectionFont = loadFont(Regular, MENUSTATE_SELECTION_SIZE);
            
            //OPTION STATE FONTS
            optionStateTitleFont =  loadFont(DemiBold, OPTIONSTATE_TITLE_SIZE);
            optionStateSelectionFont = loadFont(Regular, OPTIONSTATE_SELECTION_SIZE);
            optionStateKeyBindingsFont = new Font(Regular, Font.PLAIN, 20);
            
            //GAME OVER FONTS
            gameOverTitleFont = loadFont(DemiBold, GAMEOVER_TITLE_SIZE);
            gameOverSelectionFont = loadFont(Regular, GAMEOVER_SELECTION_SIZE);
            
            //PAUSE FONTS
            pauseTitleFont = loadFont(DemiBold, PAUSE_TITLE_SIZE);
            pauseSelectionFont = loadFont(Regular, PAUSE_SELECTION_SIZE);
            
            //HUD FFONT
            hudFont = loadFont(Regular, HUD_TEXT_SIZE);
        }
        catch (IOException | FontFormatException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Draw a String centered in the middle of a Rectangle.
     *
     * @param g    The Graphics instance.
     * @param text The String to draw.
     * @param rect The Rectangle to center the text in.
     */
    public static void drawCenteredString(Graphics g, String text,
            Rectangle rect, Font font)
    {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2)
                + metrics.getAscent();
        // Set the font
        g.setFont(font);
        // Draw the String
        g.drawString(text, x, y);
    }

    /**
     * Loads a font and returns it.
     *
     * @param name name of the font found in /Fonts
     * @param size size of the font
     * @return loaded font
     * @throws IOException if there is no such file
     * @throws FontFormatException if the format of the font is not accepted
     */
     public static Font loadFont(String name, int size) throws IOException, FontFormatException
     {
         Font f;
         f = Font.createFont(Font.TRUETYPE_FONT, FontHandler.class.getResourceAsStream("/Fonts/" + name));
         f = f.deriveFont(Font.PLAIN, size);
         return f;
     }
     
     public static Font getMenuStateFont()
     {
         return menuStateSelectionFont;
     }

     public static Font getOptionStateSelectionFont()
     {
         return optionStateSelectionFont;
     }
     
     public static Font getOptionStateTitleFont()
     {
         return optionStateTitleFont;
     }
     
     public static Font getOptionStateKeyBindingsFont()
     {
         return optionStateKeyBindingsFont;
     }

     public static Font getGameOverFont()
     {
         return gameOverTitleFont;
     }

     public static Font getGameOverSelectionFont()
     {
         return gameOverSelectionFont;
     }

     public static Font getPauseTitleFont()
     {
         return pauseTitleFont;
     }

     public static Font getPauseSelectionFont()
     {
         return pauseSelectionFont;
     }
     
     public static Font getHudFont()
     {
         return hudFont;
     }
     
}
