package testing;

import Audio.JukeBox;
import Entity.AnimationState;
import Entity.Enemy;
import Entity.EvilTwin;
import Entity.Player;
import Handlers.KeyHandler;
import Helpers.Vector2;
import TileMap.TileMap;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnemyInteractionTest
{
    private TileMap tm;
    private Player player;
    private Enemy enemy;

    public EnemyInteractionTest()
    {
        init();
    }

    @Before
    public void init()
    {
        JukeBox.init();
        JukeBox.setMute(true);
        tm = new TileMap(128);
        tm.loadMap("/Maps/duskmap.map");
        tm.loadTiles("/Tilesets/terrain_spritesheet_128_3.png");
        tm.setPosition(0, 0);
        player = new Player(tm);
        enemy = new EvilTwin(tm);
        enemy.initEnemy(new Vector2(500, 0), "enemy_spritesheet_128_2.png");
        player.initPlayer(new Vector2(500, 0));
    }

    @Test
    public void PlayerHitTest()
    {
        // No collision check added to player
        assertTrue(player.getCollisionBox().overlaps(enemy.getCollisionBox()));
        enemy.update();
        player.update();
        assertFalse(player.isHitByEnemy());

        // Add collision check and reset positions
        player.addCollisionCheck(enemy);
        player.setPosition(500, 0);
        enemy.setPosition(500, 0);
        assertEquals(new Vector2(500, 0), player.getPosition());
        assertEquals(new Vector2(500, 0), enemy.getPosition());
        assertEquals(new Vector2(500, 0), player.getCollisionBox().center);
        assertEquals(new Vector2(500, 0), enemy.getCollisionBox().center);

        enemy.update();
        player.update();
        assertTrue(player.isHitByEnemy());
        assertTrue(player.isInvulnerable());
        assertEquals(AnimationState.FLINCHING, player.getAnimationState());
    }

    @Test
    public void EnemyHitTest()
    {
        player.setPosition(600, 100);
        enemy.setPosition(740, 100);
        assertFalse(player.getCollisionBox().overlaps(enemy.getCollisionBox()));
        player.addCollisionCheck(enemy);
        KeyHandler.keySet(65, true);
        enemy.update();
        player.update();
        assertTrue(player.isAttacking());
        assertTrue(player.getHitEnemy());
    }
}
