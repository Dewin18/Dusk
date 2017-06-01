package Entity;

import TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends MapObject {

    private int health;
    private int exp;
    private int lives;
    private int dmg;

    private boolean hasAttack = true;
    private boolean hasDoubleJump = false;
    private boolean hasDash = false;

    private long time = 0;

    public Player(TileMap tm) {
        super(tm);

        // load sprites
        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream(
                    "/Sprites/Player/PlayerSprites.gif"));
            sprite = spritesheet.getSubimage(0, 0, 30, 30);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void update() {
        time ++;

        position[0] += velocity[0];
        position[1] += velocity[1];
    }
}
