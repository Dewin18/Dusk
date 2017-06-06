package Entity;

import TileMap.TileMap;
import TileMap.Vector2;

public class MovingObject extends MapObject{

    // Movement
    protected Vector2 velocity;
    protected Vector2 oldPosition;
    protected Vector2 oldVelocity;
    protected double jumpSpeed;
    protected double walkSpeed;
    protected double gravity = 0.5;
    protected double maxFallingSpeed;
    protected double minJumpingSpeed;

    // collision states
    boolean isPushingRightWall;
    boolean pushedRightWall;
    boolean isPushingLeftWall;
    boolean pushedLeftWall;
    boolean isOnGround;
    boolean wasOnGround;
    boolean isAtCeiling;
    boolean wasAtCeiling;

    public MovingObject(TileMap tm) {
        super(tm);
    }

    public void setVelocity(double x, double y) {
        if(velocity == null) {
            velocity = new Vector2(x, y);
            return;
        }
        this.velocity.x = x;
        this.velocity.y = y;
    }



    public void updatePhysics() {
        oldPosition = position;
        oldVelocity = velocity;
        wasOnGround = isOnGround;
        wasAtCeiling = isAtCeiling;
        pushedLeftWall = isPushingLeftWall;
        pushedRightWall = isPushingRightWall;

        position.x += velocity.x;
        position.y += velocity.y;

        if(position.y >= 175) {
            position.y = 175;
            isOnGround = true;
        } else {
            isOnGround = false;
        }

        collisionBox.center = position.add(collisionOffset);
    }
}
