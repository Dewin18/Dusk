package Entity;

import Main.Time;
import TileMap.TileMap;
import TileMap.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

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

    double groundY;

    // debugging
    double triggerLineY;
    double triggerLineX;
    double triggerLineX2;
    Rectangle drawRect = new Rectangle(0, 0, 0 ,0);
    Rectangle drawRect2 = new Rectangle(0, 0, 0 ,0);
    BufferedImage drawImg = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);

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

    public boolean hasGround(Vector2 oldPosition, Vector2 position, Vector2 velocity) {
        Vector2 oldCenter = oldPosition.add(collisionOffset);
        Vector2 center = position.add(collisionOffset);

        Vector2 oldBotLeft = oldCenter.sub(collisionBox.halfSize).add(Vector2.DOWN).add(Vector2.RIGHT);
        Vector2 botLeft = center.sub(collisionBox.halfSize).add(Vector2.DOWN).add(Vector2.RIGHT);
        botLeft.y += collisionBox.halfSize.y * 2;
        Vector2 botRight = new Vector2(botLeft.x + collisionBox.halfSize.x * 2 - 2, botLeft.y);

        triggerLineX = botLeft.x;
        triggerLineY = botLeft.y;
        drawRect = new Rectangle(tileMap.getMapTileXAtPoint(botLeft.x)*tileSize,
                tileMap.getMapTileYAtPoint(botLeft.y)*tileSize, tileSize, tileSize);
        drawRect2 = new Rectangle(tileMap.getMapTileXAtPoint(botRight.x)*tileSize,
                tileMap.getMapTileYAtPoint(botRight.y)*tileSize , tileSize, tileSize);
        drawImg = tileMap.printTile(tileMap.getMapTileXAtPoint(botLeft.x), tileMap.getMapTileYAtPoint(botLeft.y));

        int tileIndexX, tileIndexY;
        for (Vector2 checkedTile = botLeft; ;checkedTile.x += tileSize) {
            checkedTile.x = Math.min(checkedTile.x, botRight.x);

            tileIndexX = tileMap.getMapTileXAtPoint(checkedTile.x);
            tileIndexY = tileMap.getMapTileYAtPoint(checkedTile.y);

            groundY = tileIndexY * tileSize + tileMap.getY();
            if (tileMap.isGround(tileIndexX, tileIndexY) &&
                    tileIndexY * tileSize < botLeft.y && botLeft.y < tileIndexY * tileSize + 3)
                return true;
            if (checkedTile.x >= botRight.x) break;
        }
        return false;
    }

    public void updatePhysics() {
        oldPosition = position;
        oldVelocity = velocity;
        wasOnGround = isOnGround;
        wasAtCeiling = isAtCeiling;
        pushedLeftWall = isPushingLeftWall;
        pushedRightWall = isPushingRightWall;

        position.x += velocity.x * Time.deltaTime;
        position.y += velocity.y * Time.deltaTime;

        groundY = 0;
        if (velocity.y >= 0 && hasGround(oldPosition, position, velocity)) {
            position.y = groundY - collisionBox.halfSize.y - collisionOffset.y;
            velocity.y = 0;
            isOnGround = true;
        } else isOnGround = false;

        collisionBox.center = position.add(collisionOffset);
    }

    public void draw(Graphics2D g) {
        g.drawImage(sprite, (int)position.x, (int)position.y, null);

        // debugging
        g.setColor(Color.BLUE);
        int[] a = collisionBox.toXYWH();
        g.drawRect(a[0], a[1], a[2], a[3]);
        g.setColor(Color.MAGENTA);
        g.drawLine(0, (int)groundY,500, (int)groundY);
        g.setColor(Color.YELLOW);
        g.drawLine(0, (int) triggerLineY,500, (int) triggerLineY);
        g.drawLine((int) triggerLineX, 0, (int) triggerLineX, 500);
        g.setColor(new Color(5, 5, 5, 150));
        g.fillRect(drawRect.x, drawRect.y, drawRect.width, drawRect.height);
        g.fillRect(drawRect2.x, drawRect2.y, drawRect2.width, drawRect2.height);
        g.drawImage(drawImg, 0, 0, null);
    }
}
