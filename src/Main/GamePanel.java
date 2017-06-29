package Main;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.security.Key;
import java.util.Arrays;

import javax.swing.JPanel;

import GameState.GameStateManager;
import GameState.OptionState;
import Handlers.KeyHandler;

public class GamePanel extends JPanel implements Runnable, KeyListener
{
    private static final long serialVersionUID = 1L;
    // dimensions
    public static final int WIDTH = 1300;
    public static final int HEIGHT = 600;
    public static final int SCALE = 1;

    // game thread
    private Thread thread;
    private boolean isRunning;

    // image
    private BufferedImage image;
    private Graphics2D g;
    public Canvas canvas;
    public BufferStrategy strategy;

    // manager
    private GameStateManager gsm;

    public GamePanel()
    {
        super();
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setFocusable(true);
        setIgnoreRepaint(true);
        requestFocus();
    }

    public void addNotify()
    {
        super.addNotify();
        if (thread == null)
        {
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }
    }

    private void init()
    {
        //image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        //g = (Graphics2D) image.getGraphics();
        //g = (Graphics2D)strategy.getDrawGraphics();
        isRunning = true;

        gsm = new GameStateManager();
    }

    public void run()
    {
        init();
        long wait;

        // game loop
        while (isRunning)
        {
            Time.updateDeltaTime();
            g = (Graphics2D)strategy.getDrawGraphics();
            update();
            draw();
            //drawToScreen();
            g.dispose();
            strategy.show();
            wait = Time.calculateWaitTime();
            if (wait < 0) wait = 10;
            try
            {
                Thread.sleep(wait);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void update()
    {
        gsm.update();
        KeyHandler.update();
    }

    private void draw()
    {
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHints(rh);
        gsm.draw(g);
    }

    private void drawToScreen()
    {
        Graphics g2 = getGraphics();
        g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        g2.dispose();
    }

    public void keyTyped(KeyEvent key)
    {
    }

    public void keyPressed(KeyEvent key)
    {
        KeyHandler.keySet(key.getKeyCode(), true);
        KeyHandler.setKeyPressed(key.getKeyCode());
    }

    public void keyReleased(KeyEvent key)
    {
        KeyHandler.keySet(key.getKeyCode(), false);
       
    }
}
