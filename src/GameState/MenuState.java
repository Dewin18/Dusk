package GameState;

import Audio.JukeBox;
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

        initFonts();
        JukeBox.load("/Audio/title_music.mp3", "titlemusic");
        JukeBox.load("menu_pick.mp3", "menupick");
        JukeBox.load("menu_choice.mp3", "menuchoice");
        JukeBox.loop("titlemusic");
    }

    private void initFonts()
    {
        try {
            font = loadFont("Berlin Sans FB Regular.ttf", FONT_SIZE);
            boldFont = loadFont("Berlin Sans FB Bold.ttf", FONT_SIZE);
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
        JukeBox.play("menupick");
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
            JukeBox.play("menuchoice");
            currentChoice--;
            if (currentChoice == -1)
            {
                currentChoice = options.length - 1;
            }
        }
        if (KeyHandler.hasJustBeenPressed(Keys.DOWN))
        {
            JukeBox.play("menuchoice");
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

}
