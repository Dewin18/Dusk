package Main;

import GameState.GameStateManager;
import Handlers.KeyHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

/** PTP 2017
 * GamePanel including the main game loops, draw(g) and update().
 *
 * @author Dewin Bagci, Ali popa
 * @version 14.08.
 * @since 31.05.
 */
public class GamePanel extends JPanel implements Runnable, KeyListener
{
    // dimensions
    public static final int WIDTH = 1300;
    public static final int HEIGHT = 600;
    public static final int SCALE = 1;
    private static final long serialVersionUID = 1L;
    public BufferStrategy strategy;

    // game thread
    private Thread thread;
    private boolean isRunning;

    // image
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
        start();
    }

    private void start()
    {
        isRunning = true;
        thread = new Thread(this);
        addKeyListener(this);
        thread.start();
    }
    

    public void run()
    {
        gsm = new GameStateManager();   
        long wait;

        // game loop
        while (isRunning)
        {
            Time.updateDeltaTime();
            g = (Graphics2D)strategy.getDrawGraphics();
            update();
            draw();
            g.dispose();
            strategy.show();
            wait = Time.calculateWaitTime();
            
            if (wait < 0) {
                wait = 6;
            }
            try {
                Thread.sleep(wait);
            } catch (Exception e) {
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
    
    public boolean getGameLoopState()
    {
        return isRunning;
    }

    public GameStateManager getGsm()
    {
        return gsm;
    }
}
