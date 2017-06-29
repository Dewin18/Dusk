package GameState;

import Handlers.KeyHandler;
import Handlers.Keys;
import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

import Main.Game;
import Main.GamePanel;

public class MenuState extends GameState
{

    private Background bg;

    private int currentChoice = 0;
    private String[] options = {"Start", "Settings", "Quit"};

    private Color titleColor;
    private Font titleFont;

    private Font font;

    public MenuState(GameStateManager gsm)
    {
        this.gsm = gsm;
        bg = new Background("/Backgrounds/dark.png");
        titleColor = new Color(128, 0, 0);
        titleFont = new Font("Century Gothic", Font.PLAIN, 28);
        try
        {
            bg = new Background("/Backgrounds/dark.png");

            titleColor = new Color(128, 0, 0);
            titleFont = new Font("Century Gothic", Font.PLAIN, 18);

            font = new Font("Arial", Font.PLAIN, 25);
        } catch (Exception e)
        {
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
        g.setColor(titleColor);
        g.setFont(titleFont);

        g.drawString("Platformer v.1", GamePanel.WIDTH - 250, GamePanel.HEIGHT - 190);

        // draw menu options
        g.setFont(font);
        
        for (int i = 0; i < options.length; i++)
        {
            if (i == currentChoice)
            {
                g.setColor(Color.GREEN);
            } 
            else
            {
                g.setColor(Color.LIGHT_GRAY);
            }
            g.drawString(options[i], (int) (GamePanel.WIDTH / 2.5), GamePanel.HEIGHT - 120 + i * 45);
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
}
