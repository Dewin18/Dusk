package GameState;

import Entity.*;
import Main.Camera;
import TileMap.Background;
import TileMap.TileMap;
import TileMap.Vector2;

import java.awt.*;
import java.util.ArrayList;

public class Level1State extends GameState implements EntityObserver
{
    private Background bg1;
    private Background bg2;
    private Background bg3;
    private Background bg4;

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
        initBackground();
        initMap();
        initPlayer();
        initCamera();
        initEnemies();
    }

    private void initBackground()
    {
        bg1 = new Background("bg1_2.png");
        bg1.setScrollSpeed(new Vector2(0.9, 0.9));
        bg1.setOffset(new Vector2(0, -70));
        bg2 = new Background("bg2_2.png");
        bg2.setScrollSpeed(new Vector2(0.7, 0.9));
        bg2.setOffset(new Vector2(0, -130));
        bg3 = new Background("bg3_2.png");
        bg3.setScrollSpeed(new Vector2(0.5, 0.8));
        bg3.setOffset(new Vector2(0, -100));
        bg4 = new Background("bg4_2.png");
        bg4.setScrollSpeed(new Vector2(0.3, 0.7));
        bg4.setOffset(new Vector2(0, -110));
    }

    private void initMap()
    {
        tileMap = new TileMap(128);
        tileMap.loadTiles("/Sprites/terrain_spritesheet_128_2.png");
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
            createEnemy("EvilTwin", new Vector2(600, 100), "enemy_spritesheet_128.png");
            createEnemy("EvilTwin", new Vector2(700, 100), "enemy_spritesheet_128.png");
    }


    public void update()
    {
        player.update();

        for (Enemy enemy : enemyList)
        {
            enemy.update();
        }

        camera.update();
        bg1.setPosition(tileMap.cameraPos);
        bg2.setPosition(tileMap.cameraPos);
        bg3.setPosition(tileMap.cameraPos);
        bg4.setPosition(tileMap.cameraPos);
    }

    public void draw(Graphics2D g)
    {
        // clear screen
        //g.setColor(Color.WHITE);
        //g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        bg4.draw(g);
        bg3.draw(g);
        bg2.draw(g);
        bg1.draw(g);

        player.draw(g);
        for (Enemy enemy : enemyList)
        {
            enemy.draw(g);
        }

        // draw tilemap
        //tileMap.draw(g);
        camera.draw(g);
    }

    public void handleInput(){}

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
        enemy.addObserver(this);
        enemyList.add(enemy);
        player.addCollisionCheck(enemy);
    }

    @Override
    public void reactToChange(ObservableEntity o)
    {
        if (o instanceof Enemy)
        {
            enemyList.remove(o);
            player.addObjectToBeRemoved((MapObject) o);
        }
    }
}