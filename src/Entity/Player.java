package Entity;

import static Entity.CharacterState.FLINCHING;
import static Entity.CharacterState.IDLE;
import static Entity.CharacterState.JUMPING;
import static Entity.CharacterState.WALKING;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import Audio.JukeBox;
import Handlers.KeyHandler;
import Handlers.Keys;
import Main.Time;
import TileMap.TileMap;
import TileMap.Vector2;

public class Player extends MovingObject
{
   
    private final int flinchTime = 5;
    private final int attackTime = 8;
    private final int invulnerabilityTime = 80;
    private final int[] NUMFRAMES = {4, 6, 1, 1, 1, 2, 2, 2};
    private final int[] FRAMEWIDTHS = {128, 128, 128, 256, 128, 128, 128, 128};
    private final int[] FRAMEHEIGHTS = {128, 128, 128, 128, 128, 128, 128, 128};
    private final int[] SPRITEDELAYS = {12, 7, -1, -1, 8, 8, 8, 8};
    double knockback = 15;
    private int health = 5;
    private int exp;
    private int lives;
    private int dmg = 3;
    private ArrayList<MapObject> objectsToRemove;
    private boolean isAttacking = false;
    private boolean isInvulnerable = false;
    private boolean isRising = false;
    private boolean isFalling = false;
    private double minFallSpeed;
    private int fallingTime = 0;
    private int fallingLinesTriggerTime = 60;
    private boolean hasJumped; // for canceling repeat jumps by keeping the button pressed
    private boolean hasAttack = true;
    private boolean hasDoubleJump = false;
    private boolean hasDash = false;
    private int currentFlinchTime = flinchTime;
    private int currentAttackTime = attackTime;
    private boolean isBlinking = false;
    private boolean hitByEnemy;
    
    private HUD hud;

    private int invulnerabilityTimer = invulnerabilityTime;
    private ArrayList<MapObject> mapObjects = new ArrayList<>();

    private int stepSoundMaxTime = 20;
    private int currentStepSoundTime = stepSoundMaxTime;

    public Player(TileMap tm)
    {
        super(tm);

        width = FRAMEWIDTHS[0];
        height = FRAMEHEIGHTS[0];
        loadSprites("dusk_spritesheet_128.png", NUMFRAMES, FRAMEWIDTHS, FRAMEHEIGHTS);
        JukeBox.load("grass_step1.mp3", "grassstep1");
        JukeBox.load("grass_step2.mp3", "grassstep2");
        JukeBox.load("grass_step3.mp3", "grassstep3");
        JukeBox.load("grass_step4.mp3", "grassstep4");
        JukeBox.load("jump.mp3", "jump");
        JukeBox.load("landing.mp3", "landing");
    }

    public void initPlayer(Vector2 position)
    {
        //hud = new HUD(this, new Font("Arial", Font.PLAIN, 23));
        
        
        setPosition(position);
        // set up speeds
        jumpSpeed = -16;
        walkSpeed = 6;
        minJumpingSpeed = -2;
        maxFallingSpeed = 12;
        gravity = 0.5;
        minFallSpeed = 3;
        // set up collision box
        collisionBox.setCenter(position);
        collisionBox.setHalfSize(new Vector2(tileSize / 3, tileSize / 3 - 18));
        collisionOffset = new Vector2(tileSize / 2 - 1, collisionBox.halfSize.y + 38);
        // set up attack collider
        attackColliderOffset = new Vector2(tileSize + tileSize/3, tileSize / 2);
    }

    @Override
    public void update()
    {
        handleInputs();
        animation.update();
        updateForce();
        updatePhysics();
        updateInvulnerability();
        checkCollision();
        updateAlpha();
        checkAndHandleAttack();
    }

