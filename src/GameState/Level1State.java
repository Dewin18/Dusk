package GameState;

import java.awt.AlphaComposite;
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

public class Level1State extends GameState {
	
	private TileMap tileMap;
	private Player player;
	private Camera camera;
	
	//All Level1State enemies are stored in this list
	private ArrayList<Enemy> enemyList;
	
	public Level1State(GameStateManager gsm) {
	    enemyList = new ArrayList<>();
	    this.gsm = gsm;
		init();
	}

	public void init() {
		tileMap = new TileMap(128);
		tileMap.loadTiles("/Sprites/terrain_spritesheet_128.png");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);


		player = new Player(tileMap);
		camera = new Camera(tileMap, player);
		player.initPlayer(new Vector2(150, 100));
		
		//With dynamic binding we can assign the Enemy reference to a concrete Enemy e.g. EvilTwin
		Enemy evilTwin1 = new EvilTwin(tileMap);
		evilTwin1.initEnemy(new Vector2(700, 100),"enemy_spritesheet_128.png");
		
		Enemy evilTwin2 = new EvilTwin(tileMap);
        evilTwin2.initEnemy(new Vector2(600, 100),"enemy_spritesheet_128.png");
		
		enemyList.add(evilTwin1);
		enemyList.add(evilTwin2);

		player.addCollisionCheck(evilTwin1);
	    player.addCollisionCheck(evilTwin2);
	}


	public void update() {
		player.update();

		for (Enemy enemy : enemyList) {
            enemy.update();
        }
		
		camera.update();
	}

	public void draw(Graphics2D g) {
		// clear screen
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

		player.draw(g);
		for (Enemy enemy : enemyList) {
            enemy.draw(g);
        }
		
		// draw tilemap
		//tileMap.draw(g);
		camera.draw(g);
	}

	public void handleInput() {
	}

}