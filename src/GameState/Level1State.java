package GameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import Entity.Enemy;
import Entity.EvilTwin;
import Entity.Player;
import Main.Camera;
import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Vector2;

public class Level1State extends GameState
{

    private TileMap tileMap;
    private Player player;
    private Camera camera;

    //All Level1State enemies are stored in this list
    private ArrayList<Enemy> enemyList;

    public Level1State(GameStateManager gsm)
    {
        enemyList = new ArrayList<>();
        this.gsm = gsm;
        init();
    }

    public void init()
    {
        initMap();
        initPlayer();
        initCamera();
        initEnemies();
    }

    private void initMap()
    {
        tileMap = new TileMap(128);
        tileMap.loadTiles("/Sprites/terrain_spritesheet_128.png");
        tileMap.loadMap("/Maps/level1-1.map");
        tileMap.setPosition(0, 0);
    }

    private void initPlayer()
    {
        player = new Player(tileMap);
        player.initPlayer(new Vector2(150, 100));
    }

    private void initCamera()
    {
        camera = new Camera(tileMap, player);
    }

    private void initEnemies()
    {
        createEnemy("EvilTwin", new Vector2(700, 100), "enemy_spritesheet_128.png");
        createEnemy("EvilTwin", new Vector2(600, 100), "enemy_spritesheet_128.png");
    }


    public void update()
    {
        player.update();

        for (Enemy enemy : enemyList)
        {
            enemy.update();
        }

        camera.update();
    }

    public void draw(Graphics2D g)
    {
        // clear screen
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        player.draw(g);
        for (Enemy enemy : enemyList)
        {
            enemy.draw(g);
        }

        // draw tilemap
        //tileMap.draw(g);
        camera.draw(g);
    }

    public void handleInput()
    {
    }

    /**
     * Create new enemies by calling this method.
     *
     * @param enemyName   The enemy name as String
     * @param position    The map position as Vector2
     * @param spriteSheet THe spriteSheet as String
     */
    public void createEnemy(String enemyName, Vector2 position, String spriteSheet)
    {
        Enemy enemy = null;

        switch (enemyName)
        {
            case "EvilTwin":
                enemy = new EvilTwin(tileMap);
                break;
        }

        enemy.initEnemy(position, spriteSheet);
        enemyList.add(enemy);
        player.addCollisionCheck(enemy);
    }
}