    @Override
    public void draw(Graphics2D g)
    {
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g.setComposite(ac);
        super.draw(g);
        AlphaComposite a = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1);
        g.setComposite(a);
    }

    //---- State handling ---------------------------------------------------------------------------------
    private void handleInputs()
    {
        switch (currentState)
        {
            case IDLE:
                setVelocity(0, 0);
                if (checkAndHandleInAir()) break;
                if (checkAndHandleJumpReleased()) ;
                if (checkAndHandleWalking()) break;
                else if (checkAndHandlePlatformDrop()) ;
                else if (checkAndHandleJump()) break;
                break;
            case WALKING:
                if (checkAndHandleInAir()) break;
                playWalkingSound();
                if (checkAndHandleJumpReleased()) ;
                if (checkAndHandleIdling()) break;
                if (checkAndHandleWalls()) ;
                if (checkAndHandlePlatformDrop()) ;
                else if (checkAndHandleJump()) break;
                break;
            case JUMPING:
                if (isOnGround)
                {
                    JukeBox.play("landing");
                    JukeBox.play("grassstep4");
                    if (checkAndHandleIdling()) ;
                    else
                    {
                        setAnimation(WALKING);
                        velocity.y = 0;
                    }
                    if (checkAndHandleJumpReleased()) ;
                }
                updateIsRising();
                updateYVelocity();
                checkForEarlyReleaseOfJump();
                updateIsFalling();
                updateFallingTime();
                if (checkAndHandleJumpDirectionChange()) ;
                if (checkAndHandleLeftAndRightPressed()) break;
                else if (checkAndHandleWalls()) ;
                break;
            case FLINCHING:
                if (checkAndHandleStillFlinching()) ;
                else stopFlinching();
                break;
        }
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

    @Override
    void setAnimation(CharacterState state)
    {
        currentState = state;
        if (!isAttacking)
        {
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
                    if (isFalling)
                    {
                        statenr = 5;
                        if (fallingTime > fallingLinesTriggerTime)
                        {
                            statenr = 7;
                            fallingTime = 0;
                        }
                    }
                    else statenr = 4;
                    break;
                case FLINCHING:
                    statenr = 2;
                    break;
                case ATTACKING:
                    statenr = 3;
                    break;
            }
            animation.setFrames(sprites.get(statenr));
            animation.setDelay(SPRITEDELAYS[statenr]);
            width = FRAMEWIDTHS[statenr];
            height = FRAMEHEIGHTS[statenr];
        }
    }

    private void setAnimation(int statenr)
    {
        animation.setFrames(sprites.get(statenr));
        animation.setDelay(SPRITEDELAYS[statenr]);
        width = FRAMEWIDTHS[statenr];
        height = FRAMEHEIGHTS[statenr];
    }

    public boolean isInvulnerable()
    {
        return isInvulnerable;
    }

    public void setInvulnerable(boolean b)
    {
        isInvulnerable = b;
    }

    private void updateAlpha()
    {
        if (isInvulnerable && isBlinking) alpha = (float) (Math.sin(Time.getCurrentTime() * 0.0013) * 0.2 + 0.66);
    }

    private void updateInvulnerability()
    {
        if (invulnerabilityTimer < invulnerabilityTime)
        {
            invulnerabilityTimer += Math.round(Time.deltaTime);
        } else if (isInvulnerable)
        {
            setInvulnerable(false);
            setAnimation(JUMPING);
            alpha = 1;
            isBlinking = false;
        }
    }

    //---- Collision handling ---------------------------------------------------------------------------------
    public void addCollisionCheck(MapObject mapObject)
    {
        mapObjects.add(mapObject);
    }

    public void removeCollisionCheck(MapObject mapObject)
    {
        mapObjects.remove(mapObject);
    }

    public void addObjectToBeRemoved(MapObject o)
    {
        if (objectsToRemove == null)
        {
            ArrayList<MapObject> list = new ArrayList<>();
            list.add(o);
            objectsToRemove = list;
        } else
        {
            objectsToRemove.add(o);
        }
    }

    private void checkCollision()
    {
        for (MapObject m : mapObjects)
        {
            if (collisionBox.overlaps(m.collisionBox))
            {
                reactToCollision(m);
            }
        }
    }

    private void checkAttackCollision()
    {
        for (MapObject m : mapObjects)
        {
            if (attackCollider.overlaps(m.collisionBox))
            {
                reactToAttackCollision(m);
                addForce(new Vector2(-1, 0), 10);
            }
        }
        if (objectsToRemove != null)
        {
            for (MapObject o : objectsToRemove)
            {
                removeCollisionCheck(o);
            }
            objectsToRemove = null;
        }
    }

    private void reactToAttackCollision(MapObject m)
    {
        if (m instanceof Enemy) reactToAttackCollision((Enemy) m);
    }

    private void reactToAttackCollision(Enemy e)
    {
        e.getHit(dmg, position.x);
    }

    private void reactToCollision(MapObject m)
    {
        if (m instanceof Enemy) reactToCollision((Enemy) m);
    }

    private void reactToCollision(Enemy e)
    {
        if (!isInvulnerable())
        {
            Time.freeze(15);
            if (e.position.x < this.position.x)
            {
                velocity.x = knockback;
                rotation = 45;
            } else
            {
                velocity.x = -knockback;
                rotation = -45;
            }
            setInvulnerable(true);
            --health;

            //TODO System.out.println(health);
            setHitByEnemy(true);
            currentFlinchTime = 0;
            invulnerabilityTimer = 0;
            setAnimation(FLINCHING);
        }
    }

    //---- Helpers for state handling ---------------------------------------------------------------------------------
    private void checkAndHandleAttack()
    {
        if (KeyHandler.hasJustBeenPressed(Keys.ATTACK) && !isAttacking && currentState != FLINCHING)
        {
            if (isFacingRight)
            {
                attackCollider = new CollisionBox(position.add(attackColliderOffset), new Vector2(tileSize / 3, tileSize / 3));
            } else
            {
                attackCollider = new CollisionBox(position.add(attackColliderOffset).addX(attackColliderOffset.x * -2 + tileSize), new Vector2(tileSize / 3, tileSize / 3));
            }
            setAnimation(3);
            isAttacking = true;
            currentAttackTime = 0;
            checkAttackCollision();
        }
        else if (currentAttackTime < attackTime)
        {
            checkAttackCollision();
            currentAttackTime++;
            if (currentAttackTime == attackTime)
            {
                isAttacking = false;
                setAnimation(currentState);
                attackCollider = null;
            }
        }
    }

    private boolean checkAndHandleInAir()
    {
        if (!isOnGround)
        {
            setAnimation(JUMPING);
            currentStepSoundTime = stepSoundMaxTime;
            return true;
        }
        return false;
    }

    private boolean checkAndHandleWalking()
    {
        if (KeyHandler.isPressed(Keys.RIGHT) != KeyHandler.isPressed(Keys.LEFT))
        {
            setAnimation(WALKING);
            return true;
        }
        return false;
    }

    private boolean checkAndHandleIdling()
    {
        if (KeyHandler.isPressed(Keys.RIGHT) == KeyHandler.isPressed(Keys.LEFT))
        {
            setAnimation(IDLE);
            setVelocity(0, 0);
            currentStepSoundTime = stepSoundMaxTime;
            return true;
        }
        return false;
    }

    private boolean checkAndHandlePlatformDrop()
    {
        if (KeyHandler.isPressed(Keys.DOWN) && KeyHandler.isPressed(Keys.JUMP))
        {
            if (isOnPlatform)
            {
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

    private boolean checkAndHandleJump()
    {
        if (KeyHandler.isPressed(Keys.JUMP) && !hasJumped)
        {
            JukeBox.play("jump");
            velocity.y = jumpSpeed;
            setAnimation(JUMPING);
            hasJumped = true;
            currentStepSoundTime = stepSoundMaxTime;
            return true;
        }
        return false;
    }

    private boolean checkAndHandleWalls()
    {
        if (KeyHandler.isPressed(Keys.RIGHT))
        {
            if (isPushingRightWall) velocity.x = 0;
            else velocity.x = walkSpeed;
            if (!isAttacking) isFacingRight = true;
            return true;
        } else if (KeyHandler.isPressed(Keys.LEFT))
        {
            if (isPushingLeftWall) velocity.x = 0;
            else velocity.x = -walkSpeed;
            if (!isAttacking) isFacingRight = false;
            return true;
        }
        return false;
    }

    private boolean checkAndHandleLeftAndRightPressed()
    {
        if (KeyHandler.isPressed(Keys.RIGHT) == KeyHandler.isPressed(Keys.LEFT))
        {
            velocity.x = 0;
            return true;
        }
        return false;
    }

    private boolean checkAndHandleJumpDirectionChange()
    {
        if (isRising && isFalling)
        {
            setAnimation(JUMPING);
            return true;
        }
        return false;
    }

    private boolean checkAndHandleJumpReleased()
    {
        if (hasJumped && !KeyHandler.isPressed(Keys.JUMP))
        {
            hasJumped = false;
            return true;
        }
        return false;
    }

    private void updateYVelocity()
    {
        velocity.y += gravity * Time.deltaTime;
        velocity.y = Math.min(velocity.y, maxFallingSpeed);
    }

    private void checkForEarlyReleaseOfJump()
    {
        if (!KeyHandler.isPressed(Keys.JUMP) && velocity.y < 0)
        {
            velocity.y = Math.min(-velocity.y, -minJumpingSpeed);
        }
    }

    private void updateIsRising()
    {
        boolean oldIsRising = isRising;
        isRising = velocity.y < 0;
        if (oldIsRising != isRising)
            setAnimation(JUMPING);
    }

    private void updateIsFalling()
    {
        boolean oldIsFalling = isFalling;
        isFalling = velocity.y > 0;
        if (oldIsFalling != isFalling)
            setAnimation(JUMPING);
    }

    private void updateFallingTime()
    {
        if (isRising) fallingTime = 0;
        if (isFalling) fallingTime++;
        if (fallingTime > fallingLinesTriggerTime) setAnimation(JUMPING);
    }

    private boolean checkAndHandleStillFlinching()
    {
        if (currentFlinchTime < flinchTime)
        {
            currentFlinchTime += Math.round(Time.deltaTime);
            return true;
        }
        return false;
    }

    private void stopFlinching()
    {
        setAnimation(JUMPING);
        isBlinking = true;
        rotation = 0;
    }

    //---- Getters and setters ---------------------------------------------------------------------------------
    public int getHealth()
    {
        return health;
    }
    
    public void setHealth(int health)
    {
        this.health = health;
    }

    public CharacterState getCharacterState()
    {
        return currentState;
    }

    public double getMinFallSpeed()
    {
        return minFallSpeed;
    }

    public void setHitByEnemy(boolean b)
    {
        hitByEnemy = b;
    }

    public boolean isHitByEnemy()
    {
        return hitByEnemy;
    }
}
