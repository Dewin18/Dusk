package Entity.PlayerState;

import Entity.Player;
import Handlers.KeyHandler;
import Handlers.Keys;
import Helpers.Vector2;

public abstract class PlayerState
{
    Player player;

    PlayerState(Player player)
    {
        this.player = player;
    }

    public abstract void enter();
    public abstract PlayerState update();
    public abstract void exit();
    public abstract void resetAnimation();

    void handleWalls()
    {
        if (KeyHandler.isPressed(Keys.RIGHT))
        {
            if (player.isPushingRightWall()) player.setVelocityX(0);
            else player.walkInDirection(Vector2.RIGHT);
            if (!player.isAttacking()) player.setFacingRight(true);
        }
        else if (KeyHandler.isPressed(Keys.LEFT))
        {
            if (player.isPushingLeftWall()) player.setVelocityX(0);
            else player.walkInDirection(Vector2.LEFT);
            if (!player.isAttacking()) player.setFacingRight(false);
        }
    }
}
