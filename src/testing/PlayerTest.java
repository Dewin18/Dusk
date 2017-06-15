package testing;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import Entity.CharacterState;
import Entity.Player;
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
        player.initPlayer(new Vector2(150, 100));
    }

    @Test
    public void playerInitalisationTest()
    {
        assertTrue(player.getJumpSpeed() == (-12.5));
        assertTrue(player.getWalkSpeed() == 6);
        assertTrue(player.getMinJumpingSpeed() == -1);
        assertTrue(player.getMaxFallingSpeed() == 4);
        assertTrue(player.getGravity() == 0.3);
        assertTrue(player.getMinFallSpeed() == 3);
    }
    
    @Test
    public void playerPositionTest()
    {
        //create a new player and place it at the vector position

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
    
    @Test
    public void playerStateTest()
    {
        CharacterState currentState = player.getCharacterState();
        //Test the default player state at start
        assertEquals(currentState.toString(), "IDLE");
        
    }

}
