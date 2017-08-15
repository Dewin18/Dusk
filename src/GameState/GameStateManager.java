package GameState;

import Audio.JukeBox;

public class GameStateManager
{
    private GameState[] gameStates;
    private int currentState;

    public static final int MENUSTATE = 0;
    public static final int LEVEL1STATE = 1;
    public static final int OPTIONSTATE = 2;
    public static final int TESTSTATE = 3;

    public GameStateManager()
    {
        JukeBox.init();
        gameStates = new GameState[12];
        currentState = MENUSTATE;
        loadState(currentState);
    }

    public void setState(int state)
    {
        //unloadState(currentState);
        currentState = state;
        loadState(currentState);
    }

    public void loadState(int state)
    {
        if (state == MENUSTATE) gameStates[state] = new MenuState(this);
        else if (state == LEVEL1STATE) gameStates[state] = new Level1State(this);
        else if (state == OPTIONSTATE) gameStates[state] = new OptionState(this);
        
    }

    public void unloadState(int state)
    {
        gameStates[state] = null;
    }

    public void update()
    {
        if (gameStates[currentState] != null) gameStates[currentState].update();
    }

    public void draw(java.awt.Graphics2D g)
    {
        if (gameStates[currentState] != null) gameStates[currentState].draw(g);
    }
    
    public GameState getState(int stateNumber)
    {
        return gameStates[stateNumber];
    }

    public int getCurrentStateNumber()
    {
        return currentState;
    }
}