package Entity;

import TileMap.TileMap;
import TileMap.Vector2;

import java.awt.*;

/**
 * All enemy subclasses inherit from this abstract class Enemy. 
 * 
 * @author Dewin & Ali
 *
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
    @Override
    public abstract void update();
}
