package Entity;

import Main.Time;
import TileMap.TileMap;
import TileMap.Vector2;

import java.awt.*;

/**
 * All enemy subclasses inherit from this abstract class Enemy.
 *
 * @author Dewin & Ali
 */
public abstract class Enemy extends MovingObject
{
    /**
     * All enemies define their own Maximum health
     */
    protected int health;

    /**
     * All enemies define their own Maximum damage
     */
    protected int damage;

    protected boolean isInvulnerable;
    protected int invulnerableTime;
    protected int currentInvulnerableTime;
    protected double knockback = 6.75;

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
        if (!isInvulnerable) {
            setAnimation(CharacterState.FLINCHING);
            setInvulnerable(true);
            //velocity.x += knockback;
            if (playerXPosition < position.x)
            {
                addForce(new Vector2(knockback, 0), 30);
            } else
            {
                addForce(new Vector2(-knockback, 0), 30);
            }
            health -= damage;
            if (health <= 0) {
                die();
                notifyObservers();
            }
        }
    }

    /**
     * Update the invulnerable state.
     */
    private void updateInvulnerability() {
        if (currentInvulnerableTime < invulnerableTime) {
            currentInvulnerableTime += Math.round(Time.deltaTime);
        } else if (isInvulnerable) {
            setInvulnerable(false);
            setAnimation(CharacterState.JUMPING);
        }
    }

    /**
     * Set the invulnerable state of this enemy.
     *
     * @param b the invulnerability state
     */
    public void setInvulnerable(boolean b) {
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
    public void update() {
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
    public void draw(Graphics2D g) {
        super.draw(g);
    }
}
