package GameState;

import Entity.Player;
import Handlers.KeyHandler;
import Main.GamePanel;
import TileMap.*;

import java.awt.*;

public class Level1State extends GameState {
	
	private TileMap tileMap;
	private Player player;
	
	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	
	public void init() {
		tileMap = new TileMap(30);
        tileMap.loadTiles("/Tilesets/grasstileset.gif");
        tileMap.loadMap("/Maps/level1-1.map");
        tileMap.setPosition(0, 0);

        player = new Player(tileMap);
        player.setPosition(150, 100);
	}
	
	
	public void update() {
	    handleInput();
	    player.update();
    }
	
	public void draw(Graphics2D g) {
		
		// clear screen
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		// draw tilemap
		tileMap.draw(g);
		player.draw(g);
	}

	public void handleInput() {
        player.setVelocity((KeyHandler.keyState[KeyHandler.LEFT] ? -1 : 0)
                        + (KeyHandler.keyState[KeyHandler.RIGHT] ? 1 : 0),
                (KeyHandler.keyState[KeyHandler.UP] ? -1 : 0)
                + (KeyHandler.keyState[KeyHandler.DOWN] ? 1 : 0));
    }
	
}












