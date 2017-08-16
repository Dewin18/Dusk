package testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Entity.AnimationState;
import Entity.Player;
import TileMap.TileMap;
import Helpers.Vector2;

/** PTP 2017
 * Class for testing the planned perfectness of the awesome player.
 *
 * @author Dewin Bagci
 * @version 16.08.
 * @since 15.06.
 */
public class PlayerTest
{
    // Damage
    private final int dmg = 10;
    
    // Player specific speeds
    private final double gravity = 0.5;
    private final double walkspeed = 6;
    private final double jumpSpeed = -16;
    private final double minJumpSpeed = -2;
    private final double minFallSpeed = 3;
    private final double maxFallSpeed = 12;
    
    // Player specific health
    private final int health = 100;
    private final int lives = 3;
    
    private Player player;
    private TileMap tileMap;

    public PlayerTest()
    {
        init();
    }

    @Before
    // Create a new Player object for testing purposes
    public void init()
    {
        tileMap = new TileMap(128);
        player = new Player(tileMap);
    }

    @Test
    // Player constructor Test
    public void playerDefaultInitialisationTest()
    {
        assertTrue(player.isFacingRight());
       
        assertEquals(dmg, player.getDamage()); 
        assertEquals((int) gravity, (int) player.getGravity());
        assertEquals((int) walkspeed, (int) player.getWalkSpeed());
        assertEquals((int) jumpSpeed, (int) player.getJumpSpeed());
        assertEquals((int) minJumpSpeed, (int) player.getMinJumpSpeed());
        assertEquals((int) minFallSpeed, (int) player.getMinFallSpeed());
        assertEquals((int) maxFallSpeed, (int) player.getMaxFallSpeed());
        
        assertEquals(health, player.getHealth());
        assertEquals(lives, player.getLives());
    }
    
    @Test
    public void playerStateTest()
    {
        AnimationState currentState = player.getAnimationState();
        
        //Test the default player state at start
        assertEquals(currentState.toString(), "IDLE");
    }

    @Test
    public void playerPositionTest()
    {
        //create a vector new at (0.0) and check the players default position
        Vector2 defaultPosition = new Vector2(0, 0);
        assertEquals(defaultPosition.toString(), player.getPosition().toString());

        // change players position and check again with (0.0) vector
        player.setPosition(new Vector2(200, 100));
        assertFalse(defaultPosition.toString().equals(player.getPosition().toString()));
        
        // adapt the vector (0.0) -> (200,100)
        defaultPosition.x = 200;
        defaultPosition.y = 100;
        
        // compare vector with player position
        assertEquals(defaultPosition.toString(), player.getPosition().toString());
    }
}
