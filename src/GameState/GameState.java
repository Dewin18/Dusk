package GameState;

import java.awt.*;
import java.io.IOException;

public abstract class GameState
{
    protected GameStateManager gsm;

    public abstract void init();

    public abstract void update();

    public abstract void draw(java.awt.Graphics2D g);

    public abstract void handleInput();

    /**
     * Draw a String centered in the middle of a Rectangle.
     *
     * @param g    The Graphics instance.
     * @param text The String to draw.
     * @param rect The Rectangle to center the text in.
     */
    void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
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
    Font loadFont(String name, int size) throws IOException, FontFormatException
    {
        Font f;
        f = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/Fonts/" + name));
        f = f.deriveFont(Font.PLAIN, size);
        return f;
    }
}
