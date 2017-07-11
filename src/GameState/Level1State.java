package GameState;

import Entity.*;
import Handlers.KeyHandler;
import Handlers.Keys;
import Main.Camera;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;
import TileMap.Vector2;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Level1State extends GameState implements EntityObserver
{
    //Pause Title
    private final int PAUSE_TITLE_SIZE = 45;
    private final String PAUSE_TITLE_STYLE = "Berlin Sans FB Demi Bold.ttf";
    private final Color PAUSE_TITLE_COLOR = Color.WHITE;
    //Option Titles
    private final int OPTIONS_SIZE = 25;
    private final String OPTIONS_STYLE = "Berlin Sans FB Regular.ttf";
    private final Color OPTIONS_SELECTED_COLOR = Color.GREEN;
    private final Color OPTIONS_DEFAULT_COLOR = Color.LIGHT_GRAY;
    private Background bg1;
    private Background bg2;
    private Background bg3;
    private Background bg4;
    private TileMap tileMap;
    private Player player;
    private Camera camera;
    private String[] options = {"Resume", "Back to menu", "Quit"};
    private int[] optionsAlign = {22, -2, 35};
    private final int VGAP = -30;

    private boolean pause;
    private int currentChoice = 0;
    private Font pauseTitle;
    private Font optionTitles;

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
        initFonts();

        //some keys have changed through the settings
        if (KeyHandler.keysChanged())
        {
            initSound();
            initDifficulty();
        }
    }

    private void initFonts()
    {
        try
        {
            optionTitles = loadFont(OPTIONS_STYLE, OPTIONS_SIZE);
            pauseTitle = loadFont(PAUSE_TITLE_STYLE, PAUSE_TITLE_SIZE);
        } catch (FontFormatException | IOException e)
        {
            e.printStackTrace();
        }
    }

    private void initSound()
    {
        String sound = KeyHandler.getNewKeys()[0];

        switch (sound)
        {
            case "ON":
                enableSound();
                break;
            case "OFF":
                disableSound();
                break;
        }
    }

    private void enableSound()
    {
        // TODO ENABLE SOUND
    }

    private void disableSound()
    {
        // TODO DISABLE SOUND
    }

    private void initDifficulty()
    {
        String difficulty = KeyHandler.getNewKeys()[1];

        switch (difficulty)
        {
            case "EASY":
                setDifficutlyEasy();
                break;
            case "MEDIUM":
                setDifficultyMedium();
                break;
            case "HARD":
                setDifficultyHard();
                break;
        }
    }

    private void setDifficultyHard()
    {
        for (Enemy enemy : enemyList)
        {
            enemy.setWalkSpeed(5);
            //TODO
        }
    }

    private void setDifficultyMedium()
    {
        for (Enemy enemy : enemyList)
        {
            enemy.setWalkSpeed(3);
            //TODO
        }
    }

    private void setDifficutlyEasy()
    {
        for (Enemy enemy : enemyList)
        {
            enemy.setWalkSpeed(2);
            //TODO
        }
    }

    private void initBackground()
    {
        bg1 = new Background("bg1_3.png");
        bg1.setScrollSpeed(new Vector2(0.9, 0.9));
        bg1.setOffset(new Vector2(0, 205));
        bg2 = new Background("bg2_3.png");
        bg2.setScrollSpeed(new Vector2(0.7, 0.9));
        bg2.setOffset(new Vector2(0, 185));
        bg3 = new Background("bg3_3.png");
        bg3.setScrollSpeed(new Vector2(0.5, 0.8));
        bg3.setOffset(new Vector2(0, 35));
        bg4 = new Background("bg4_3.png");
        bg4.setScrollSpeed(new Vector2(0.3, 0.7));
        bg4.setOffset(new Vector2(0, -200));
    }

    private void initMap()
    {
        tileMap = new TileMap(128);
        tileMap.loadTiles("/Sprites/terrain_spritesheet_128_3.png");
        tileMap.loadMap("/Maps/duskmap.map");
        tileMap.setPosition(0, 0);
    }

    private void initPlayer()
    {
        player = new Player(tileMap);
        player.initPlayer(new Vector2(6000, 309));
    }

    private void initCamera()
    {
        camera = new Camera(tileMap, player);
    }

    private void initEnemies()
    {
        createEnemy("EvilTwin", new Vector2(4550, 809), "enemy_spritesheet_128_2.png");
        createEnemy("EvilTwin", new Vector2(3633, 1065), "enemy_spritesheet_128_2.png");
        createEnemy("EvilTwin", new Vector2(2315, 2345), "enemy_spritesheet_128_2.png");
        createEnemy("EvilTwin", new Vector2(773, 2729), "enemy_spritesheet_128_2.png");
        createEnemy("EvilTwin", new Vector2(1566, 2729), "enemy_spritesheet_128_2.png");
    }

    public void update()
    {
        if (!pause)
        {
            player.update();

            for (Enemy enemy : enemyList)
            {
                if (isOnCamera(enemy))
                {
                    enemy.update();
                }
            }

            camera.update();
            bg1.setPosition(tileMap.cameraPos);
            bg2.setPosition(tileMap.cameraPos);
            bg3.setPosition(tileMap.cameraPos);
            bg4.setPosition(tileMap.cameraPos);
        }

        handleInput();
    }

    public void draw(Graphics2D g)
    {
        // clear screen
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        bg4.draw(g);
        bg3.draw(g);
        bg2.draw(g);
        bg1.draw(g);

        player.draw(g);
        for (Enemy enemy : enemyList)
        {
            if (isOnCamera(enemy))
            {
                enemy.draw(g);
            }
        }

        // draw tilemap
        camera.draw(g);

        // draw hud
        player.drawHUD(g);

        if (pause)
        {
            drawPause(g);
        }
    }

    private boolean isOnCamera(MapObject o)
    {
        return (o.getPosition().x >= player.getPosition().x - GamePanel.WIDTH && o.getPosition().x <= player.getPosition().x + GamePanel.WIDTH) && (o.getPosition().y >= player.getPosition().y - GamePanel.HEIGHT && o.getPosition().y <= player.getPosition().y + GamePanel.HEIGHT);
    }

    private void drawPause(Graphics2D g)
    {
        //make screen opaque and darker
        g.setColor(new Color(0, 0, 0, 80));
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        g.setColor(Color.WHITE);
        drawCenteredString(g, "PAUSE", new Rectangle(0, GamePanel.HEIGHT / 2 - PAUSE_TITLE_SIZE, GamePanel.WIDTH, PAUSE_TITLE_SIZE), pauseTitle);

        for (int i = 0; i < options.length; i++)
        {
            if (i == currentChoice)
            {
                drawCenteredString(g, "- " + options[i] + " -", new Rectangle(0, GamePanel.HEIGHT / 2 + 30 + i * 30, GamePanel.WIDTH, OPTIONS_SIZE), optionTitles);
            } else
            {
                drawCenteredString(g, options[i], new Rectangle(0, GamePanel.HEIGHT / 2 + 30 + i * 30, GamePanel.WIDTH, OPTIONS_SIZE), optionTitles);
            }
        }
    }

    /**
     * Whenever Enter or Escape is pressed this method enable / disable the pause mode
     */
    public void handleInput()
    {
        if (!pause && KeyHandler.hasJustBeenPressed(Keys.ENTER) || KeyHandler.hasJustBeenPressed(Keys.ESCAPE))
        {
            pause = true;
        } else if (pause)
        {
            if (KeyHandler.hasJustBeenPressed(Keys.ENTER)) select();
            else if (KeyHandler.hasJustBeenPressed(Keys.UP))
            {
                currentChoice--;
                if (currentChoice == -1)
                {
                    currentChoice = options.length - 1;
                }
            }
            if (KeyHandler.hasJustBeenPressed(Keys.DOWN))
            {
                currentChoice++;
                if (currentChoice == options.length)
                {
                    currentChoice = 0;
                }
            }
        }
    }

    private void select()
    {
        switch (currentChoice)
        {
            case 0:
                pause = false;
                break;
            case 1:
                gsm.setState(GameStateManager.MENUSTATE);
                break;
            case 2:
                System.exit(0);
                break;
        }
    }

    /**
     * Create new enemies by calling this method.
     *
     * @param enemyName   Enemy name
     * @param position    The map position
     * @param spriteSheet THe spriteSheet
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