package GameState;

import Audio.JukeBox;
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

import javax.imageio.ImageIO;

public class Level1State extends GameState implements EntityObserver
{
    //Pause Title
    private final int PAUSE_TITLE_SIZE = 45;
    private final String PAUSE_TITLE_STYLE = "Berlin Sans FB Demi Bold.ttf";
    private final Color PAUSE_TITLE_COLOR = Color.WHITE;
    //Option Titles
    private final int OPTIONS_SIZE = 25;
    private final String OPTIONS_STYLE = "Berlin Sans FB Regular.ttf";
    private Background bg1;
    private Background bg2;
    private Background bg3;
    private Background bg4;
    private TileMap tileMap;
    private Player player;
    private Camera camera;
    private String[] pauseOptions = {"Resume", "Back to menu", "Exit"};
    private String[] gameOverOptions = {"Restart", "Back to menu", "Exit"};

    private boolean pause;
    private int currentChoice = 0;
    private Font pauseTitle;
    private Font optionTitles;

    //All Level1State enemies are stored in this list
    private ArrayList<Enemy> enemyList;

    private HUD hud;
    private int gameOverTextPosition;

    public Level1State(GameStateManager gsm)
    {
        enemyList = new ArrayList<>();
        this.gsm = gsm;
        init();
    }

    public void init()
    {
        JukeBox.close("titlemusic");
        initBackground();
        initMap();
        initPlayer();
        initFonts();
        initHUD();
        initCamera();
        initEnemies();
      
        //Game Over text border from center at Y-AXIS
        gameOverTextPosition = 70;

        //some keys have changed through the settings
        if (KeyHandler.keysChanged())
        {
            initSound();
            initDifficulty();
        }
        else
        {
            setDifficutlyEasy();
            enableSound();
        }
    }

    private void initHUD()
    {
       hud = new HUD(player, optionTitles);
    }

    private void initFonts()
    {
        try
        {
            optionTitles = loadFont(OPTIONS_STYLE, OPTIONS_SIZE);
            pauseTitle = loadFont(PAUSE_TITLE_STYLE, PAUSE_TITLE_SIZE);
        }
        catch (FontFormatException | IOException e)
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
        JukeBox.load("main_music.mp3", "mainmusic");
        JukeBox.load("forest_atmo.mp3", "forestatmo");
        JukeBox.loop("mainmusic");
        JukeBox.loop("forestatmo");
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
        hud.setLives(0);

        for (Enemy enemy : enemyList)
        {
            enemy.setWalkSpeed(5);
            //TODO
        }
    }

    private void setDifficultyMedium()
    {
        hud.setLives(2);

        for (Enemy enemy : enemyList)
        {
            enemy.setWalkSpeed(3);
            //TODO
        }
    }

    private void setDifficutlyEasy()
    {
        hud.setLives(3);

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
        if (!pause && !hud.isGameOver())
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

            if (player.isHitByEnemy())
            {
                hud.handleHealthBar();
            }
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

        camera.draw(g);
        hud.draw(g);
        
        if (pause)
        {
            // TODO stop Blink
            drawPause(g);
        }
        else if (hud.isGameOver())
        {
            drawGameOver(g);
        }
    }

    private void drawGameOver(Graphics2D g)
    {
        g.setColor(new Color(0, 0, 0, 80));
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        g.setColor(Color.WHITE);
        drawCenteredString(g, "GAME OVER",
                new Rectangle(0,
                        GamePanel.HEIGHT / 2 + gameOverTextPosition - PAUSE_TITLE_SIZE,
                        GamePanel.WIDTH, PAUSE_TITLE_SIZE),
                pauseTitle);

        if (gameOverTextPosition > 0)
        {
            gameOverTextPosition--;
        }
        else
        {
            g.setFont(optionTitles);
            drawOptions(gameOverOptions, g);
        }
    }

    public void drawOptions(String[] selections, Graphics2D g)
    {
        for (int i = 0; i < selections.length; i++)
        {
            if (i == currentChoice)
            {
                drawCenteredString(g, "- " + selections[i] + " -",
                        new Rectangle(0,
                                GamePanel.HEIGHT / 2 + OPTIONS_SIZE
                                        + i * OPTIONS_SIZE,
                                GamePanel.WIDTH, OPTIONS_SIZE),
                        optionTitles);
            }
            else
            {
                drawCenteredString(g, selections[i],
                        new Rectangle(0,
                                GamePanel.HEIGHT / 2 + OPTIONS_SIZE
                                        + i * OPTIONS_SIZE,
                                GamePanel.WIDTH, OPTIONS_SIZE),
                        optionTitles);
            }

            //g.drawString(selections[i], GamePanel.WIDTH / 2 + VGAP + optionsAlign[i], GamePanel.HEIGHT / 2 + 30 + i * 30);
        }
    }

   

    private boolean isOnCamera(MapObject o)
    {
        return (o.getPosition().x >= player.getPosition().x - GamePanel.WIDTH
                && o.getPosition().x <= player.getPosition().x
                        + GamePanel.WIDTH)
                && (o.getPosition().y >= player.getPosition().y
                        - GamePanel.HEIGHT
                        && o.getPosition().y <= player.getPosition().y
                                + GamePanel.HEIGHT);
    }

    private void drawPause(Graphics2D g)
    {
        //make screen opaque and darker
        g.setColor(new Color(0, 0, 0, 80));
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        g.setColor(PAUSE_TITLE_COLOR);
        drawCenteredString(g, "PAUSE",
                new Rectangle(0, GamePanel.HEIGHT / 2 - PAUSE_TITLE_SIZE,
                        GamePanel.WIDTH, PAUSE_TITLE_SIZE),
                pauseTitle);
        drawOptions(pauseOptions, g);
    }

    /**
     * Whenever Enter or Escape is pressed this method enable / disable the pause mode
     */
    public void handleInput()
    {
        if (!hud.isGameOver() && !pause && KeyHandler.hasJustBeenPressed(Keys.ENTER)
                || KeyHandler.hasJustBeenPressed(Keys.ESCAPE))
        {
            pause = true;
            hud.setHealthBlinking(true);
        }
        else if (pause)
        {
            if (KeyHandler.hasJustBeenPressed(Keys.ENTER))
            {
                selectPause();
                hud.setHealthBlinking(false);
            }
            else
                selectChoice();
        }
        else if (hud.isGameOver())
        {
            if (KeyHandler.hasJustBeenPressed(Keys.ENTER))
                selectGameOver();
            else
                selectChoice();
        }
    }

    private void selectChoice()
    {
        if (KeyHandler.hasJustBeenPressed(Keys.UP))
        {
            currentChoice--;
            if (currentChoice == -1)
            {
                currentChoice = pauseOptions.length - 1;
            }
        }
        else if (KeyHandler.hasJustBeenPressed(Keys.DOWN))
        {
            currentChoice++;
            if (currentChoice == pauseOptions.length)
            {
                currentChoice = 0;
            }
        }
    }

    private void selectGameOver()
    {
        switch (currentChoice)
        {
        case 0:
            hud.setLives(2); 
            gsm.setState(GameStateManager.LEVEL1STATE);
            break;
        case 1:
            gsm.setState(GameStateManager.MENUSTATE);
            break;
        case 2:
            System.exit(0);
            break;
        }
        hud.setGameOver(false);
    }

    private void selectPause()
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
    public void createEnemy(String enemyName, Vector2 position,
            String spriteSheet)
    {
        Enemy enemy = null;

        switch (enemyName)
        {
        case "EvilTwin":
            enemy = new EvilTwin(tileMap);
            break;
        }

        assert enemy != null;
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