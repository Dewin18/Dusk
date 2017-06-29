package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import Handlers.KeyHandler;
import Handlers.Keys;
import Main.GamePanel;

public class OptionState extends GameState
{
    private static boolean CHECK_INPUTS = false;
    
    private final int NAVIGATION_VGAP_INTERVAL = 380; 
    
    //GAP starts at center (GamePanel / 2)
    private final int TITLE_VGAP = 60;
    private final int TITLE_HGAP = 250;
    
    //default value for selectable settings
    private int currentChoice = 0;
    private int currentControl = 0;
    private int currentState = 0;

    private Font font;
    private Font titleFont;
    private Color titleColor;

    private boolean[] selectionStates = new boolean[10];
    private boolean isNoKeySelected = true;
    private boolean duplicateWarning;

    private ArrayList<String[]> stateList;
    
    private String[] settingTitles = {"SOUND", "DIFFICULTY"};
    private String[] subSoundSettings = {"ON", "OFF"};
    private String[] subDifficultySettings = {"EASY", "MEDIUM", "HARD"};
    private String[] navigationSettings = {"BACK", "RESET", "CLEAR KEYS","SAVE"};
    private String[] controlTitles = {"UP", "DOWN", "LEFT", "RIGHT", "JUMP", "HIT"};
  
 private String[] selection;

    public OptionState(GameStateManager gsm)
    {
        this.gsm = gsm;
        init();
        System.out.println(KeyHandler.getNewKeys());
    }

    public void init()
    {
        initStateList();
        initFonts();
        initDefaultSettings();
        
        selectionStates[0] = true;
    }

    private void initFonts()
    {
        titleFont = new Font("Arial", Font.PLAIN, 25);
        titleColor = new Color(0, 153, 255);
        font = new Font("Arial", Font.PLAIN, 15);
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
        selection[7] = "D";
    }

    public void draw(Graphics2D g)
    {
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        g.setColor(titleColor);
        g.setFont(titleFont);

        g.drawString("SETTINGS", GamePanel.WIDTH / 2 - TITLE_VGAP,
                GamePanel.HEIGHT / 2 - TITLE_HGAP);

        g.setColor(Color.LIGHT_GRAY);
        g.setFont(font);

        //draw NOT selectable setting titles
        drawOptionTitles(g);

        //draw changeable keys for controls
        drawControls(g);
        
       //draw selectable settings
        drawSoundSelection(g);
        drawDifficultySelection(g);
        drawNavigation(g);
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

    //draw back, reset, clear keys, save
    private void drawNavigation(Graphics2D g)
    {
        for (int i = 0; i < navigationSettings.length; i++)
        {
            if(selectionStates[8])
            {
                if (i == currentChoice) { g.setColor(Color.GREEN); } 
                else { g.setColor(Color.LIGHT_GRAY); }
            }
            else
            {
                g.setColor(Color.LIGHT_GRAY);
            }
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
        String drawString;
        
        if (controlState)
        {
            if(duplicateWarning)
            {
                g.setColor(Color.RED);
                drawString = "KEY ALREADY USED!";
            }
            else
            {
                g.setColor(Color.GREEN);
                drawString = selection[position];
            }
            g.drawString(drawString, GamePanel.WIDTH / 2 + 150,
                    GamePanel.HEIGHT / 2 - yOffset);
        }
        else
        {
            g.setColor(Color.LIGHT_GRAY);
            g.drawString(selection[position], GamePanel.WIDTH / 2 + 150,
                    GamePanel.HEIGHT / 2 - yOffset);
        }
    }

    //draw easy, medium, hard
    private void drawDifficultySelection(Graphics2D g)
    {
        for (int i = 0; i < subDifficultySettings.length; i++)
        {
            if (selectionStates[1])
            {
                g.setColor(Color.GREEN);
                difficultySelector();
            }
            else
            {
                g.setColor(Color.LIGHT_GRAY);
            }
            g.drawString(selection[1], GamePanel.WIDTH / 2 + 150,
                    GamePanel.HEIGHT / 2 - 135);
        }
    }

    private void difficultySelector()
    {
        switch (currentChoice)
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

    private void drawSoundSelection(Graphics2D g)
    {
        for (int i = 0; i < subSoundSettings.length; i++)
        {
            if (selectionStates[0])
            {
                g.setColor(Color.GREEN);
                soundSelector();
            }
            else
            {
                g.setColor(Color.LIGHT_GRAY);
            }
            g.drawString(selection[0], GamePanel.WIDTH / 2 + 150,
                    GamePanel.HEIGHT / 2 - 180);
        }
    }

    
    private void soundSelector()
    {
        switch(currentChoice)
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
        
        if(selectionStates[9]) selectNavigation();
        else currentChoice = 0;
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
    }

    //helper method to check for duplicates and enable duplicate warning string
    private void checkForDuplicates()
    {
        if(containsDuplicates())
        {
            duplicateWarning = true;
        }
        else
        {
            duplicateWarning = false;
            currentChoice = 0;
            selectDown();
        }
    }

    public void handleInput(String[] selection)
    {
        int selectionLength = selection.length;
        
        if (KeyHandler.hasJustBeenPressed(Keys.ENTER) 
                || KeyHandler.hasJustBeenPressed(Keys.DOWN))
        {
            selectDown();
        }
        
        if (KeyHandler.hasJustBeenPressed(Keys.LEFT))
        {
            currentChoice--;
            if (currentChoice == -1)
            {
                currentChoice = selectionLength - 1;
            }
        }
        if (KeyHandler.hasJustBeenPressed(Keys.RIGHT))
        {
            currentChoice++;
            if (currentChoice == selectionLength)
            {
                currentChoice = 0;
            }
        }
    }
    
    private void selectNavigation()
    {
       if(currentChoice == 0)
       {
           initDefaultSettings();
           gsm.setState(GameStateManager.MENUSTATE);
           if(CHECK_INPUTS) checkInputs();
       }   
       else if(currentChoice == 1)
       {
           initDefaultSettings();
           gotoFirstState();
       }
       else if(currentChoice == 2)
       {
           for(int i=2; i < 8; i++)
           {
               selection[i] = "NOT ASSIGNED";
           }
           
           gotoFirstState();
       }
       else if(currentChoice == 3)
       {
           //KeyHandler.setNewKeys(selection); TODO
           gsm.setState(GameStateManager.MENUSTATE);
           if(CHECK_INPUTS) checkInputs();
       }
    }
    
    private void gotoFirstState()
    {
        currentChoice = 0;
        currentControl = 0;
        currentState = 0;
        
        selectionStates[9] = false;
        selectionStates[0] = true;
    }

    /**
     * Check if all user key inputs for the movement controls are different
     * 
     * @return true if contains duplicates false otherwise
     */
    public boolean containsDuplicates()
    {
        boolean duplicates = false;
        String noKey = "NOT ASSIGNED";
        
        for(int i = 0; i < selection.length; i++)
        {
            for(int j = i; j < selection.length; j++)
            {
                if(!selection[i].equals(noKey) && !selection[j].equals(noKey) 
                  && i != j && selection[i].equals(selection[j]))
                {
                    duplicates = true;
                }
            }
        }
        return duplicates;
    }
    
    //Helper method to print all selection[] array elements at standard output
    private void checkInputs()
    {
        for (int i = 0; i < selection.length; i++)
        {
            System.out.print(selection[i] + " ");
        }
    }
    
    public void handleInput()
    {
    }
}
