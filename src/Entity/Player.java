package Entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.util.ArrayList;

import Audio.JukeBox;
import Entity.PlayerState.IdleState;
import Entity.PlayerState.PlayerState;
import Handlers.KeyHandler;
import Handlers.Keys;
import Main.Time;
import TileMap.TileMap;
import Helpers.Vector2;

import static Entity.AnimationState.*;

public class Player extends MovingObject
{
    // Active PlayerState
    private PlayerState currentPlayerState;

    // Animation
    private final int[] NUMFRAMES = {4, 6, 1, 1, 1, 2, 2, 2};
    private final int[] FRAMEWIDTHS = {128, 128, 128, 256, 128, 128, 128, 128};
    private final int[] FRAMEHEIGHTS = {128, 128, 128, 128, 128, 128, 128, 128};
    private final int[] SPRITEDELAYS = {12, 7, -1, -1, 8, 8, 8, 8};

    // Basic Values
    private double knockback = 2;
    private double minFallSpeed;
    private int health;
    private int exp;
    private int lives;
    private int dmg = 10;

    // Attacking
    private boolean isAttacking = false;
    private boolean hitByEnemy;
    private boolean hitEnemy;
    private final int attackTime = 8;
    private int currentAttackTime = attackTime;
    private int attackCooldown = 10;
    private int attackCooldownElapsed = attackCooldown;
    private ArrayList<MapObject> objectsToRemove; // To remove the collider from a dead enemy
    private ArrayList<MapObject> mapObjects = new ArrayList<>(); // MapObjects to check collision with

    // Flinching
    private boolean isInvulnerable = false;
    private boolean isBlinking = false;
    private boolean isFlinching = false;
    private boolean isGameOver = false;
    private final int invulnerabilityTime = 80;
    private final int flinchTime = 5;
    private int invulnerabilityTimer = invulnerabilityTime;
    private int currentFlinchTime = flinchTime;

    // HUD
    private HUD hud;


    public Player(TileMap tm)
    {
        super(tm);
        hud = new HUD();

        width = FRAMEWIDTHS[0];
        height = FRAMEHEIGHTS[0];
        loadSprites("dusk_spritesheet_128.png", NUMFRAMES, FRAMEWIDTHS, FRAMEHEIGHTS);

        currentPlayerState = new IdleState(this);
        initValues();
    }

    private void initValues()
    {
        // set up health
        health = 100;
        lives = 3;
        
        // set up speeds
        gravity = 0.5;
        walkSpeed = 6;
        jumpSpeed = -16;
        minJumpSpeed = -2;
        minFallSpeed = 3;
        maxFallSpeed = 12;
    }

    public void initPlayer(Vector2 position)
    {
        setPosition(position);
        // set up collision box
        collisionBox.setCenter(position);
        collisionBox.setHalfSize(new Vector2(tileSize / 3, tileSize / 3 - 18));
        collisionOffset = new Vector2(tileSize / 2 - 1, collisionBox.halfSize.y + 38);
        // set up attack collider
        attackColliderOffset = new Vector2(tileSize + tileSize/8, tileSize / 2);
        // load sound files
        Thread t = new Thread(() ->
        {
            JukeBox.load("grass_step1.mp3", "grassstep1");
            JukeBox.load("grass_step2.mp3", "grassstep2");
            JukeBox.load("grass_step3.mp3", "grassstep3");
            JukeBox.load("grass_step4.mp3", "grassstep4");
            JukeBox.load("jump.mp3", "jump");
            JukeBox.load("landing.mp3", "landing");
            JukeBox.load("attack_woosh.mp3", "attack");
            JukeBox.load("enemy_hit.mp3", "hit");
            JukeBox.load("get_hit.mp3", "gethit");
        });
        t.start();
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
        drawHUD(g);
    }

    private void drawHUD(Graphics2D g)
    {
        hud.draw(g, health, lives);

        if(hud.getHealthBarValue() <= 0)
        {
            lives--;

            if(lives < 0)
            {
                isGameOver = true;
                lives = 0;
            }

            health = 100;
            hud.setHealthBarValue(100);
        }
    }

    //---- State handling ---------------------------------------------------------------------------------
    private void handleInputs()
    {
        if (isFlinching)
        {
            if (currentFlinchTime < flinchTime)
            {
                currentFlinchTime += Math.round(Time.deltaTime);
            }
            else
            {
                isBlinking = true;
                rotation = 0;
                isFlinching = false;
                currentPlayerState.resetAnimation();
            }
            return;
        }

        PlayerState state = currentPlayerState.update();
        if (state != null)
        {
            currentPlayerState.exit();
            currentPlayerState = state;
            state.enter();
        }
    }

    @Override
    public void setAnimation(AnimationState state)
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
                    statenr = 4;
                    break;
                case FALLING:
                    statenr = 5;
                    break;
                case FALLING_LINES:
                    statenr = 7;
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

