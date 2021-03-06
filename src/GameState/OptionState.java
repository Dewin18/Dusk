package GameState;

import java.awt.*;
import java.util.ArrayList;

import Audio.JukeBox;
import Handlers.AnimationHandler;
import Handlers.ChoiceHandler;
import Handlers.FontHandler;
import Handlers.KeyHandler;
import Handlers.Keys;
import Main.GamePanel;

/** PTP 2017
 * The state describing the options menu of the game.
 *
 * @author Dewin Bagci
 * @version 16.08.
 * @since 29.06.
 */
public class OptionState extends GameState
{
    private final int NAVIGATION_VGAP_INTERVAL = 560; 
    
    //GAP starts at center (GamePanel / 2)
    private final int TITLE_VGAP = 20;
    private final int TITLE_HGAP = 250;
    
    //constant colors for selection mode
    private final Color SELECT_COLOR = Color.GREEN;
    private final Color WARNING_COLOR = Color.RED;
    private final Color DEFAULT_COLOR = Color.LIGHT_GRAY;
    
    //current key state (up, down, left, right) selected
    private int currentControl;
    
    //current state number in boolean Array
    private int currentState;

    private Color titleColor = new Color(0, 153, 255);;
    
    private boolean[] selectionStates = new boolean[10];
    private boolean isNoKeySelected = true;
    private boolean duplicateWarning;

    private ArrayList<String[]> stateList;
    
    private String[] settingTitles = {"SOUND", "DIFFICULTY"};
    private String[] subSoundSettings = {"ON", "OFF"};
    private String[] subDifficultySettings = {"EASY", "MEDIUM", "HARD"};
    private String[] navigationSettings = {"SAVE AND BACK", "RESET", "CLEAR"};
    private String[] controlTitles = {"UP", "DOWN", "LEFT", "RIGHT", "JUMP", "HIT"};
  
    private String[] selection;

    //store all key codes of all keys pressed
    private int[] keyCodes;
    private int keyCodeIndex;
    
    public OptionState(GameStateManager gsm)
    {
        this.gsm = gsm;
        init();
    }

    public void init()
    {
        initChoiceAndStates();
        initStateList();
        initDefaultSettings();
        
        //indicates that the first state (SOUND state) is selected
        selectionStates[0] = true;
        
        if(KeyHandler.keysChanged())
        {
            keyCodes = KeyHandler.getKeyCodes();
            selection = KeyHandler.getNewKeys();
            ChoiceHandler.setChoice(KeyHandler.getStoredChoice(0));
        }
    }

    private void initChoiceAndStates()
    {
        ChoiceHandler.setChoice(0);
        currentControl = 0;
        currentState = 0;
    }

    private void initStateList()
    {
        stateList = new ArrayList<>();
        stateList.add(subSoundSettings);
        stateList.add(subDifficultySettings);
        stateList.add(navigationSettings);
    }   

    public void initDefaultSettings()
    {
        initSelections();
        initKeyCodes();
    }

    private void initKeyCodes()
    {
       keyCodes = new int[6];
        
       keyCodes[0] = 38;
       keyCodes[1] = 40;
       keyCodes[2] = 37;
       keyCodes[3] = 39;
       keyCodes[4] = 32;
       keyCodes[5] = 65;
       
       keyCodeIndex = 0;
    }

    private void initSelections()
    {
        selection = new String[8];
        
        //STATES
        selection[0] = "ON";
        selection[1] = "EASY";
        
        // KEYS
        selection[2] = "↑";
        selection[3] = "↓";
        selection[4] = "←";
        selection[5] = "→";
        selection[6] = "SPACE";
        selection[7] = "A";
    }

    public void draw(Graphics2D g)
    {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        drawTitle(g);
        
        g.setColor(DEFAULT_COLOR);
        g.setFont(FontHandler.getOptionStateSelectionFont());

        //draw NOT selectable titles
        drawOptionTitles(g);

        //draw changeable keys for controls
        drawControls(g);
        
       //draw selectable settings
        drawSoundSelection(g);
        drawDifficultySelection(g);
        drawNavigation(g);
    }

    private void drawTitle(Graphics2D g)
    {
        g.setColor(titleColor);
        g.setFont(FontHandler.getOptionStateTitleFont());

        g.drawString("SETTINGS", GamePanel.WIDTH / 2 - TITLE_VGAP,
                GamePanel.HEIGHT / 2 - TITLE_HGAP);
    }

    private void drawControls(Graphics2D g)
    {
        drawControl(g, selectionStates[2], 2 , 50);
        drawControl(g, selectionStates[3], 3, 5);
        drawControl(g, selectionStates[4], 4, -40);
        drawControl(g, selectionStates[5], 5, -85);
        drawControl(g, selectionStates[6], 6, -130);
        drawControl(g, selectionStates[7], 7, -175);
    }

