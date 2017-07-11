package GameState;

import Entity.*;
import Handlers.KeyHandler;
import Handlers.Keys;
import Main.Camera;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;
import TileMap.Vector2;
import TileMap.Vector2i;
import sun.java2d.pipe.DrawImage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Level1State extends GameState implements EntityObserver
{
    //Pause Title
    private final int PAUSE_TITLE_SIZE = 35;
    private final String PAUSE_TITLE_STYLE = "Serif";
    private final Color PAUSE_TITLE_COLOR = Color.ORANGE;
    //Option Titles
    private final int OPTIONS_SIZE = 15;
    private final String OPTIONS_STYLE = "Arial";
    private final Color OPTIONS_SELECTED_COLOR = Color.GREEN;
    private final Color OPTIONS_DEFAULT_COLOR = Color.LIGHT_GRAY;
    private Background bg1;
    private Background bg2;
    private Background bg3;
    private Background bg4;
    private TileMap tileMap;
    private Player player;
    private Camera camera;
    private String[] pauseOptions = {"RESUME", "BACK TO MENU", "EXIT"};
    private String[] gameOverOptions = {"RESTART", "BACK TO MENU", "EXIT"};
    private int[] optionsAlign = {22, -2, 35};
    private final int VGAP = -30;

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
        pauseTitle = new Font(PAUSE_TITLE_STYLE, Font.PLAIN, PAUSE_TITLE_SIZE);
        optionTitles = new Font(OPTIONS_STYLE, Font.PLAIN, OPTIONS_SIZE);
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
        createEnemy("EvilTwin", new Vector2(773, 2729),  "enemy_spritesheet_128_2.png");
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

            if (player.isHittenByEnemy())
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
            player.setHittenByEnemy(false);
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

        // draw tilemap
        //tileMap.draw(g);
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

        g.setColor(new Color(0, 153, 255));
        g.setFont(new Font("Arial", Font.PLAIN, 70));
        g.drawString("GAME OVER", GamePanel.WIDTH / 2 - 170,
                GamePanel.HEIGHT / 2 + lowerBorder);

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
                g.setColor(OPTIONS_SELECTED_COLOR);
            }
            else
            {
                g.setColor(OPTIONS_DEFAULT_COLOR);
            }

            g.drawString(selections[i],
                    GamePanel.WIDTH / 2 + VGAP + optionsAlign[i],
                    GamePanel.HEIGHT / 2 + 30 + i * 30);
        }
    }

    private void drawHUD(Graphics2D g)
    {
        g.drawImage(liveSymbol, 55, 10, null);

        Font HUDFont = new Font("Arial", Font.BOLD, 25);
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
            g.setColor(Color.RED);

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

        if (lives <= 0)
        {
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
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

        g.setFont(pauseTitle);
        g.setColor(PAUSE_TITLE_COLOR);
        g.drawString("PAUSE", GamePanel.WIDTH / 2 + VGAP, GamePanel.HEIGHT / 2);

        g.setFont(optionTitles);
        drawOptions(pauseOptions, g);
    }

    /**
     * whenever Enter is pressed this method enable / disable the pause mode
     */
    public void handleInput()
    {
        if (!gameOver && !pause && KeyHandler.hasJustBeenPressed(Keys.ENTER))
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