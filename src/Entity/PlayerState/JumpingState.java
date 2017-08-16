package Entity.PlayerState;

import Audio.JukeBox;
import Entity.AnimationState;
import Entity.Player;
import Handlers.KeyHandler;
import Handlers.Keys;
import Main.Time;

/** PTP 2017
 * PlayerState implementation that handles the Jumping State of the Player.
 *
 * @author Ali Popa
 * @version 11.08.
 * @since 07.08.
 */
public class JumpingState extends PlayerState
{
    private boolean hasJustJumped;
    private boolean isRising;
    private boolean oldIsRising;
    private double gravity;
    private double maxFallingSpeed;
    private double minJumpingSpeed;
    private int fallingTime = 0;
    private int fallingLinesTriggerTime = 50;
    private boolean waitForNextState;

    JumpingState(Player player, boolean hasJustJumped)
    {
        super(player);
        this.hasJustJumped = hasJustJumped;
        this.gravity = player.getGravity();
        this.maxFallingSpeed = player.getMaxFallSpeed();
        this.minJumpingSpeed = player.getMinJumpSpeed();
    }

    @Override
    public void enter()
    {
        if (hasJustJumped)
        {
            JukeBox.play("jump");
            player.jump();
            player.setAnimation(AnimationState.JUMPING);
            isRising = oldIsRising = true;
        }
        else
        {
            player.fall();
            player.setAnimation(AnimationState.FALLING);
            isRising = oldIsRising = false;
        }
    }

    @Override
    public PlayerState update()
    {
        // Ground
        if (player.isOnGround())
        {
            if (KeyHandler.isPressed(Keys.LEFT) != KeyHandler.isPressed(Keys.RIGHT))
            {
                return new WalkingState(player);
            }
            else
            {
                return new IdleState(player);
            }
        }
        updateRiseAndFall();
        updateYVelocity();
        checkForEarlyReleaseOfJump();
        updateFallingTime();
        handleWalls();
        if (KeyHandler.isPressed(Keys.RIGHT) == KeyHandler.isPressed(Keys.LEFT))
        {
            player.getVelocity().x = 0;
        }
        return null;
    }

    @Override
    public void exit()
    {
        JukeBox.play("landing");
        JukeBox.play("grassstep4");
        player.setVelocityY(0);
    }

    @Override
    public void resetAnimation()
    {
        if (isRising)
        {
            player.setAnimation(AnimationState.JUMPING);
        }
        else
        {
            player.setAnimation(AnimationState.FALLING);
        }
    }

    private void updateRiseAndFall()
    {
        oldIsRising = isRising;
        isRising = player.getVelocity().y < 0;
        if (oldIsRising != isRising)
        {
            if (isRising)
            {
                player.setAnimation(AnimationState.JUMPING);
                fallingTime = 0;
            }
            else
            {
                player.setAnimation(AnimationState.FALLING);
            }
        }
    }

    private void updateYVelocity()
    {
        player.setVelocityY(Math.min(player.getVelocity().y + gravity * Time.deltaTime, maxFallingSpeed));
    }

    private void checkForEarlyReleaseOfJump()
    {
        if (!KeyHandler.isPressed(Keys.JUMP) && player.getVelocity().y < 0)
        {
            player.setVelocityY(Math.min(-player.getVelocity().y, -minJumpingSpeed));
        }
    }

    private void updateFallingTime()
    {
        if (!isRising)
        {
            fallingTime++;
            if (fallingTime > fallingLinesTriggerTime && !waitForNextState)
            {
                player.setAnimation(AnimationState.FALLING_LINES);
                waitForNextState = true;
            }
        }
    }
}
