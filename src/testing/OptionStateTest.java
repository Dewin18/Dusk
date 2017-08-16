package testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import GameState.GameStateManager;
import GameState.OptionState;

public class OptionStateTest
{
    private GameStateManager gsm;
    private OptionState optionState;
    
    public OptionStateTest()
    {
        super();
        init();
    }

    @Before
    public void init()
    {
        gsm = new GameStateManager(); 
        gsm.setState(GameStateManager.OPTIONSTATE);
        
        optionState = (OptionState) gsm.getState(GameStateManager.OPTIONSTATE);
    }
    
    @Test
    public void defaultKeyBindingsTest()
    {
        String[] selection = optionState.getSelection();
            
        assertEquals("ON", selection[0]);
        assertEquals("EASY", selection[1]);
        assertEquals("↑", selection[2]);
        assertEquals("↓", selection[3]);
        assertEquals("←", selection[4]);
        assertEquals("→", selection[5]);
        assertEquals("SPACE", selection[6]);
        assertEquals("A", selection[7]);
    }
    
    @Test
    public void duplicateAlgorithmCheckTest()
    {
        //create dummy array
        String[] selection = {"A", "B", "C", "D", "E"};
        
        assertFalse(optionState.duplicateCheck(selection));
        
        selection[1] = "A";
        
        assertTrue(optionState.duplicateCheck(selection));
        
        selection[0] = "B";
        
        assertFalse(optionState.duplicateCheck(selection));
        
        //duplicateCheck Algorithm allows only "NOT ASSIGNED" duplicates  
        selection[0] = "NOT ASSIGNED";
        selection[1] = "NOT ASSIGNED";
        selection[2] = "NOT ASSIGNED";
        selection[3] = "NOT ASSIGNED";
        selection[4] = "NOT ASSIGNED";
        
        assertFalse(optionState.duplicateCheck(selection));
    }
}
