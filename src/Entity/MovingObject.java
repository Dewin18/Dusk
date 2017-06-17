package Entity;

import Main.Time;
import TileMap.TileMap;
import TileMap.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MovingObject extends MapObject{

    // debugging
    private final boolean debugging = false;
    
    // Movement
    Vector2 velocity;
    Vector2 oldPosition;
    Vector2 oldVelocity;
    double jumpSpeed;
    double walkSpeed;
    double gravity = 0.5;
    double maxFallingSpeed;
    double minJumpingSpeed;

    // collision states
    boolean isPushingRightWall;
    boolean pushedRightWall;
    boolean isPushingLeftWall;
    boolean pushedLeftWall;
    boolean isOnGround;
    boolean wasOnGround;
    boolean isAtCeiling;
    boolean wasAtCeiling;
    boolean isOnPlatform;

    int framesPassedUntilDrop = 6;
    private int framesToIgnoreGround = 5;
    private double groundY, ceilingY, leftWallX, rightWallX;
    private int mapCollisionSensorDepth = 10;

    boolean isFacingRight = true;

    private double triggerLineY, triggerLineX, triggerLineX2, triggerLineY2;
    private Rectangle drawRect = new Rectangle(0, 0, 0 ,0);
    private Rectangle drawRect2 = new Rectangle(0, 0, 0 ,0);
    private BufferedImage drawImg = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);

    float alpha = 1.0f;

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
        // Update attributes of last frame
        oldPosition = Vector2.copy(position);
        oldVelocity = Vector2.copy(velocity);
        wasOnGround = isOnGround;
        wasAtCeiling = isAtCeiling;
        pushedLeftWall = isPushingLeftWall;
        pushedRightWall = isPushingRightWall;
        isOnPlatform = false;
        // Update the position
        position.x += velocity.x * Time.deltaTime;
        position.y += velocity.y * Time.deltaTime;
        checkMapCollision();
        // Update collision box center
        collisionBox.center = position.add(collisionOffset);
    }

    /**
     * Draw the sprite of the MovingObject
     * @param g the graphic context to be drawn on
     */
    public void draw(Graphics2D g) {
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g.setComposite(ac);
        if(isFacingRight) {
            g.drawImage(animation.getImage(), (int) position.x, (int) position.y, null);
        } else {
            g.drawImage(animation.getImage(), (int) position.x + width, (int) position.y, -width, height, null);
        }
        // debugging
        if(debugging) showDebuggers(g);
    }

    /**
     * Use the helper functions to determine the map collisions.
     */
    private void checkMapCollision() {
        // Check for ground
        if (velocity.y >= 0 && hasGround(oldPosition, position)) {
            position.y = groundY - collisionBox.halfSize.y - collisionOffset.y - 1;
            velocity.y = 0;
            isOnGround = true;
        } else isOnGround = false;
        // Check for left tile
        if (velocity.x <= 0 && collidesWithLeftWall(oldPosition, position)) {
            if (oldPosition.x - collisionBox.halfSize.x + collisionOffset.x >= leftWallX) {
                position.x = leftWallX + collisionBox.halfSize.x - collisionOffset.x;
                isPushingLeftWall = true;
            }
            velocity.x = Math.min(velocity.x, 0);
        } else isPushingLeftWall = false;
        // Check for right tile
        if (velocity.x >= 0 && collidesWithRightWall(oldPosition, position)) {
            if (oldPosition.x + collisionBox.halfSize.x + collisionOffset.x <= rightWallX) {
                position.x = rightWallX - collisionBox.halfSize.x - collisionOffset.x;
                isPushingRightWall = true;
            }
            velocity.x = Math.max(velocity.x, 0);
        } else isPushingRightWall = false;
        // Check for ceiling
        if (velocity.y <= 0 && hasCeiling(oldPosition, position)) {
            position.y = ceilingY + collisionBox.halfSize.y + collisionOffset.y;
            velocity.y = 0;
            isAtCeiling = true;
        } else isAtCeiling = false;
    }

    /**
     * Check if the MovingObject is exactly above a ground tile.
     * @param oldPosition the old position of the MovingObject
     * @param position the new position of the MovingObject
     * @return true, if there is ground, false if not
     */
    private boolean hasGround(Vector2 oldPosition, Vector2 position) {
        Vector2 oldCenter = oldPosition.add(collisionOffset);
        Vector2 center = position.add(collisionOffset);
        Vector2 oldBotLeft = oldCenter.sub(collisionBox.halfSize).add(Vector2.DOWN).add(Vector2.RIGHT);
        oldBotLeft.y += collisionBox.halfSize.y * 2;
        Vector2 newBotLeft = center.sub(collisionBox.halfSize).add(Vector2.DOWN).add(Vector2.RIGHT);
        newBotLeft.y += collisionBox.halfSize.y * 2;
        Vector2 newBotRight = new Vector2(newBotLeft.x + collisionBox.halfSize.x * 2 - 2, newBotLeft.y);
        int endY = tileMap.getMapTileYAtPoint(newBotLeft.y);
        int startY = Math.min(tileMap.getMapTileYAtPoint(oldBotLeft.y), endY);
        int dist = Math.max(Math.abs(endY - startY), 1);
        if(framesPassedUntilDrop < 6) framesPassedUntilDrop++;
        // debugging stuff
        if (debugging) {
            triggerLineX = newBotLeft.x;
            triggerLineY = newBotLeft.y;
            drawRect = new Rectangle(tileMap.getMapTileXAtPoint(newBotLeft.x)*tileSize,
                    tileMap.getMapTileYAtPoint(newBotLeft.y)*tileSize, tileSize, tileSize);
            drawRect2 = new Rectangle(tileMap.getMapTileXAtPoint(newBotRight.x)*tileSize,
                    tileMap.getMapTileYAtPoint(newBotRight.y)*tileSize , tileSize, tileSize);
            drawImg = tileMap.printTile(tileMap.getMapTileXAtPoint(newBotLeft.x), tileMap.getMapTileYAtPoint(newBotLeft.y));
        }
        int tileIndexX;
        // First for loop for detecting collision at high speeds (takes in account the previous frame)
        for(int tileIndexY = startY; tileIndexY <= endY; tileIndexY++) {
            Vector2 botLeft = Vector2.lerp(newBotLeft, oldBotLeft, Math.abs(endY - tileIndexY) / dist);
            Vector2 botRight = new Vector2(botLeft.x + collisionBox.halfSize.x * 2 - 2, botLeft.y);
            // Second for loop for checking tiles being touched
            for (Vector2 checkedTile = botLeft; ;checkedTile.x += tileSize) {
                checkedTile.x = Math.min(checkedTile.x, botRight.x);
                tileIndexX = tileMap.getMapTileXAtPoint(checkedTile.x);
                tileIndexY = tileMap.getMapTileYAtPoint(checkedTile.y);
                groundY = tileIndexY * tileSize + tileMap.getY();
                if(tileIndexY * tileSize <= botLeft.y && botLeft.y <= tileIndexY * tileSize + mapCollisionSensorDepth) {
                    if (tileMap.isObstacle(tileIndexX, tileIndexY)) {
                        isOnPlatform = false;
                        return true;
                    } else if (tileMap.isPlatform(tileIndexX, tileIndexY) && framesPassedUntilDrop > framesToIgnoreGround) {
                        isOnPlatform = true;
                    }
                    if (checkedTile.x >= botRight.x) {
                        if (isOnPlatform) return true;
                        break;
                    }
                }
                if (checkedTile.x >= botRight.x) break;
            }
        }
        return false;
    }

    /**
     * Check if the MovingObject is exactly under a solid tile.
     * @param oldPosition the old position of the MovingObject
     * @param position the new position of the MovingObject
     * @return true, if there is a solid tile directly above, false if not
     */
    private boolean hasCeiling(Vector2 oldPosition, Vector2 position) {
        Vector2 oldCenter = oldPosition.add(collisionOffset);
        Vector2 center = position.add(collisionOffset);
        Vector2 oldTopLeft = oldCenter.sub(collisionBox.halfSize).add(Vector2.UP).add(Vector2.RIGHT).round();
        Vector2 newTopLeft = center.sub(collisionBox.halfSize).add(Vector2.UP).add(Vector2.RIGHT).round();
        int endY = tileMap.getMapTileYAtPoint(newTopLeft.y);
        int startY = Math.min(tileMap.getMapTileYAtPoint(oldTopLeft.y), endY);
        int dist = Math.max(Math.abs(endY - startY), 1);
        int tileIndexX;
        ceilingY = 0;
        for(int tileIndexY = startY; tileIndexY <= endY; tileIndexY++) {
            Vector2 topLeft = Vector2.lerp(newTopLeft, oldTopLeft, Math.abs(endY - tileIndexY) / dist);
            Vector2 topRight = new Vector2(topLeft.x + collisionBox.halfSize.x * 2 - 2, topLeft.y);
            for (Vector2 checkedTile = topLeft; ;checkedTile.x += tileSize) {
                checkedTile.x = Math.min(checkedTile.x, topRight.x);
                tileIndexX = tileMap.getMapTileXAtPoint(checkedTile.x);
                tileIndexY = tileMap.getMapTileYAtPoint(checkedTile.y);
                if(tileIndexY * tileSize + tileSize >= topLeft.y && topLeft.y <= tileIndexY * tileSize + tileSize - mapCollisionSensorDepth) {
                    if (tileMap.isObstacle(tileIndexX, tileIndexY)) {
                        ceilingY = tileIndexY * tileSize + tileMap.getY();
                        return true;
                    }
                }
                if (checkedTile.x >= topRight.x) break;
            }
        }
        return false;
    }

    /**
     * Check if the MovingObject is exactly to the right of a solid tile.
     * @param oldPosition the old position of the MovingObject
     * @param position the new position of the MovingObject
     * @return true, if there is a solid tile directly to the left, false if not
     */
    private boolean collidesWithLeftWall(Vector2 oldPosition, Vector2 position) {
        Vector2 oldCenter = oldPosition.add(collisionOffset);
        Vector2 oldBotLeft = oldCenter.sub(collisionBox.halfSize).add(Vector2.LEFT).round();
        oldBotLeft.y += collisionBox.halfSize.y * 2;
        Vector2 center = position.add(collisionOffset);
        Vector2 newBotLeft = center.sub(collisionBox.halfSize).add(Vector2.LEFT).round();
        newBotLeft.y += collisionBox.halfSize.y * 2;
        int endX = tileMap.getMapTileXAtPoint(newBotLeft.x);
        int startX = Math.max(tileMap.getMapTileXAtPoint(oldBotLeft.x), endX);
        int dist = Math.max(Math.abs(endX - startX), 1);
        int tileIndexY;
        leftWallX = 0;
        for(int tileIndexX = startX; tileIndexX >= endX; tileIndexX--) {
            Vector2 botLeft = Vector2.lerp(newBotLeft, oldBotLeft, Math.abs(endX - tileIndexX) / dist);
            Vector2 topLeft = new Vector2(botLeft.x, botLeft.y - collisionBox.halfSize.y * 2);
            for (Vector2 checkedTile = botLeft; ;checkedTile.y -= tileSize) {
                checkedTile.y = Math.max(checkedTile.y, topLeft.y);
                tileIndexY = tileMap.getMapTileYAtPoint(checkedTile.y);
                if(tileIndexX * tileSize + tileSize >= botLeft.x) {
                    if (tileMap.isObstacle(tileIndexX, tileIndexY)) {
                        leftWallX = tileIndexX * tileSize + tileSize + tileMap.getX();
                        return true;
                    }
                }
                if (checkedTile.y <= topLeft.y) break;
            }
        }
        return false;
    }

    /**
     * Check if the MovingObject is exactly to the left of a solid tile.
     * @param oldPosition the old position of the MovingObject
     * @param position the new position of the MovingObject
     * @return true, if there is a solid tile directly to the right, false if not
     */
    private boolean collidesWithRightWall(Vector2 oldPosition, Vector2 position) {
        Vector2 oldCenter = oldPosition.add(collisionOffset);
        Vector2 oldBotRight = oldCenter.add(collisionBox.halfSize).add(Vector2.RIGHT).round();
        Vector2 center = position.add(collisionOffset);
        Vector2 newBotRight = center.add(collisionBox.halfSize).add(Vector2.RIGHT).round();
        int endX = tileMap.getMapTileXAtPoint(newBotRight.x);
        int startX = Math.max(tileMap.getMapTileXAtPoint(oldBotRight.x), endX);
        int dist = Math.max(Math.abs(endX - startX), 1);
        int tileIndexY;
        rightWallX = 0;
        for(int tileIndexX = startX; tileIndexX <= endX; tileIndexX++) {
            Vector2 botRight = Vector2.lerp(newBotRight, oldBotRight, Math.abs(endX - tileIndexX) / dist);
            Vector2 topRight = new Vector2(botRight.x, botRight.y - collisionBox.halfSize.y * 2);
            for (Vector2 checkedTile = botRight; ;checkedTile.y -= tileSize) {
                checkedTile.y = Math.max(checkedTile.y, topRight.y);
                tileIndexY = tileMap.getMapTileYAtPoint(checkedTile.y);
                if(tileIndexX * tileSize <= botRight.x) {
                    if (tileMap.isObstacle(tileIndexX, tileIndexY)) {
                        rightWallX = tileIndexX * tileSize + tileMap.getX();
                        return true;
                    }
                }
                if (checkedTile.y <= topRight.y) break;
            }
        }
        return false;
    }

    /**
     * Draw the debugging lines and squares needed for the collision testing.
     * @param g the graphic context to be drawn on
     */
    private void showDebuggers(Graphics2D g) {
        // Collision Box
        g.setColor(Color.BLUE);
        int[] a = collisionBox.toXYWH();
        g.drawRect(a[0], a[1], a[2], a[3]);
        // Ground line
        g.setColor(Color.MAGENTA);
        g.drawLine(0, (int)groundY,500, (int)groundY);
        // Ground detection lines
        g.setColor(Color.YELLOW);
        g.drawLine(0, (int) triggerLineY,500, (int) triggerLineY);
        g.drawLine((int) triggerLineX, 0, (int) triggerLineX, 500);
        // Rectangles over the tiles that are being checked
        g.setColor(new Color(5, 5, 5, 150));
        g.fillRect(drawRect.x, drawRect.y, drawRect.width, drawRect.height);
        g.fillRect(drawRect2.x, drawRect2.y, drawRect2.width, drawRect2.height);
        // The image of the left bot tile shown in the top left corner
        g.drawImage(drawImg, 0, 0, null);
    }

    public Vector2 getVelocity() { return velocity; }

    public double getJumpSpeed() { return jumpSpeed; }

    public double getWalkSpeed() { return walkSpeed; }

    public double getGravity() { return gravity; }

    public double getMaxFallingSpeed() { return maxFallingSpeed; }

    public double getMinJumpingSpeed() { return minJumpingSpeed; }

    /**
     * Get the collider of the object.
     * @return the collider (collision box)
     */
    public CollisionBox getCollisionBox() {
        return collisionBox;
    }
}
