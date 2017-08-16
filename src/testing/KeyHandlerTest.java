package testing;

import static org.junit.Assert.*;

import org.junit.Test;

import Handlers.KeyHandler;

public class KeyHandlerTest
{
    private final int UP = 38;
    private final int DOWN = 40;
    private final int LEFT = 37;
    private final int RIGHT = 39;
    private final int SPACE = 32;
    private final int A = 65;
    
    
    @Test
    // test default key-bindings
    public void defaultKeyBindingsTest()
    {
        //Array contains default KeyBindings KeyCode
        int[] keyCodes = KeyHandler.getKeyCodes();
        
        assertTrue(keyCodes[0] == UP);
        assertTrue(keyCodes[1] == DOWN);
        assertTrue(keyCodes[2] == LEFT);
        assertTrue(keyCodes[3] == RIGHT);
        assertTrue(keyCodes[4] == SPACE);
        assertTrue(keyCodes[5] == A);
    }

    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void defaultKeyBindingsAmount()
    {
        KeyHandler.getKeyCodes()[6] = 0;
    }
    
    @Test 
    public void storeChoiceTest()
    {
        //default choice is 0
        assertTrue(KeyHandler.getStoredChoice(0) == 0);
        assertTrue(KeyHandler.getStoredChoice(1) == 0);
        
        KeyHandler.storeChoice(1, 0);
        KeyHandler.storeChoice(2, 1);
        
        assertTrue(KeyHandler.getStoredChoice(0) == 1);
        assertTrue(KeyHandler.getStoredChoice(1) == 2);
    }
    
    @Test (expected = ArrayIndexOutOfBoundsException.class)
    public void storeChoiceAtWrongTest()
    {
        KeyHandler.storeChoice(0, 0);
        KeyHandler.storeChoice(1, 2);
    }
    
    @Test
    // limit value analysis for all valid keys
    public void validKeyTest()
    {
        //Keys [31 - 33] = SPACE
        assertFalse(KeyHandler.isValidKey(31));
        assertTrue(KeyHandler.isValidKey(32));
        assertFalse(KeyHandler.isValidKey(33));
        
        //Keys [36 - 41] = arrows 
        assertFalse(KeyHandler.isValidKey(36));
        assertTrue(KeyHandler.isValidKey(37));
        assertTrue(KeyHandler.isValidKey(40));
        assertFalse(KeyHandler.isValidKey(41));
        
        //Keys [47 - 58] = numeric values 0 - 9
        assertFalse(KeyHandler.isValidKey(47));
        assertTrue(KeyHandler.isValidKey(48));
        assertTrue(KeyHandler.isValidKey(57));
        assertFalse(KeyHandler.isValidKey(58));
        
        //Keys [47 - 58] = alphabetic values A - Z
        assertFalse(KeyHandler.isValidKey(64));
        assertTrue(KeyHandler.isValidKey(65));
        assertTrue(KeyHandler.isValidKey(90));
        assertFalse(KeyHandler.isValidKey(91));
    }
}

