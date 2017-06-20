package Entity;

import static Entity.CharacterState.IDLE;
import static Entity.CharacterState.JUMPING;
import static Entity.CharacterState.WALKING;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Main.Time;
import TileMap.TileMap;
import TileMap.Vector2;

public class EvilTwin extends Enemy
{
    private double minFallSpeed;

    private final int[] NUMFRAMES = {1, 6};
    private final int[] FRAMEWIDTHS = {128, 128};
    private final int[] FRAMEHEIGHTS = {128, 128};
    private final int[] SPRITEDELAYS = {-1, 8};
    
    public EvilTwin(TileMap tm)
    {
        super(tm);
        health = 1;
        damage = 1;
        minFallSpeed = 0.4;
        velocity = new Vector2(0, 0);
        isFacingRight = false;
    }
    
    public void initEnemy(Vector2 position, String spriteName) {
        this.position = position;
        loadSprites("Enemies/" + spriteName, NUMFRAMES, FRAMEWIDTHS, FRAMEHEIGHTS);

        collisionBox.setCenter(position);
        collisionBox.setHalfSize(new Vector2(tileSize/3 , tileSize/3 - 18));
        collisionOffset = new Vector2(tileSize / 2 - 1, collisionBox.halfSize.y + 38);

        width = FRAMEWIDTHS[0];
        height = FRAMEHEIGHTS[0];
        jumpSpeed = -6.5;
        walkSpeed = 2;
        minJumpingSpeed = -1;
        maxFallingSpeed = 4;
        gravity = 0.3;
        minFallSpeed = 2;
        

        animation.setFrames(sprites.get(0));
        animation.setDelay(SPRITEDELAYS[0]);
    }

    @Override
    public void update() {
        moveAround();
        animation.update();
        updatePhysics();
    }

    private void moveAround() {
        switch (currentState) {
            case IDLE:
                if (!isOnGround) {
                    setAnimation(JUMPING);
                    break;
                }
                setAnimation(WALKING);

                if(isFacingRight) setVelocity(walkSpeed, 0);
                else setVelocity(-walkSpeed, 0);
                break;

            case WALKING:
                if (!isOnGround) {
                    setAnimation(JUMPING);
                    break;
                }
                if(isFacingRight) setVelocity(walkSpeed, 0);
                else setVelocity(-walkSpeed, 0);
                if (isPushingLeftWall) {
                    isFacingRight = true;
                    position.x += 5;
                    isPushingLeftWall = false;
                } else if (isPushingRightWall) {
                    isFacingRight = false;
                    position.x -= 5;
                    isPushingRightWall = false;
                }
                break;

            case JUMPING:
                velocity.y += gravity * Time.deltaTime;
                velocity.y = Math.min(velocity.y, maxFallingSpeed);
                if (isOnGround) {
                    setAnimation(WALKING);
                    velocity.y = 0;
                }
                break;
        }
    }

    @Override
    void setAnimation(CharacterState state) {
        currentState = state;
        int statenr = 0;
        switch (currentState) {
            case IDLE:
                statenr = 0;
                break;
            case WALKING:
                statenr = 1;
                break;
            case JUMPING:
                statenr = 0;
                break;
        }
        animation.setFrames(sprites.get(statenr));
        animation.setDelay(SPRITEDELAYS[statenr]);
    }
}
