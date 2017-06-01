package Entity;

import TileMap.TileMap;

public class MovingObject extends MapObject{

    // Movement
    protected double[] velocity = new double[2];
    protected double[] oldPosition = new double[2];
    protected double[] oldVelocity = new double[2];
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
        this.velocity[0] = x;
        this.velocity[1] = y;
    }

    public void updatePhysics() {
        oldPosition = position;
        oldVelocity = velocity;
        wasOnGround = isOnGround;
        wasAtCeiling = isAtCeiling;
        pushedLeftWall = isPushingLeftWall;
        pushedRightWall = isPushingRightWall;

        position[0] += velocity[0];
        position[1] += velocity[1];

        if(position[1] >= 175) {
            position[1] = 175;
            isOnGround = true;
        } else {
            isOnGround = false;
        }
    }
}
