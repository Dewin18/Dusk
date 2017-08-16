package testing;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import Entity.EvilTwin;
import Entity.Player;
import GameState.GameStateManager;
import GameState.Level1State;
import Helpers.Vector2;

public class Level1StateTest
{
    private GameStateManager gsm;
    private Level1State level1State;
    private Player player;
    private EvilTwin evilTwin;
    
    private final int DEFAULT_ENEMY_AMOUNT = 5;
    
    public Level1StateTest()
    {
        super();
        init();
    }

    @Before
    public void init()
    {
        gsm = new GameStateManager();
        gsm.setState(GameStateManager.LEVEL1STATE);
        
        level1State = (Level1State) gsm.getState(GameStateManager.LEVEL1STATE);
        evilTwin = (EvilTwin) level1State.getEnemyList().get(0);
        player = level1State.getPlayer();
    }
    
    @Test
    public void enemyListTest()
    {
        assertTrue(level1State.getEnemyList().size() == DEFAULT_ENEMY_AMOUNT);
        
        level1State.createEnemy("EvilTwin", new Vector2(4540, 809), "enemy_spritesheet_128_2.png");
        level1State.createEnemy("EvilTwin", new Vector2(4560, 809), "enemy_spritesheet_128_2.png");
        level1State.createEnemy("EvilTwin", new Vector2(4580, 809), "enemy_spritesheet_128_2.png");
        
        assertTrue(level1State.getEnemyList().size() == DEFAULT_ENEMY_AMOUNT + 3);
    }
    
    @Test
    // new difficulty changes Players health and enemies walkspeed
    public void setDifficultyTest()
    {
        testDefault();
        testEasy();
        testMedium();
        testHard();
    }

    private void testDefault()
    {
        assertTrue(player.getLives() == 3);
        assertTrue((int) evilTwin.getWalkSpeed() == 2);
    }
    
    private void testEasy()
    {
        level1State.setDifficultyEasy();
        assertTrue(player.getLives() == 3);
        assertTrue((int) evilTwin.getWalkSpeed() == 2);
    }

    private void testMedium()
    {
        level1State.setDifficultyMedium();
        assertTrue(player.getLives() == 2);
        assertTrue((int) evilTwin.getWalkSpeed() == 3);
    }
    
    private void testHard()
    {
        level1State.setDifficultyHard();
        assertTrue(player.getLives() == 0);
        assertTrue((int) evilTwin.getWalkSpeed() == 5);
    }
}
