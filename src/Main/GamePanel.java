package Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;
import java.security.Key;
import java.util.Arrays;

import javax.swing.JPanel;

import GameState.GameStateManager;
import Handlers.KeyHandler;

public class GamePanel extends JPanel implements Runnable, KeyListener
{
    // dimensions
    public static final int WIDTH = 1500;
    public static final int HEIGHT = 700;
    public static final int SCALE = 1;

    // game thread
    private Thread thread;
    private boolean running;

    // image
    private BufferedImage image;
    private Graphics2D g;


    // manager
    private GameStateManager gsm;
    private KeyHandler keyHandler;

    public GamePanel()
    {
        super();
        setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        setFocusable(true);
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

        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();

        running = true;

        gsm = new GameStateManager();
    }

    public void run()
    {
        init();
        long wait;

        // game loop
        while (running)
        {
            Time.updateDeltaTime();
            update();
            draw();
            drawToScreen();
            wait = Time.calculateWaitTime();
            if (wait < 0) wait = 5;
            try
            {
                Thread.sleep(wait);
            }
            catch (Exception e)
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
        gsm.draw(g);
    }

    private void drawToScreen()
    {
        Graphics g2 = getGraphics();
        g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
        g2.dispose();
    }

    public void keyTyped(KeyEvent key) {}

    public void keyPressed(KeyEvent key)
    {
        KeyHandler.keySet(key.getKeyCode(), true);
    }

    public void keyReleased(KeyEvent key)
    {
        KeyHandler.keySet(key.getKeyCode(), false);
    }

}
