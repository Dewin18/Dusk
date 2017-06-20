package GameState;

public class GameStateManager
{

    private GameState[] gameStates;
    private int currentState;

    public static final int MENUSTATE = 0;
    public static final int LEVEL1STATE = 1;

    public GameStateManager()
    {
        gameStates = new GameState[12];
        currentState = MENUSTATE;
        loadState(currentState);
    }

    public void setState(int state)
    {
        unloadState(currentState);
        currentState = state;
        loadState(currentState);
    }

    private void loadState(int state)
    {
        if (state == MENUSTATE) gameStates[state] = new MenuState(this);
        else if (state == LEVEL1STATE) gameStates[state] = new Level1State(this);
    }

    private void unloadState(int state)
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

}









