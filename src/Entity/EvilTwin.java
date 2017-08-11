package Entity;

import Main.Time;
import TileMap.TileMap;
import TileMap.Vector2;

import static Entity.AnimationState.JUMPING;
import static Entity.AnimationState.WALKING;

public class EvilTwin extends Enemy
{
    private final int[] NUMFRAMES = {1, 6, 3};
    private final int[] FRAMEWIDTHS = {128, 128, 128};
    private final int[] FRAMEHEIGHTS = {128, 128, 128};
    private final int[] SPRITEDELAYS = {-1, 8, 6};
    //protected int currentInvulnerableTime = invulnerableTime = 9;

    public EvilTwin(TileMap tm)
    {
        super(tm);
        health = 30;
        damage = 1;
        velocity = new Vector2(0, 0);
        isFacingRight = false;
    }

    public void initEnemy(Vector2 position, String spriteName)
    {
        this.position = position;
        loadSprites("Enemies/" + spriteName, NUMFRAMES, FRAMEWIDTHS, FRAMEHEIGHTS);

        collisionBox.setCenter(position);
        collisionBox.setHalfSize(new Vector2(tileSize / 3, tileSize / 3 - 18));
        collisionOffset = new Vector2(tileSize / 2 - 1, collisionBox.halfSize.y + 38);

        width = FRAMEWIDTHS[0];
        height = FRAMEHEIGHTS[0];
        jumpSpeed = -6.5;
        walkSpeed = 2;
        minJumpSpeed = -1;
        maxFallSpeed = 12;
        gravity = 0.3;
        invulnerableTime = 9;

        animation.setFrames(sprites.get(0));
        animation.setDelay(SPRITEDELAYS[0]);
    }

    @Override
    public void update()
    {
        moveAround();
        super.update();
    }

    private void moveAround()
    {
        switch (currentState)
        {
            case IDLE:
                if (!isOnGround)
                {
                    setAnimation(JUMPING);
                    break;
                }
                setAnimation(WALKING);

                if (isFacingRight) setVelocity(walkSpeed, 0);
                else setVelocity(-walkSpeed, 0);
                break;

            case WALKING:
                if (!isOnGround)
                {
                    setAnimation(JUMPING);
                    break;
                }
                if (isFacingRight) setVelocity(walkSpeed, 0);
                else setVelocity(-walkSpeed, 0);
                if (isPushingLeftWall)
                {
                    isFacingRight = true;
                    isPushingLeftWall = false;
                } else if (isPushingRightWall)
                {
                    isFacingRight = false;
                    isPushingRightWall = false;
                }
                break;

            case JUMPING:
                velocity.y += gravity * Time.deltaTime;
                velocity.y = Math.min(velocity.y, maxFallSpeed);
                velocity.x = velocity.x * 0.9;
                if (isOnGround)
                {
                    setAnimation(WALKING);
                    velocity.y = 0;
                }
                break;

            case FLINCHING:
                break;
        }
    }

    @Override
    void setAnimation(AnimationState state)
    {
        currentState = state;
        int statenr = 0;
        switch (currentState)
        {
            case IDLE:
                statenr = 0;
                break;
            case WALKING:
                statenr = 1;
                break;
            case JUMPING:
                statenr = 0;
                break;
            case FLINCHING:
                statenr = 2;
                break;
        }
        animation.setFrames(sprites.get(statenr));
        animation.setDelay(SPRITEDELAYS[statenr]);
    }

    @Override
    protected void die() {
        //TODO trigger death animation
    }
}
