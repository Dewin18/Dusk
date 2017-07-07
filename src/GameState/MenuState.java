package GameState;

import Handlers.KeyHandler;
import Handlers.Keys;
import Main.GamePanel;
import TileMap.Background;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class MenuState extends GameState
{

    private final int FONT_SIZE = 27;
    private Background bg;
    private int currentChoice = 0;
    private String[] options = {"Start Game", "Options", "Exit"};
    private Font font;
    private Font boldFont;
    private BufferedImage titleImage;

    public MenuState(GameStateManager gsm)
    {
        this.gsm = gsm;
        bg = new Background("menu bg.jpg");

        InputStream is = getClass().getResourceAsStream("/Fonts/Berlin Sans FB Regular.ttf");
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont(Font.PLAIN, FONT_SIZE);
            boldFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/Fonts/Berlin Sans FB Bold.ttf"));
            boldFont = boldFont.deriveFont(Font.PLAIN, FONT_SIZE);
            titleImage = ImageIO.read(getClass().getResourceAsStream("/Fonts/dusk title.png"));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public void init()
    {
    }

    public void draw(Graphics2D g)
    {
        // draw bg
        bg.draw(g);

        // draw title
        g.drawImage(titleImage, GamePanel.WIDTH / 2 - titleImage.getWidth() / 2, 130, null);

        // draw menu options
        g.setColor(Color.WHITE);
        for (int i = 0; i < options.length; i++)
        {
            if (i == currentChoice)
            {
                drawCenteredString(g,
                        " - " + options[i] + " - ",
                        new Rectangle(0, (int) (GamePanel.HEIGHT * 0.6 + i * FONT_SIZE * 1.6), GamePanel.WIDTH, FONT_SIZE),
                        font.deriveFont(Font.PLAIN, FONT_SIZE + 3));
            } else
            {
                drawCenteredString(g,
                        options[i],
                        new Rectangle(0, (int) (GamePanel.HEIGHT * 0.6 + i * FONT_SIZE * 1.6), GamePanel.WIDTH, FONT_SIZE),
                        font);
            }
        }
    }

    private void select()
    {
        if (currentChoice == 0)
        {
            gsm.setState(GameStateManager.LEVEL1STATE);
        }
        if (currentChoice == 1)
        {
           gsm.setState(GameStateManager.OPTIONSTATE);
        }
        if (currentChoice == 2)
        {
            System.exit(0);
        }
    }

    public void handleInput()
    {
        if (KeyHandler.hasJustBeenPressed(Keys.ENTER)) select();
        
        if (KeyHandler.hasJustBeenPressed(Keys.UP))
        {
            currentChoice--;
            if (currentChoice == -1)
            {
                currentChoice = options.length - 1;
            }
        }
        if (KeyHandler.hasJustBeenPressed(Keys.DOWN))
        {
            currentChoice++;
            if (currentChoice == options.length)
            {
                currentChoice = 0;
            }
        }
    }

    public void update()
    {
        handleInput();
    }


    /**
     * Draw a String centered in the middle of a Rectangle.
     *
     * @param g    The Graphics instance.
     * @param text The String to draw.
     * @param rect The Rectangle to center the text in.
     */
    private void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
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
}
