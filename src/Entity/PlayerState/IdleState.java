package Entity.PlayerState;

import Entity.AnimationState;
import Entity.Player;
import Handlers.KeyHandler;
import Handlers.Keys;

public class IdleState extends PlayerState
{
    public IdleState(Player player)
    {
        super(player);
    }

    @Override
    public void enter()
    {
        player.setVelocity(0, 0);
        player.setAnimation(AnimationState.IDLE);
    }

    @Override
    public PlayerState update()
    {
        // In air
        if (!player.isOnGround())
        {
            return new JumpingState(player, false);
        }
        // Platform drop
        if (KeyHandler.isPressed(Keys.DOWN) && KeyHandler.isPressed(Keys.JUMP) && player.isOnPlatform())
        {
            return new JumpingState(player, false);
        }
        // Jump
        if (KeyHandler.hasJustBeenPressed(Keys.JUMP))
        {
            return new JumpingState(player, true);
        }
        // Walking
        if (KeyHandler.isPressed(Keys.RIGHT) != KeyHandler.isPressed(Keys.LEFT))
        {
            return new WalkingState(player);
        }
        // Nothing
        player.setVelocity(0, 0);
        return null;
    }

    @Override
    public void exit()
    {

    }

    @Override
    public void resetAnimation()
    {
        player.setAnimation(AnimationState.IDLE);
    }
}
