package GameState;

import Entity.Player;
import Entity.Enemy;
import Entity.EvilTwin;
import Main.GamePanel;
import TileMap.*;

import java.awt.*;

public class Level1State extends GameState {
	
	private TileMap tileMap;
	private Player player;
	private EvilTwin evilTwin;
	
	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}

	public void init() {
		tileMap = new TileMap(128);
		tileMap.loadTiles("/Sprites/terrain_spritesheet_128.png");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);

		player = new Player(tileMap);
		player.initPlayer(new Vector2(150, 100));
		
		evilTwin = new EvilTwin(tileMap);
		evilTwin.initEnemy(new Vector2(700, 100),"enemy_spritesheet_128.png");

		player.addCollisionCheck(evilTwin);
	}


	public void update() {
		player.update();
		evilTwin.update();
		//handleInput();
	}

	public void draw(Graphics2D g) {
		// clear screen
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

		player.draw(g);
		evilTwin.draw(g);
		// draw tilemap
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
		g.setComposite(ac);
		tileMap.draw(g);
	}

	public void handleInput() {
	}
	
}