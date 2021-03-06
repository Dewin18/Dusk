package Entity;

import Main.Time;
import TileMap.TileMap;
import Helpers.Vector2;

import java.awt.*;

/** PTP 2017
 * All enemy subclasses inherit from this abstract class Enemy.
 *
 * @author Dewin Bagci
 * @version 15.08.
 * @since 01.06.
 */
public abstract class Enemy extends MovingObject
{
    protected int health;
    protected int walkspeed;
    protected int damage;
    protected double knockback = 3;

    protected boolean isInvulnerable;
    protected int invulnerableTime;
    protected int currentInvulnerableTime;

    public Enemy(TileMap tm)
    {
        super(tm);
    }

    /**
     * Enemy initialization method
     *
     * @param position   The position as Vector(x, y) on the map
     * @param spriteName is the selected spriteSheet for this enemy
     */
    public abstract void initEnemy(Vector2 position, String spriteName);

    /**
     * Damage this enemy by a given amount.
     *
     * @param damage the damage being done
     */
    public void getHit(int damage, double playerXPosition)
    {
        if (!isInvulnerable)
        {
            setAnimation(AnimationState.FLINCHING);
            setInvulnerable(true);
            if (playerXPosition < position.x)
            {
                addForce(new Vector2(knockback, 0), 5);
            } else
            {
                addForce(new Vector2(-knockback, 0), 5);
            }
            health -= damage;
            if (health <= 0) {
                die();
                notifyObservers(this);
            }
        }
    }

    /**
     * Update the invulnerable state.
     */
    private void updateInvulnerability()
    {
        if (currentInvulnerableTime < invulnerableTime)
        {
            currentInvulnerableTime += Math.round(Time.deltaTime);
        }
        else if (isInvulnerable)
        {
            setInvulnerable(false);
            setAnimation(AnimationState.JUMPING);
        }
    }

    /**
     * Set the invulnerable state of this enemy.
     *
     * @param b the invulnerability state
     */
    public void setInvulnerable(boolean b)
    {
        isInvulnerable = b;
        if (b) {
            currentInvulnerableTime = 0;
        }
    }

    /**
     * Trigger the dying animation and actions triggered with it
     */
    protected abstract void die();

    /**
     * Update all enemy actions
     *
     * Should implement the enemy movements e.g. a private method moveAround();
     */
    @Override
    public void update()
    {
        updateInvulnerability();
        animation.update();
        super.update();
        updatePhysics();
    }

    /**
     * Draw the sprite on the screen.
     *
     * @param g the graphic context to be drawn on
     */
    @Override
    public void draw(Graphics2D g)
    {
        super.draw(g);
    }
    
    // Setters for all difficulty modes (easy, medium, hard)
    public void setWalkSpeed(int walkSpeed)
    {
        this.walkSpeed = walkSpeed;
    }
    
    public void setHealth(int health)
    {
        this.health = health;
    }
}
