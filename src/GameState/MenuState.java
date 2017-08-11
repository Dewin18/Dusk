package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Audio.JukeBox;
import Handlers.FontHandler;
import Handlers.KeyHandler;
import Handlers.Keys;
import Main.GamePanel;
import TileMap.Background;

public class MenuState extends GameState
{
    private Background bg;
    private int currentChoice = 0;
    private String[] options = {"Start Game", "Options", "Exit"};
    private BufferedImage titleImage;

    public MenuState(GameStateManager gsm)
    {
        this.gsm = gsm;
        bg = new Background("menu bg.jpg");

        initTitleImage();
        JukeBox.load("title_music.mp3", "titlemusic");
        JukeBox.load("menu_pick.mp3", "menupick");
        JukeBox.load("menu_choice.mp3", "menuchoice");
        JukeBox.loop("titlemusic");
    }

    private void initTitleImage()
    {
        try {
            titleImage = ImageIO.read(getClass().getResourceAsStream("/Fonts/dusk title.png"));
        } catch (IOException e) {
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
                FontHandler.drawCenteredString(g,
                        " - " + options[i] + " - ",
                        new Rectangle(0, (int) (GamePanel.HEIGHT * 0.6 + i * FontHandler.MENUSTATE_SELECTION_SIZE * 1.6), GamePanel.WIDTH, FontHandler.MENUSTATE_SELECTION_SIZE),
                        FontHandler.getMenuStateFont().deriveFont(Font.PLAIN, FontHandler.MENUSTATE_SELECTION_SIZE + 3));
            } else
            {
                FontHandler.drawCenteredString(g,
                        options[i],
                        new Rectangle(0, (int) (GamePanel.HEIGHT * 0.6 + i * FontHandler.MENUSTATE_SELECTION_SIZE * 1.6), GamePanel.WIDTH, FontHandler.MENUSTATE_SELECTION_SIZE),
                        FontHandler.getMenuStateFont());
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
    
    public String toString()
    {
        return "MenuState";
    }
}
