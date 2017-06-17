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

import static Entity.CharacterState.*;

public class Player extends MovingObject{

    private int health;
    private int exp;
    private int lives;
    private int dmg;

    private boolean isInvulnerable = false;
    private boolean isRising = false;
    private boolean isFalling = false;
    private double minFallSpeed;
    private boolean hasJumped; // for canceling repeat jumps by keeping the button pressed
    private CharacterState currentState = IDLE;
    private final int[] NUMFRAMES = {
            1, 6, 1
    };
    private final int[] FRAMEWIDTHS = {
            128, 128, 128
    };
    private final int[] FRAMEHEIGHTS = {
            128, 128, 128
    };
    private final int[] SPRITEDELAYS = {
            -1, 8, -1
    };

    private boolean hasAttack = true;
    private boolean hasDoubleJump = false;
    private boolean hasDash = false;

    private final double knockBack = 10;
    private final int flinchTime = 5;
    private int currentFlinchTime = flinchTime;
    private final int invulnerabilityTime = 80;
    private boolean isBlinking = false;
    private int invulnerabilityTimer = invulnerabilityTime;
    private ArrayList<MapObject> mapObjects = new ArrayList<>();

    public Player(TileMap tm) {
        super(tm);
        width = 128;
        height = 128;

        // load sprites
        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream(
                    "/Sprites/dusk_spritesheet_128.png"));
            sprite = spritesheet.getSubimage(0, 0, 128, 128);
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
        jumpSpeed = -12.5;
        walkSpeed = 6;
        minJumpingSpeed = -1;
        maxFallingSpeed = 4;
        gravity = 0.3;
        minFallSpeed = 3;

        // set up collision box
        collisionBox = new CollisionBox(position, new Vector2(tileSize/3 , tileSize/3 - 18));
        collisionOffset = new Vector2(tileSize / 2 - 1, collisionBox.halfSize.y + 38);
    }

    public void update() {
        handleInputs();
        animation.update();
        updatePhysics();
        updateInvulnerability();
        checkCollision();
        updateAlpha();
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
            case FLINCHING:
                if (currentFlinchTime < flinchTime) currentFlinchTime += Math.round(Time.deltaTime);
                else {
                    setAnimation(IDLE);
                    isBlinking = true;
                    rotation = 0;
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
                if(isFalling) statenr = 0;
                else statenr = 0;
                break;
            case FLINCHING:
                statenr = 2;
                break;
        }
        animation.setFrames(sprites.get(statenr));
        animation.setDelay(SPRITEDELAYS[statenr]);
    }

    private void updateInvulnerability() {
        if (invulnerabilityTimer < invulnerabilityTime) {
            invulnerabilityTimer += Math.round(Time.deltaTime);
        } else if (isInvulnerable){
            setInvulnerable(false);
            setAnimation(IDLE);
            alpha = 1;
            isBlinking = false;
        }
    }

    public void addCollisionCheck(MapObject mapObject) {
        mapObjects.add(mapObject);
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    public int getHealth() {
        return health;
    }

    public void setInvulnerable(boolean b) {
        isInvulnerable = b;
    }

    private void checkCollision() {
        for (MapObject m : mapObjects) {
            if(collisionBox.overlaps(m.collisionBox)) {
                reactToCollision(m);
            }
        }
    }

    private void reactToCollision(MapObject m) {
        if (m instanceof Enemy) reactToCollision((Enemy) m);
    }

    private void updateAlpha() {
        if(isInvulnerable && isBlinking)
            alpha = (float)(Math.sin(Time.getCurrentTime() * 0.0013) * 0.2 + 0.66);
    }

    private void reactToCollision(Enemy e) {
        if (!isInvulnerable()) {
            Time.freeze(10);
            if (e.position.x < this.position.x) {
                velocity.x = knockBack;
                velocity.y = 0;
                rotation = 45;
            } else {
                velocity.x = -knockBack;
                velocity.y = 0;
                rotation = -45;
            }
            setInvulnerable(true);
            --health;
            currentFlinchTime = 0;
            invulnerabilityTimer = 0;
            setAnimation(FLINCHING);
        }
    }

    public CharacterState getCharacterState()
    {
        return currentState;
    }

    public double getMinFallSpeed()
    {
        return minFallSpeed;
    }
}
