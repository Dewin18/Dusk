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

    private int lives;
    private float healthBar;
    private float healthInterval;
    private boolean gameOver;
    private int lowerBorder;
    private Image liveSymbol;
    private int upperBorder;
    private boolean liveLost;

    //health blink animation
    private final int BLINK_TIME = 50;
    private int blinkTimer = 0;
    private float smoothLimit = 1f;
    private final float SMOOTH_SCALE = 0.05f;

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
        initHUD();
        initCamera();
        initEnemies();
        initFonts();

        //HUD
        healthBar = 100;
        healthInterval = 80; // the player lose 20 health every hit

        //Game Over text border from center at Y-AXIS
        lowerBorder = 70;

        //dusk life symbol position at Y-AXIS
        upperBorder = 10;

        //some keys have changed through the settings
        if (KeyHandler.keysChanged()) //TODO
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
        try
        {
            liveSymbol = ImageIO.read(
                    getClass().getResourceAsStream("/Backgrounds/lsymbol.png"));

            liveSymbol = liveSymbol.getScaledInstance(50, 40,
                    Image.SCALE_SMOOTH);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
        lives = 0;

        for (Enemy enemy : enemyList)
        {
            enemy.setWalkSpeed(5);
            //TODO
        }
    }

    private void setDifficultyMedium()
    {
        lives = 1;

        for (Enemy enemy : enemyList)
        {
            enemy.setWalkSpeed(3);
            //TODO
        }
    }

    private void setDifficutlyEasy()
    {
        lives = 2;

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
        if (!pause && !gameOver)
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
                handleHealthBar();
            }
        }

        handleInput();
    }

    private void handleHealthBar()
    {
        healthBar -= 1;

        if (healthBar <= healthInterval)
        {
            player.setHitByEnemy(false);
            healthInterval -= 20;
        }

        if (healthBar == 0)
        {
            lives--;
            healthBar = 100; // fill HealthBar slowly
            healthInterval = 80;
        }

        if (lives < 0)
        {
            lives = 0;
            gameOver = true;
        }
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
        drawHUD(g);

        if (pause)
        {
            drawPause(g);
        }
        else if (gameOver)
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
                        GamePanel.HEIGHT / 2 + lowerBorder - PAUSE_TITLE_SIZE,
                        GamePanel.WIDTH, PAUSE_TITLE_SIZE),
                pauseTitle);

        if (lowerBorder > 0)
        {
            lowerBorder--;
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

    private void drawHUD(Graphics2D g)
    {
        g.drawImage(liveSymbol, 55, 10, null);

        Font HUDFont = optionTitles;
        g.setFont(HUDFont);
        //g.drawString("LIVES", 20, 30);

        g.drawString(String.valueOf(lives) + " x", 10, 40);

        g.setColor(Color.BLACK);
        g.drawRect(120, 20, 100, 20);

        if (healthBar >= 65)
            g.setColor(Color.GREEN);
        else if (healthBar >= 35)
            g.setColor(Color.YELLOW);
        else
        {
            g.setColor(Color.RED);
            if (!pause) blink(g);
        }

        g.fillRect(120, 20, (int) healthBar, 20);

        if (healthBar <= 1)
        {
            liveLost = true;
        }

        if (liveLost)
        {
            float op = 0.5f;
            g.setComposite(
                    AlphaComposite.getInstance(AlphaComposite.SRC_OVER, op));
            g.drawImage(liveSymbol, 55, upperBorder, null);
            drawLiveLostAnimation(g);
        }

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        g.setColor(Color.BLACK);
        for (int i = 140; i <= 200; i += 20) g.drawLine(i, 20, i, 40);
    }

    private void blink(Graphics2D g)
    {
        blinkTimer++;

        if (blinkTimer >= BLINK_TIME / 2)
        {
            g.setComposite(
                    AlphaComposite.getInstance(AlphaComposite.SRC_OVER, smoothLimit));
                
                if(smoothLimit > 0.1f) smoothLimit -= SMOOTH_SCALE;
        }
        
        if (blinkTimer == BLINK_TIME) 
        {
            smoothLimit = 1f;
            blinkTimer = 0;
        }
    }

    private void drawLiveLostAnimation(Graphics2D g)
    {
        upperBorder -= 1;

        if (upperBorder == -100)
        {
            upperBorder = 10;
            liveLost = false;
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
        if (!gameOver && !pause && KeyHandler.hasJustBeenPressed(Keys.ENTER)
                || KeyHandler.hasJustBeenPressed(Keys.ESCAPE))
        {
            pause = true;
        }
        else if (pause)
        {
            if (KeyHandler.hasJustBeenPressed(Keys.ENTER))
                selectPause();
            else
                selectChoice();
        }
        else if (gameOver)
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
            lives = 2;
            gsm.setState(GameStateManager.LEVEL1STATE);
            break;
        case 1:
            gsm.setState(GameStateManager.MENUSTATE);
            break;
        case 2:
            System.exit(0);
            break;
        }
        gameOver = false;
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