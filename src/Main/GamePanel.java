package Main;

import GameState.GameStateManager;
import Handlers.KeyHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable, KeyListener
{
    // dimensions
    public static final int WIDTH = 1300;
    public static final int HEIGHT = 600;
    public static final int SCALE = 1;
    private static final long serialVersionUID = 1L;
    public Canvas canvas;
    public BufferStrategy strategy;
    // game thread
    private Thread thread;
    private boolean isRunning;
    // image
    private BufferedImage image;
    private Graphics2D g;
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

    //runs automatically
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
            Thread t1 = new Thread(this::update);
            Thread t2 = new Thread(this::draw);
            t1.run();
                t2.join();
            } catch (InterruptedException e)
            update();
            draw();
            g.dispose();
            strategy.show();
            wait = Time.calculateWaitTime();
            
            if (wait < 0) {
                wait = 6;
            }
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
