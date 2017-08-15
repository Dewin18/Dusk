package GameState;

import Audio.JukeBox;
import Entity.*;
import Handlers.AnimationHandler;
import Handlers.ChoiceHandler;
import Handlers.FontHandler;
import Handlers.KeyHandler;
import Handlers.Keys;
import Main.Camera;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;
import Helpers.Vector2;

import java.awt.*;
import java.util.ArrayList;

public class Level1State extends GameState implements EntityObserver
{
    //Pause Title
    private final Color PAUSE_TITLE_COLOR = Color.WHITE;

    private Background bg1;
    private Background bg2;
    private Background bg3;
    private Background bg4;
    private TileMap tileMap;
    private Player player;
    private Camera camera;
    private String[] pauseOptions = {"Resume", "Back to menu", "Exit"};
    private String[] gameOverOptions = {"Restart", "Back to menu", "Exit"};
    
    private boolean pause = false;

    private EndingTrigger endingTrigger;

    //All Level1State enemies are stored in this list
    private ArrayList<Enemy> enemyList;
    private boolean gameFinished = false;

    public Level1State(GameStateManager gsm)
    {
        this.gsm = gsm;
        init();
    }

    public void init()
    {
        enemyList = new ArrayList<>();
        ChoiceHandler.setChoice(0);
        
        JukeBox.close("titlemusic");
        initBackground();
        initMap();
        initPlayer();
        initCamera();
        initEnemies();
        initEnding();

        //some keys have changed through the settings
        if (KeyHandler.keysChanged())
        {
            initSound();
            initDifficulty();
        }
        else
        {
            setDifficultyEasy();
            enableSound();
        }
        player.update();
        camera.updateNoDeltaTime();

        bg1.setPosition(tileMap.cameraPos);
        bg2.setPosition(tileMap.cameraPos);
        bg3.setPosition(tileMap.cameraPos);
        bg4.setPosition(tileMap.cameraPos);
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
        JukeBox.setMute(true);
    }

    private void initDifficulty()
    {
        String difficulty = KeyHandler.getNewKeys()[1];

        switch (difficulty)
        {
            case "EASY":
                setDifficultyEasy();
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
        player.setLives(0);

        for (Enemy enemy : enemyList)
        {
            enemy.setWalkSpeed(5);
        }
    }

    private void setDifficultyMedium()
    {
        player.setLives(2);

        for (Enemy enemy : enemyList)
        {
            enemy.setWalkSpeed(3);
        }
    }

    private void setDifficultyEasy()
    {
        player.setLives(3);

        for (Enemy enemy : enemyList)
        {
            enemy.setWalkSpeed(2);
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
        tileMap.loadMap("/Maps/duskmap.map");
        tileMap.loadTiles("/Tilesets/terrain_spritesheet_128_3.png");
        tileMap.setPosition(0, 0);
    }

    private void initPlayer()
    {
        player = new Player(tileMap);
        player.initPlayer(new Vector2(6000, 681));
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

    private void initEnding()
    {
        endingTrigger = new EndingTrigger(tileMap);
        player.addCollisionCheck(endingTrigger);
    }

    public void update()
    {
        if (!pause ^ player.isGameOver())
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
        endingTrigger.draw(g);
        camera.draw(g);
        player.draw(g);

        for (Enemy enemy : enemyList)
        {
            if (isOnCamera(enemy))
            {
                enemy.draw(g);
            }
        }

        if (pause)
        {
            drawPause(g);
        }
        else if (player.isGameOver())
        {
            if (gameFinished)
            {
                drawEndingOverlay(g, "FIN");
            }
            else
            {
                drawEndingOverlay(g, "GAME OVER");
            }
        }
    }

    private void drawEndingOverlay(Graphics2D g, String message)
    {
        AnimationHandler.drawEndingText(g, message);

        if (AnimationHandler.getEndingTextPosition() > 0)
        {
            AnimationHandler.decreaseEndingTextPosition();
        }
        else
        {
            g.setFont(FontHandler.getEndingSelectionFont());
            drawOptions(gameOverOptions, g);
        }
    }

    public void drawOptions(String[] selections, Graphics2D g)
    {
        for (int i = 0; i < selections.length; i++)
        {
            if (i == ChoiceHandler.getChoice())
            {
                FontHandler.drawCenteredString(g, "- " + selections[i] + " -",
                        new Rectangle(0,
                                GamePanel.HEIGHT / 2 + FontHandler.PAUSE_SELECTION_SIZE
                                        + i * FontHandler.PAUSE_SELECTION_SIZE,
                                GamePanel.WIDTH,FontHandler.PAUSE_SELECTION_SIZE),
                        FontHandler.getPauseSelectionFont());
            }
            else
            {
                FontHandler.drawCenteredString(g, selections[i],
                        new Rectangle(0,
                                GamePanel.HEIGHT / 2 + FontHandler.PAUSE_SELECTION_SIZE
                                        + i * FontHandler.PAUSE_SELECTION_SIZE,
                                GamePanel.WIDTH, FontHandler.PAUSE_SELECTION_SIZE),
                        FontHandler.getPauseSelectionFont());
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
        FontHandler.drawCenteredString(g, "PAUSE",
                new Rectangle(0, GamePanel.HEIGHT / 2 - FontHandler.PAUSE_TITLE_SIZE,
                        GamePanel.WIDTH, FontHandler.PAUSE_TITLE_SIZE),
                FontHandler.getPauseTitleFont());
        drawOptions(pauseOptions, g);
    }

    /**
     * Whenever Enter or Escape is pressed this method enable / disable the pause mode
     */
    public void handleInput()
    {
        if (!player.isGameOver() && !pause && KeyHandler.hasJustBeenPressed(Keys.ENTER)
                || KeyHandler.hasJustBeenPressed(Keys.ESCAPE))
        {
            pause = true;
        }
        else if (pause)
        {
            if (KeyHandler.hasJustBeenPressed(Keys.ENTER))
            {
                selectPause();
            }
            else
            {
                selectChoice();
            }
        }
        else if (player.isGameOver())
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
            ChoiceHandler.selectNextUp(pauseOptions.length);
        }
        else if (KeyHandler.hasJustBeenPressed(Keys.DOWN))
        {
            ChoiceHandler.selectNextDown(pauseOptions.length);
        }
    }

    private void selectGameOver()
    {
        switch (ChoiceHandler.getChoice())
        {
        case 0:
            gsm.setState(GameStateManager.LEVEL1STATE);
            AnimationHandler.resetGameOverTextPosition();
            break;
        case 1:
            gsm.setState(GameStateManager.MENUSTATE);
            break;
        case 2:
            System.exit(0);
            break;
        }
    }

    private void selectPause()
    {
        switch (ChoiceHandler.getChoice())
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
     * @param spriteSheet The spriteSheet
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

        assert enemy != null;
        enemy.initEnemy(position, spriteSheet);
        enemy.addObserver(this);
        player.addObserver(this);
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
        if (o instanceof EndingTrigger)
        {
            gameFinished = true;
            player.setGameOver(true);
        }
    }
    
    public String toString()
    {
        return "Level1State";
    }
    
    public ArrayList<Enemy> getEnemyList()
    {
        return enemyList;
    }
}