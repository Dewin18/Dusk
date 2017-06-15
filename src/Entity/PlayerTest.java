package Entity;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import TileMap.TileMap;
import TileMap.Vector2;

public class PlayerTest
{
    Player player;
    TileMap tileMap;

    public PlayerTest()
    {
        init();
    }

    @Before
    public void init()
    {
        tileMap = new TileMap(128);
        player = new Player(tileMap);
    }

    @Test
    public void playerPositionTest()
    {
        //create a new player and place it at the vector position
        player.initPlayer(new Vector2(150, 100));

        int VectorPositionX = (int) new Vector2(150, 100).x;
        int VectorPositionY = (int) new Vector2(150, 100).y;

        int PlayerPositionX = (int) player.getPosition().x;
        int PlayerPositionY = (int) player.getPosition().y;

        assertEquals(VectorPositionX, PlayerPositionX);
        assertEquals(VectorPositionY, PlayerPositionY);

        player.setPosition(new Vector2(200, 100));

        assertFalse(VectorPositionX != PlayerPositionX
                && VectorPositionY != PlayerPositionY);
    }

}
