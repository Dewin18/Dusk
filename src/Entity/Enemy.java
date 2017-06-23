package Entity;

import TileMap.TileMap;
import TileMap.Vector2;

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
     * Damage this enemy by a given amount
     *
     * @param damage the damage being done
     */
    public void getHit(int damage)
    {
        //TODO Toggle flinching and short invulnerability
        health -= damage;
        if (health <= 0)
        {
            die();
            notifyObservers();
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
     * Should implement animation.update();
     * Should implement updatePhysics();
     */
    @Override
    public abstract void update();
}
