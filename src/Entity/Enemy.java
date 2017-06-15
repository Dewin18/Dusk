package Entity;

import Main.Time;
import TileMap.TileMap;
import TileMap.Vector2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import static Entity.CharacterState.*;

public class Enemy extends MovingObject{
    int health;
    int damage;
    CharacterState currentState = IDLE;
    double minFallSpeed = 0.4;

    private final int[] NUMFRAMES = {
            1, 6
    };
    private final int[] FRAMEWIDTHS = {
            128, 128
    };
    private final int[] FRAMEHEIGHTS = {
            128, 128
    };
    private final int[] SPRITEDELAYS = {
            -1, 8
    };


    public Enemy(TileMap tm) {
        super(tm);

        health = 1;
        damage = 1;

        velocity = new Vector2(0, 0);

        isFacingRight = false;
    }

    public void initEnemy(Vector2 position, String spriteName) {
        this.position = position;
        // load sprites
        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream(
                    "/Sprites/Enemies/" + spriteName));
            sprite = spritesheet.getSubimage(0, 0, 30, 30);
            int count = 0;
            sprites = new ArrayList<>();
            for(int i = 0; i < NUMFRAMES.length; i++) {
                BufferedImage[] bi = new BufferedImage[NUMFRAMES[i]];
                for(int j = 0; j < NUMFRAMES[i]; j++) {
                    bi[j] = spritesheet.getSubimage(
                            j * FRAMEWIDTHS[i],
                            count,
                            FRAMEWIDTHS[i],
                            FRAMEHEIGHTS[i]
                    );
                }
                sprites.add(bi);
                count += FRAMEHEIGHTS[i];
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        collisionBox = new CollisionBox(position, new Vector2(tileSize/3 , tileSize/3 - 18));
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
                    System.out.println("asdashdasjdh");
                    isPushingLeftWall = false;
                } else if (isPushingRightWall) {
                    isFacingRight = false;
                    position.x -= 5;
                    System.out.println("SDS");
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

    private void setAnimation(CharacterState state) {
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
