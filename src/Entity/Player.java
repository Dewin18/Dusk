package Entity;

import Handlers.KeyHandler;
import Handlers.Keys;
import Main.Time;
import TileMap.TileMap;
import TileMap.Vector2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static Entity.CharacterState.*;

public class Player extends MovingObject{

    private int health;
    private int exp;
    private int lives;
    private int dmg;

    private boolean isRising = false;
    private boolean isFalling = false;
    private double minFallSpeed;
    // for canceling repeat jumps by keeping the button pressed
    private boolean hasJumped;

    private boolean hasAttack = true;
    private boolean hasDoubleJump = false;
    private boolean hasDash = false;

    public CharacterState currentState = STANDING;

    private long time = 0;

    public Player(TileMap tm) {
        super(tm);

        // load sprites
        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream(
                    "/Sprites/Player/PlayerSprites.gif"));
            sprite = spritesheet.getSubimage(0, 0, 30, 30);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void initPlayer(Vector2 position) {
        setPosition(position);

        // set up speeds
        jumpSpeed = -6.5;
        walkSpeed = 2;
        minJumpingSpeed = -1;
        maxFallingSpeed = 4;
        gravity = 0.3;
        minFallSpeed = 2;

        // set up collision box
        collisionBox = new CollisionBox(position, new Vector2(tileSize/3 - 1, tileSize/2 - 1));
        collisionOffset = new Vector2(tileSize / 2 - 1, collisionBox.halfSize.y);
    }

    public void update() {
        switch (currentState) {
            case STANDING:
                setVelocity(0, 0);
                if (!isOnGround) {
                    currentState = JUMPING;
                    break;
                }
                if (hasJumped && !KeyHandler.isPressed(Keys.JUMP)) hasJumped = false;
                if (KeyHandler.isPressed(Keys.RIGHT) != KeyHandler.isPressed(Keys.LEFT)) {
                    currentState = WALKING;
                    break;
                } else if (KeyHandler.isPressed(Keys.DOWN) && KeyHandler.isPressed(Keys.JUMP)) {
                    if (isOnPlatform) {
                        position.y += 1;
                        framesPassedUntilDrop = 0;
                        isOnPlatform = false;
                        velocity.y = minFallSpeed;
                        hasJumped = true;
                    }
                } else if (KeyHandler.isPressed(Keys.JUMP) && !hasJumped) {
                        velocity.y = jumpSpeed;
                        currentState = JUMPING;
                        hasJumped = true;
                        break;
                }
                break;

            case WALKING:
                if (hasJumped && !KeyHandler.isPressed(Keys.JUMP)) hasJumped = false;
                if (KeyHandler.isPressed(Keys.RIGHT) == KeyHandler.isPressed(Keys.LEFT)) {
                    currentState = STANDING;
                    setVelocity(0, 0);
                    break;
                } else if (KeyHandler.isPressed(Keys.RIGHT)) {
                    if (isPushingRightWall) velocity.x = 0;
                    else velocity.x = walkSpeed;
                } else if (KeyHandler.isPressed(Keys.LEFT)) {
                    if (isPushingLeftWall) velocity.x = 0;
                    else velocity.x = -walkSpeed;
                }
                if (KeyHandler.isPressed(Keys.DOWN) && KeyHandler.isPressed(Keys.JUMP)) {
                    if (isOnPlatform) {
                        position.y += 1;
                        framesPassedUntilDrop = 0;
                        isOnPlatform = false;
                        velocity.y = minFallSpeed;
                        hasJumped = true;
                    }
                } else if (KeyHandler.isPressed(Keys.JUMP) && !hasJumped) {
                    velocity.y = jumpSpeed;
                    currentState = JUMPING;
                    hasJumped = true;
                    break;
                } else if (!isOnGround) {
                    currentState = JUMPING;
                    break;
                }
                break;

            case JUMPING:
                isRising = velocity.y < 0;
                velocity.y += gravity * Time.deltaTime;
                velocity.y = Math.min(velocity.y, maxFallingSpeed);
                if (isOnGround) {
                    if (KeyHandler.isPressed(Keys.LEFT) == KeyHandler.isPressed(Keys.RIGHT)) {
                        currentState = STANDING;
                        setVelocity(0, 0);
                    } else {
                        currentState = WALKING;
                        velocity.y = 0;
                    }
                    if (hasJumped && !KeyHandler.isPressed(Keys.JUMP)) hasJumped = false;
                }
                if (!KeyHandler.isPressed(Keys.JUMP) && velocity.y < 0) {
                    velocity.y = Math.max(velocity.y, minJumpingSpeed);
                }
                isFalling = velocity.y > 0;
                if(isRising && isFalling) velocity.y = minFallSpeed;
                if (KeyHandler.isPressed(Keys.RIGHT) == KeyHandler.isPressed(Keys.LEFT)) {
                    velocity.x = 0;
                    break;
                } else if (KeyHandler.isPressed(Keys.RIGHT)) {
                    if (isPushingRightWall) velocity.x = 0;
                    else velocity.x = walkSpeed;
                } else if (KeyHandler.isPressed(Keys.LEFT)) {
                    if (isPushingLeftWall) velocity.x = 0;
                    else velocity.x = -walkSpeed;
                }

                break;
        }
        updatePhysics();
    }
}
