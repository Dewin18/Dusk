package Handlers;

import java.awt.event.KeyEvent;

/**
 * This Class handles all KeyEvents
 *
 * @author Ali & Dewin
 */
public class KeyHandler
{

    /**
     * NUM_KEYS is the number of all Keys. In this case we have 10 Keys
     */
    public static final int NUM_KEYS = Keys.KEY_COUNT;

    // Create a new keyState Array with the capacity of 10
    private static boolean keyState[] = new boolean[NUM_KEYS];
    // Create a new prevKeyState Array with the capacity of 10
    private static boolean prevKeyState[] = new boolean[NUM_KEYS];


    public static void keySet(int i, boolean b)
    {
        if (i == KeyEvent.VK_UP) keyState[Keys.UP] = b;
        else if (i == KeyEvent.VK_LEFT) keyState[Keys.LEFT] = b;
        else if (i == KeyEvent.VK_DOWN) keyState[Keys.DOWN] = b;
        else if (i == KeyEvent.VK_RIGHT) keyState[Keys.RIGHT] = b;
        else if (i == KeyEvent.VK_SPACE) keyState[Keys.JUMP] = b;
        else if (i == KeyEvent.VK_A) keyState[Keys.ATTACK] = b;
        else if (i == KeyEvent.VK_S) keyState[Keys.DASH] = b;
        else if (i == KeyEvent.VK_D) keyState[Keys._SOMEOTHERBUTTON] = b;
        else if (i == KeyEvent.VK_ENTER) keyState[Keys.ENTER] = b;
        else if (i == KeyEvent.VK_ESCAPE) keyState[Keys.ESCAPE] = b;
    }

    public static void update()
    {
        for (int i = 0; i < NUM_KEYS; i++)
        {
            prevKeyState[i] = keyState[i];
        }
    }

    public static boolean isPressed(int i)
    {
        return keyState[i];
    }

    public static boolean hasJustBeenPressed(int i)
    {
        return keyState[i] && !prevKeyState[i];
    }

    public static boolean anyKeyPress()
    {
        for (int i = 0; i < NUM_KEYS; i++)
        {
            if (keyState[i]) return true;
        }
        return false;
    }
}
