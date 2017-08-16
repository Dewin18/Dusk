package Entity.PlayerState;

import Audio.JukeBox;
import Entity.AnimationState;
import Entity.Player;
import Handlers.KeyHandler;
import Handlers.Keys;

/** PTP 2017
 * PlayerState implementation that handles the Walking State of the Player.
 *
 * @author Ali Popa
 * @version 14.08.
 * @since 07.08.
 */
public class WalkingState extends PlayerState
{
    private int stepSoundMaxTime = 20;
    private int currentStepSoundTime = stepSoundMaxTime;

    WalkingState(Player player)
    {
        super(player);
    }

    @Override
    public void enter()
    {
        player.setAnimation(AnimationState.WALKING);
    }

    @Override
    public PlayerState update()
    {
        // Check if in air
        if (!player.isOnGround())
        {
            return new JumpingState(player, false);
        }

        // Check if dropping from platform
        if (KeyHandler.isPressed(Keys.DOWN) && KeyHandler.isPressed(Keys.JUMP) && player.isOnPlatform())
        {
            return new JumpingState(player, false);
        }

        // Check if jumped
        if (KeyHandler.hasJustBeenPressed(Keys.JUMP))
        {
            return new JumpingState(player, true);
        }

        // Check if idle
        if (KeyHandler.isPressed(Keys.RIGHT) == KeyHandler.isPressed(Keys.LEFT))
        {
            return new IdleState(player);
        }

        playWalkingSound();
        handleWalls();

        return null;
    }

    @Override
    public void exit()
    {
        resetStepTime();
    }

    @Override
    public void resetAnimation()
    {
        player.setAnimation(AnimationState.WALKING);
    }

    private void resetStepTime()
    {
        currentStepSoundTime = stepSoundMaxTime;
    }

    private void playWalkingSound()
    {
        if (currentStepSoundTime < stepSoundMaxTime)
        {
            currentStepSoundTime++;
            return;
        }
        String s = "grassstep" + (int)(Math.random()*4+1);
        JukeBox.play(s);
        currentStepSoundTime = 0;
    }
}
