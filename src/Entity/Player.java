package Entity;

import Handlers.KeyHandler;
import Handlers.Keys;
import TileMap.TileMap;
import TileMap.Vector2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

import static Entity.CharacterState.*;

public class Player extends MovingObject{

    private int health;
    private int exp;
    private int lives;
    private int dmg;

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
        jumpSpeed = -4;
        walkSpeed = 2;
        minJumpingSpeed = -1;
        maxFallingSpeed = 2;
        gravity = 0.2;

        // set up collision box
        collisionBox = new CollisionBox(position, new Vector2(tileSize/2, tileSize/2));
        collisionOffset = collisionBox.halfSize;
    }

    public void update() {
        switch (currentState) {
            case STANDING:
                setVelocity(0, 0);
                if (!isOnGround) {
                    currentState = JUMPING;
                    break;
                }
                if (KeyHandler.isPressed(Keys.RIGHT) != KeyHandler.isPressed(Keys.LEFT)) {
                    currentState = WALKING;
                    break;
                } else if (KeyHandler.isPressed(Keys.JUMP)) {
                    velocity.y = jumpSpeed;
                    currentState = JUMPING;
                    break;
                }
                break;

            case WALKING:
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
                if (KeyHandler.isPressed(Keys.JUMP)) {
                    velocity.y = jumpSpeed;
                    currentState = JUMPING;
                    break;
                } else if (!isOnGround) {
                    currentState = JUMPING;
                    break;
                }
                break;

            case JUMPING:
                velocity.y += gravity;
                velocity.y = Math.min(velocity.y, maxFallingSpeed);
                if (isOnGround) {
                    if (KeyHandler.isPressed(Keys.LEFT) == KeyHandler.isPressed(Keys.RIGHT)) {
                        currentState = STANDING;
                        setVelocity(0, 0);
                    } else {
                        currentState = WALKING;
                        velocity.y = 0;
                    }
                }
                if (!KeyHandler.isPressed(Keys.JUMP) && velocity.y < 0) {
                    velocity.y = Math.max(velocity.y, minJumpingSpeed);
                }
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

        //System.out.println(isOnGround + "     " + position.toString() + "     " + velocity.toString() + "     " + currentState);
    }
}
