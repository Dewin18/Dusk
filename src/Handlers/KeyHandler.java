package Handlers;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import Main.GamePanel;

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
    
    //Key Map for special keys (arrows and space)
    private static HashMap<Integer, String> specialKeysMap;
    
    //store all new selected keys 
    private static String[] newKeys = new String[8];

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
    
    /**
     * check for valid keys
     * 
     * @param key key selected
     * @return true if key is valid, false otherwise
     * 
     * Key [32]       = SPACEBAR
     * Key [37 - 40]  = ARROWS
     * Key [48 - 57]  = 0 - 9  
     * Key [48 - 57]  = A - Z
     */
    public static boolean isValidKey(int key)
    {
        return (key == 32) || (key >= 37 && key <= 40) || 
               (key >= 48 && key <= 57) || (key >= 65 && key <= 90); 
    }
    
    //check for arrows and space values
    public static boolean isSpecialKey(int key)
    {
        return (key == 32) || (key >= 37 && key <= 40);
    }
    
    
    public static String getSpecialKey(int key)
    {
        initKeyMap();
        return specialKeysMap.get(key);
    }

    private static void initKeyMap()
    {
        specialKeysMap = new HashMap<>();
        specialKeysMap.put(32, "SPACE");
        specialKeysMap.put(37, "←");
        specialKeysMap.put(38, "↑");
        specialKeysMap.put(39, "→");
        specialKeysMap.put(40, "↓");
    }
    
    public static void setNewKeys(String[] keys)
    {
        newKeys = keys;
    }
    
    public static String[] getNewKeys()
    {
        for(int i =0; i < 8; i++)
        {
            System.out.println(newKeys[i]);
        }
        
        return newKeys;
    }
}
