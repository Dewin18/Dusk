package GameState;

/** PTP 2017
 * A GameState is used as a "scene" in the game, like the separate levels or the options menu.
 *
 * @author Ali Popa
 * @version 01.08.
 * @since 31.05.
 */
public abstract class GameState
{
    protected GameStateManager gsm;

    public abstract void init();

    public abstract void update();

    public abstract void draw(java.awt.Graphics2D g);

    public abstract void handleInput();
}