    private void setAnimationAttacking()
    {
        animation.setFrames(sprites.get(3));
        animation.setDelay(SPRITEDELAYS[3]);
        width = FRAMEWIDTHS[3];
        height = FRAMEHEIGHTS[3];
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
        }
        else if (isInvulnerable)
        {
            setInvulnerable(false);
            alpha = 1;
            isBlinking = false;
            currentPlayerState.resetAnimation();
        }
    }

    public void walkInDirection(Vector2 dir)
    {
        if (dir == Vector2.LEFT)
        {
            setVelocityX(-walkSpeed);
        }
        else if (dir == Vector2.RIGHT)
        {
            setVelocityX(walkSpeed);
        }
    }

    public void jump()
    {
        velocity.y = jumpSpeed;
    }

    public void fall()
    {
        position.y += 1;
        framesPassedUntilDrop = 0;
        isOnPlatform = false;
        velocity.y = minFallSpeed;
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
        }
        else
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
                if (m.getPosition().x > position.x)
                {
                    addForce(new Vector2(-4, 0), 1);
                }
                else
                {
                    addForce(new Vector2(4, 0), 1);
                }
                hitEnemy = true;
                JukeBox.play("hit");
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
            if (e.position.x < this.position.x)
            {
                addForce(new Vector2(knockback, 0), 10);
                rotation = 45;
            } else
            {
                addForce(new Vector2(-knockback, 0), 10);
                rotation = -45;
            }
            Time.freeze(15);
            setInvulnerable(true);
            health-= 20;
            setHitByEnemy(true);
            currentFlinchTime = 0;
            invulnerabilityTimer = 0;
            isFlinching = true;
            setAnimation(FLINCHING);
            JukeBox.play("gethit");
        }
    }

    private void checkAndHandleAttack()
    {
        if (KeyHandler.hasJustBeenPressed(Keys.ATTACK) && !isAttacking && currentState != FLINCHING && attackCooldownElapsed >= attackCooldown)
        {
            if (isFacingRight)
            {
                attackCollider = new CollisionBox(position.add(attackColliderOffset), new Vector2(tileSize / 3, tileSize / 3));
            } else
            {
                attackCollider = new CollisionBox(position.add(attackColliderOffset).addX(attackColliderOffset.x * -2 + tileSize), new Vector2(tileSize / 3, tileSize / 3));
            }
            setAnimationAttacking();
            isAttacking = true;
            currentAttackTime = 0;
            checkAttackCollision();
            JukeBox.play("attack");
        }
        else if (currentAttackTime < attackTime)
        {
            checkAttackCollision();
            currentAttackTime++;
            if (isFacingRight) attackCollider.center = position.add(attackColliderOffset);
            else attackCollider.center = position.add(attackColliderOffset).addX(attackColliderOffset.x* -2 + tileSize);
            if (currentAttackTime == attackTime)
            {
                isAttacking = false;
                setAnimation(currentState);
                attackCollider = null;
                attackCooldownElapsed = 0;
            }
        }
        else if (attackCooldownElapsed < attackCooldown) attackCooldownElapsed++;
    }

    //---- Getters and setters ---------------------------------------------------------------------------------
    public void setLives(int lives)
    {
        this.lives = lives;
    }

    public int getLives()
    {
        return lives;
    }
    
    public void setHealth(int health)
    {
        this.health = health;
    }
    
    public int getHealth()
    {
        return health;
    }
    
    public AnimationState getCharacterState()
    {
        return currentState;
    }

    public void setHitByEnemy(boolean b)
    {
        hitByEnemy = b;
    }

    public boolean isHitByEnemy()
    {
        return hitByEnemy;
    }

    public boolean isGameOver()
    {
        return isGameOver;
    }

    public boolean isOnGround()
    {
        return isOnGround;
    }

    public boolean isOnPlatform()
    {
        return isOnPlatform;
    }

    public boolean isPushingRightWall()
    {
        return isPushingRightWall;
    }

    public boolean isPushingLeftWall()
    {
        return isPushingLeftWall;
    }

    public boolean isAttacking()
    {
        return isAttacking;
    }

    public boolean isFacingRight()
    {
        return isFacingRight;
    }

    public void setHitEnemy(boolean b)
    {
        hitEnemy = b;
    }

    public boolean getHitEnemy()
    {
        return hitEnemy;
    }

    public void setFacingRight(boolean b)
    {
        isFacingRight = b;
    }

    public boolean isInvulnerable()
    {
        return isInvulnerable;
    }

    public void setInvulnerable(boolean b)
    {
        isInvulnerable = b;
    }
    
    public double getGravity()
    {
        return gravity;
    }
    
    public double getWalkSpeed()
    {
        return walkSpeed;
    }
    
    public double getJumpSpeed()
    {
        return jumpSpeed;
    }
    
    public double getMinJumpSpeed()
    {
        return minJumpSpeed;
    }
    
    public double getMinFallSpeed()
    {
        return minFallSpeed;
    }
    
    public double getMaxFallSpeed()
    {
        return maxFallSpeed;
    }
}
