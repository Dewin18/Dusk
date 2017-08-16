package testing;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import Handlers.ChoiceHandler;

public class ChoiceHandlerTest
{
    private int defaultChoice;
    private int selection_quantity;
    
    public ChoiceHandlerTest()
    {
        super();
        init();
    }
    
    @Before
    public void init()
    {
        defaultChoice = 0;
        selection_quantity = 4;
    }

    @Test
    // tests the choice cycle. [...0, 3, 2, 1, 0...]
    public void selectNextUpTest()
    {
        assertTrue(ChoiceHandler.getChoice() == defaultChoice);
        
        ChoiceHandler.selectNextUp(selection_quantity);
        assertTrue(ChoiceHandler.getChoice() == 3);
        
        ChoiceHandler.selectNextUp(selection_quantity);
        assertTrue(ChoiceHandler.getChoice() == 2);
        
        ChoiceHandler.selectNextUp(selection_quantity);
        assertTrue(ChoiceHandler.getChoice() == 1);
        
        ChoiceHandler.selectNextUp(selection_quantity);
        assertTrue(ChoiceHandler.getChoice() == defaultChoice);
    }
    
    @Test
    // tests the choice cycle. [...0, 1, 2, 3, 0...]
    public void selectNextDownTest()
    {
        assertTrue(ChoiceHandler.getChoice() == defaultChoice);
        
        ChoiceHandler.selectNextDown(selection_quantity);
        assertTrue(ChoiceHandler.getChoice() == 1);
        
        ChoiceHandler.selectNextDown(selection_quantity);
        assertTrue(ChoiceHandler.getChoice() == 2);
        
        ChoiceHandler.selectNextDown(selection_quantity);
        assertTrue(ChoiceHandler.getChoice() == 3);
        
        ChoiceHandler.selectNextDown(selection_quantity);
        assertTrue(ChoiceHandler.getChoice() == defaultChoice);
    }
}
