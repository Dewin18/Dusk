package Entity;

import Handlers.KeyHandler;
import Handlers.Keys;
import Main.Time;
import TileMap.TileMap;
import TileMap.Vector2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import static Entity.CharacterState.IDLE;
import static Entity.CharacterState.JUMPING;
import static Entity.CharacterState.WALKING;

public class Player extends MovingObject{

    private int health;
    private int exp;
    private int lives;
    private int dmg;

    private boolean isRising = false;
    private boolean isFalling = false;
    private double minFallSpeed;
    private boolean hasJumped; // for canceling repeat jumps by keeping the button pressed

    private ArrayList<BufferedImage[]> sprites;
    private final int[] NUMFRAMES = {
            2, 8, 1, 2, 4, 2, 5
    };
    private final int[] FRAMEWIDTHS = {
            30, 30, 30, 30, 30, 30, 60
    };
    private final int[] FRAMEHEIGHTS = {
            30, 30, 30, 30, 30, 30, 30
    };
    private final int[] SPRITEDELAYS = {
            30, 8, -1, 8, 3, 3, 3
    };

    private boolean hasAttack = true;
    private boolean hasDoubleJump = false;
    private boolean hasDash = false;

    public CharacterState currentState = IDLE;

    private long time = 0;

    public Player(TileMap tm) {
        super(tm);
        width = 30;
        height = 30;

        // load sprites
        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream(
                    "/Sprites/Player/PlayerSprites.gif"));
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
        handleInputs();
        animation.update();
        updatePhysics();
    }

    private void handleInputs() {
        switch (currentState) {
            case IDLE:
                setVelocity(0, 0);
                if (!isOnGround) {
                    setAnimation(JUMPING);
                    break;
                }
                if (hasJumped && !KeyHandler.isPressed(Keys.JUMP)) hasJumped = false;
                if (KeyHandler.isPressed(Keys.RIGHT) != KeyHandler.isPressed(Keys.LEFT)) {
                    setAnimation(JUMPING);
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
                    setAnimation(JUMPING);
                    hasJumped = true;
                    break;
                }
                break;

            case WALKING:
                if (hasJumped && !KeyHandler.isPressed(Keys.JUMP)) hasJumped = false;
                if (KeyHandler.isPressed(Keys.RIGHT) == KeyHandler.isPressed(Keys.LEFT)) {
                    setAnimation(IDLE);
                    setVelocity(0, 0);
                    break;
                } else if (KeyHandler.isPressed(Keys.RIGHT)) {
                    if (isPushingRightWall) velocity.x = 0;
                    else velocity.x = walkSpeed;
                    isFacingRight = true;
                } else if (KeyHandler.isPressed(Keys.LEFT)) {
                    if (isPushingLeftWall) velocity.x = 0;
                    else velocity.x = -walkSpeed;
                    isFacingRight = false;
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
                    setAnimation(JUMPING);
                    hasJumped = true;
                    break;
                } else if (!isOnGround) {
                    setAnimation(JUMPING);
                    break;
                }
                break;

            case JUMPING:
                isRising = velocity.y < 0;
                velocity.y += gravity * Time.deltaTime;
                velocity.y = Math.min(velocity.y, maxFallingSpeed);
                if (isOnGround) {
                    if (KeyHandler.isPressed(Keys.LEFT) == KeyHandler.isPressed(Keys.RIGHT)) {
                        setAnimation(IDLE);
                        setVelocity(0, 0);
                    } else {
                        setAnimation(WALKING);
                        velocity.y = 0;
                    }
                    if (hasJumped && !KeyHandler.isPressed(Keys.JUMP)) hasJumped = false;
                }
                if (!KeyHandler.isPressed(Keys.JUMP) && velocity.y < 0) {
                    velocity.y = Math.max(velocity.y, minJumpingSpeed);
                }
                isFalling = velocity.y > 0;
                if(isRising && isFalling) {
                    velocity.y = minFallSpeed;
                    setAnimation(JUMPING);
                }
                if (KeyHandler.isPressed(Keys.RIGHT) == KeyHandler.isPressed(Keys.LEFT)) {
                    velocity.x = 0;
                    break;
                } else if (KeyHandler.isPressed(Keys.RIGHT)) {
                    if (isPushingRightWall) velocity.x = 0;
                    else velocity.x = walkSpeed;
                    isFacingRight = true;
                } else if (KeyHandler.isPressed(Keys.LEFT)) {
                    if (isPushingLeftWall) velocity.x = 0;
                    else velocity.x = -walkSpeed;
                    isFacingRight = false;
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
                if(isFalling) statenr = 3;
                else statenr = 2;
                break;
        }
        animation.setFrames(sprites.get(statenr));
        animation.setDelay(SPRITEDELAYS[statenr]);
    }
}