    private void drawOptionTitles(Graphics2D g)
    {
        //draw sound and difficulty titles
        for (int i = 0; i < settingTitles.length; i++)
        {
            g.drawString(settingTitles[i], GamePanel.WIDTH / 2 - 150,
                    GamePanel.HEIGHT / 2 - 180 + i * 45);
        }

        //draw up, down, left, right, jump, hit titles
        for (int i = 0; i < controlTitles.length; i++)
        {
            g.drawString(controlTitles[i], GamePanel.WIDTH / 2 - 150,
                    GamePanel.HEIGHT / 2 - 50 + i * 45);
        }
    }

    //draw back and save, reset, clear
    private void drawNavigation(Graphics2D g)
    {
        for (int i = 0; i < navigationSettings.length; i++)
        {
            if(selectionStates[8])
            {
                if (i == ChoiceHandler.getChoice())  g.setColor(SELECT_COLOR);  
                else  g.setColor(DEFAULT_COLOR); 
            }
            else g.setColor(DEFAULT_COLOR);
            
            g.drawString(navigationSettings[i], 
                    GamePanel.WIDTH / 2 - 600 + i * NAVIGATION_VGAP_INTERVAL,
                    GamePanel.HEIGHT / 2 + 270);
        }
    }
 
    /**
     * Draw all control keys
     * 
     * @param g graphics
     * @param controlState current control mode (UP, DOWN, LEFT, RIGHT, JUMP, HIT)
     * @param position the position in selection array
     * @param yOffset shift drawing at Y-axis
     */
    public void drawControl(Graphics2D g, Boolean controlState, int position, int yOffset)
    {
        String drawString = selection[position];
        
        if (controlState)
        {
            if(duplicateWarning)
            {
                drawString = "KEY ALREADY USED!";
                g.setColor(WARNING_COLOR);
            }
            else 
            {
                g.setColor(SELECT_COLOR);
                AnimationHandler.startBlinkAnimation(g);
            } 
        } 
        else 
        {
            g.setColor(DEFAULT_COLOR);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        }

        if(drawString.equals("←") || drawString.equals("↑") || drawString.equals("→") || drawString.equals("↓"))
            g.setFont(FontHandler.getOptionStateKeyBindingsFont());
        
        g.drawString(drawString, GamePanel.WIDTH / 2 + 150,
                GamePanel.HEIGHT / 2 - yOffset);
        g.setFont(FontHandler.getOptionStateSelectionFont());
    }

    //draw easy, medium, hard
    private void drawDifficultySelection(Graphics2D g)
    {
        for (int i = 0; i < subDifficultySettings.length; i++)
        {
            if (selectionStates[1])
            {
                g.setColor(SELECT_COLOR);
                difficultySelector();
            }
            else
            {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                g.setColor(DEFAULT_COLOR);
            }
            
            g.drawString(selection[1], GamePanel.WIDTH / 2 + 150,
                    GamePanel.HEIGHT / 2 - 135);
        }
    }

    private void difficultySelector()
    {
        switch (ChoiceHandler.getChoice())
        {
        case 0:
            selection[1] = "EASY";
            break;
        case 1:
            selection[1] = "MEDIUM";
            break;
        case 2:
            selection[1] = "HARD";
            break;
        }
    }

    //draw on, off
    private void drawSoundSelection(Graphics2D g)
    {
        for (int i = 0; i < subSoundSettings.length; i++)
        {
            if (selectionStates[0])
            {
                g.setColor(SELECT_COLOR);
                soundSelector();
            }
            else
            {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
                g.setColor(DEFAULT_COLOR);
            }
            g.drawString(selection[0], GamePanel.WIDTH / 2 + 150,
                    GamePanel.HEIGHT / 2 - 180);
        }
    }
    
    private void soundSelector()
    {
        switch(ChoiceHandler.getChoice())
        {
        case 0: 
            selection[0] = "ON";
            break;
        case 1: 
            selection[0] = "OFF";
            break;
        }
    }

    public void selectDown()
    {
        currentControl++;
        selectionStates[currentState] = false;
        currentState++;
        selectionStates[currentState] = true;
        isNoKeySelected = true;
        handleCurrentChoice();
    }
    
    private void handleCurrentChoice()
    {
        if(selectionStates[0]) ChoiceHandler.setChoice(ChoiceHandler.getDifficultyChoice(selection[0]));
        else if(selectionStates[1]) ChoiceHandler.setChoice(ChoiceHandler.getDifficultyChoice(selection[1]));
        else if(selectionStates[9]) selectNavigation();
    }

    /**
     * Update all selections
     */
    public void update()
    {
        int currentSelection = 0;
        
        for(int i=0; i < selectionStates.length; i++)
        {
            if(selectionStates[i]) currentSelection = i;
        }
        
        //Update state if the current selection is a state
        if(currentSelection == 0 || currentSelection == 1 || currentSelection == 8)
        {
            if(currentSelection == 8) currentSelection = 2;
            handleInput(stateList.get(currentSelection));
        }
        else if (isNoKeySelected) //Update state if the current selection is a key control state
        {
             handleNewKey(); 
        }
    }

