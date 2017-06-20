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

    private final int[] NUMFRAMES = {1, 6, 1};
    private final int[] FRAMEWIDTHS = {128, 128, 128};
    private final int[] FRAMEHEIGHTS = {128, 128, 128};
    private final int[] SPRITEDELAYS = {-1, 8, -1};

    public Player(TileMap tm) {
        super(tm);

        width = FRAMEWIDTHS[0];
        height = FRAMEHEIGHTS[0];
        loadSprites("dusk_spritesheet_128.png", NUMFRAMES, FRAMEWIDTHS, FRAMEHEIGHTS);
    }

    public void initPlayer(Vector2 position) {
        setPosition(position);
        // set up speeds
        jumpSpeed = -14;
        walkSpeed = 6;
        minJumpingSpeed = -2;
        maxFallingSpeed = 10;
        gravity = 0.5;
        minFallSpeed = 3;
        // set up collision box
        collisionBox.setCenter(position);
        collisionBox.setHalfSize(new Vector2(tileSize/3 , tileSize/3 - 18));
        collisionOffset = new Vector2(tileSize / 2 - 1, collisionBox.halfSize.y + 38);
    }

    @Override
    public void update() {
        handleInputs();
        animation.update();
        updatePhysics();
        updateInvulnerability();
        checkCollision();
        updateAlpha();
    }

    //---- State handling ---------------------------------------------------------------------------------
    private void handleInputs() {
        switch (currentState) {
            case IDLE:
                setVelocity(0, 0);
                // Check if in air
                if (checkAndHandleInAir()) break;
                // Check if Keys.JUMP has been released
                if (checkAndHandleJumpReleased());
                // Check if walking
                if (checkAndHandleWalking()) break;
                // Check for platform drop
                else if (checkAndHandlePlatformDrop());
                // Check for jump
                else if (checkAndHandleJump()) break;
                break;
            case WALKING:
                // Check if in air
                if (checkAndHandleInAir()) break;
                // Check if Keys.JUMP has been released
                if (checkAndHandleJumpReleased());
                // Check if idling
                if (checkAndHandleIdling()) break;
                // Check for walls
                if (checkAndHandleWalls());
                // Check for platform drop
                if (checkAndHandlePlatformDrop());
                // Check for jump
                else if (checkAndHandleJump()) break;
                break;
            case JUMPING:
                // Update isRising boolean
                updateIsRising();
                // Update y velocity
                updateYVelocity();
                // Check if on ground
                if (isOnGround) {
                    // Check if idling
                    if (checkAndHandleIdling());
                    // Check if walking
                    else {
                        setAnimation(WALKING);
                        velocity.y = 0;
                    }
                    // Check if Keys.JUMP has been released
                    if (checkAndHandleJumpReleased());
                }
                // Check for preemptive release of JUMP while jumping
                checkForEarlyReleaseOfJump();
                // Update isFalling boolean
                updateIsFalling();
                // Update rising/falling animation
                if (checkAndHandleJumpDirectionChange());
                // Check if both LEFT and RIGHT are being pressed
                if (checkAndHandleLeftAndRightPressed()) break;
                // Check for walls
                else if (checkAndHandleWalls());
                break;
            case FLINCHING:
                // Check if still flinching
                if (checkAndHandleStillFlinching());
                // Start blinking and stop flinching
                else stopFlinching();
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

    public void setInvulnerable(boolean b) {
        isInvulnerable = b;
    }

    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    private void updateAlpha() {
        if(isInvulnerable && isBlinking)
            alpha = (float)(Math.sin(Time.getCurrentTime() * 0.0013) * 0.2 + 0.66);
    }

    private void updateInvulnerability() {
        if (invulnerabilityTimer < invulnerabilityTime) {
            invulnerabilityTimer += Math.round(Time.deltaTime);
        } else if (isInvulnerable){
            setInvulnerable(false);
            setAnimation(JUMPING);
            alpha = 1;
            isBlinking = false;
        }
    }

    //---- Collision handling ---------------------------------------------------------------------------------
    public void addCollisionCheck(MapObject mapObject) {
        mapObjects.add(mapObject);
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

    //---- Helpers for state handling ---------------------------------------------------------------------------------
    private boolean checkAndHandleInAir() {
        if (!isOnGround) {
            setAnimation(JUMPING);
            return true;
        }
        return false;
    }

    private boolean checkAndHandleWalking() {
        if (KeyHandler.isPressed(Keys.RIGHT) != KeyHandler.isPressed(Keys.LEFT)) {
            setAnimation(WALKING);
            return true;
        }
        return false;
    }

    private boolean checkAndHandleIdling() {
        if (KeyHandler.isPressed(Keys.RIGHT) == KeyHandler.isPressed(Keys.LEFT)) {
            setAnimation(IDLE);
            setVelocity(0, 0);
            return true;
        }
        return false;
    }

    private boolean checkAndHandlePlatformDrop() {
        if (KeyHandler.isPressed(Keys.DOWN) && KeyHandler.isPressed(Keys.JUMP)) {
            if (isOnPlatform) {
                position.y += 1;
                framesPassedUntilDrop = 0;
                isOnPlatform = false;
                velocity.y = minFallSpeed;
                hasJumped = true;
                return true;
            }
        }
        return false;
    }

    private boolean checkAndHandleJump() {
        if (KeyHandler.isPressed(Keys.JUMP) && !hasJumped) {
            velocity.y = jumpSpeed;
            setAnimation(JUMPING);
            hasJumped = true;
            return true;
        }
        return false;
    }

    private boolean checkAndHandleWalls() {
        if (KeyHandler.isPressed(Keys.RIGHT)) {
            if (isPushingRightWall) velocity.x = 0;
            else velocity.x = walkSpeed;
            isFacingRight = true;
            return true;
        } else if (KeyHandler.isPressed(Keys.LEFT)) {
            if (isPushingLeftWall) velocity.x = 0;
            else velocity.x = -walkSpeed;
            isFacingRight = false;
            return true;
        }
        return false;
    }

    private boolean checkAndHandleLeftAndRightPressed() {
        if (KeyHandler.isPressed(Keys.RIGHT) == KeyHandler.isPressed(Keys.LEFT)) {
            velocity.x = 0;
            return true;
        }
        return false;
    }

    private boolean checkAndHandleJumpDirectionChange() {
        if(isRising && isFalling) {
            setAnimation(JUMPING);
            return true;
        }
        return false;
    }

    private boolean checkAndHandleJumpReleased() {
        if (hasJumped && !KeyHandler.isPressed(Keys.JUMP)) {
            hasJumped = false;
            return true;
        }
        return false;
    }

    private void updateYVelocity() {
        velocity.y += gravity * Time.deltaTime;
        velocity.y = Math.min(velocity.y, maxFallingSpeed);
    }

    private void checkForEarlyReleaseOfJump() {
        if (!KeyHandler.isPressed(Keys.JUMP) && velocity.y < 0) {
            velocity.y = Math.min(-velocity.y, -minJumpingSpeed);
        }
    }

    private void updateIsRising() {
        isRising = velocity.y < 0;
    }

    private void updateIsFalling() {
        isFalling = velocity.y > 0;
    }

    private boolean checkAndHandleStillFlinching() {
        if (currentFlinchTime < flinchTime) {
            currentFlinchTime += Math.round(Time.deltaTime);
            return true;
        }
        return false;
    }

    private void stopFlinching() {
        setAnimation(JUMPING);
        isBlinking = true;
        rotation = 0;
    }

    //---- Getters and setters ---------------------------------------------------------------------------------
    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
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
