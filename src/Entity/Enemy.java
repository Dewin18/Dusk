package Entity;

import TileMap.TileMap;
import TileMap.Vector2;

/**
 * All enemy subclasses inherit from this abstract class Enemy. 
 * 
 * @author Dewin & Ali
 *
 */
public abstract class Enemy extends MovingObject
{
    /**
     * All enemies define it's own Maximum health
     */
    protected int health;
    
    /**
     * All enemies define it's own Maximum damage
     */
    protected int damage;

    public Enemy(TileMap tm)
    { 
        super(tm);
    }
    
    /**
     * Enemy initalization method
     * 
     * @param position The position as Vector(x, y) on the map
     * @param spriteName is the selected spriteSheet for this enemy
     */
    public abstract void initEnemy(Vector2 position, String spriteName);

    /**
     * Update all enemy actions
     * 
     * Should implement the enemy movements e.g. a private method moveAround();
     * Should implement animation.update();
     * Should implement updatePhysics();
     */
    public abstract void update();
}