    /**
     * Handles the input for NEW key
     */
    public void handleNewKey()
    {
        if ((KeyHandler.hasJustBeenPressed(Keys.ENTER)))
        {
            checkForDuplicates();
        }
        else if (KeyHandler.isValidKey(KeyHandler.getKeyPressed()))
        {
            setNewKey();
        }
    }

    private void setNewKey()
    {
        duplicateWarning = false;
        
        if (KeyHandler.isSpecialKey(KeyHandler.getKeyPressed()))
        {
            selection[currentControl] = KeyHandler.getSpecialKey(KeyHandler.getKeyPressed());
        }
        else
        {
            selection[currentControl] = Character
                .toString((char)KeyHandler.getKeyPressed());
        }
        
        keyCodes[keyCodeIndex] = KeyHandler.getKeyPressed();
    }

    //helper method to check for duplicates and enable duplicate warning string
    private void checkForDuplicates()
    {
        boolean keyAlreadyDefined = duplicateCheck(selection);
        
        if(keyAlreadyDefined)
        {
            duplicateWarning = true;
        }
        else
        {
            duplicateWarning = false;
            keyCodeIndex ++;
            ChoiceHandler.setChoice(0);
            selectDown();
        }
    }

    /**
     * Handles the keys pressed.
     * 
     * @param selection the selection array
     */
    public void handleInput(String[] selection)
    {
        int selectionLength = selection.length;

        if (KeyHandler.hasJustBeenPressed(Keys.ENTER)) JukeBox.play("menupick");

        if (KeyHandler.hasJustBeenPressed(Keys.ENTER) || KeyHandler.hasJustBeenPressed(Keys.DOWN))
        {
            if(currentState == 1) ChoiceHandler.setChoice(ChoiceHandler.getDifficultyChoice(selection[1]));
            selectDown();
        }
        else if (KeyHandler.hasJustBeenPressed(Keys.LEFT))
        {
            JukeBox.play("menuchoice");
            ChoiceHandler.selectNextUp(selectionLength);
        }
        else if (KeyHandler.hasJustBeenPressed(Keys.RIGHT))
        {
            JukeBox.play("menuchoice");
            ChoiceHandler.selectNextDown(selectionLength);
        }
    }

    private void selectNavigation()
    {
        keyCodeIndex = 0;
        
        switch(ChoiceHandler.getChoice())
        {
        case 0: 
            backAndSave();
            break;
        case 1: 
            resetSettings();
            break;
        case 2: 
            clearKeys();
            break;
        }
    }
    
    private void backAndSave()
    {
      //save new selected choices
        KeyHandler.storeChoice(ChoiceHandler.getDifficultyChoice(selection[0]), 0);
        KeyHandler.storeChoice(ChoiceHandler.getDifficultyChoice(selection[1]), 1);
        
        //save new key bindings
        KeyHandler.setKeysChanged(true);
        KeyHandler.setNewKeys(selection);
        KeyHandler.setKeyCodes(keyCodes);
        
        gsm.setState(GameStateManager.MENUSTATE);
    }

    private void resetSettings()
    {
        initDefaultSettings(); // reset all key Bindings
        initChoiceAndStates();
        gotoFirstState();
    }

    private void clearKeys()
    {
        selection[1] = "EASY";
        setKeysNotAssigned();
        initChoiceAndStates();
        gotoFirstState();
    }

    private void setKeysNotAssigned()
    {
        keyCodes[0] = 38; //UP
        keyCodes[1] = 40; //DOWN
        
        for(int i=2; i < selection.length; i++)
        {
            selection[i] = "NOT ASSIGNED";
        }
        
        for(int i=2; i < keyCodes.length; i++)
        {
           keyCodes[i] = 0;
        }
    }

    private void gotoFirstState()
    {
        selectionStates[9] = false;
        selectionStates[0] = true;
    }

    /**
     * Check if all user key inputs for the movement controls are different
     * 
     * @return true if contains duplicates false otherwise
     */
    public boolean duplicateCheck(String[] selection)
    {
        boolean containsDuplicates = false;
        String noKey = "NOT ASSIGNED";
        
        for(int i = 0; i < selection.length; i++)
        {
            for(int j = i; j < selection.length; j++)
            {
                if(!selection[i].equals(noKey) && !selection[j].equals(noKey) 
                  && i != j && selection[i].equals(selection[j]))
                {
                    containsDuplicates = true;
                }
            }
        }
        return containsDuplicates;
    }
    
    public String toString()
    {
        return "OptionState";
    }
    
    public void handleInput()
    {
    }
    
    public String[] getSelection()
    {
        return selection;
    }
}
