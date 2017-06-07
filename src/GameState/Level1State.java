package GameState;

import Entity.Player;
import Handlers.KeyHandler;
import Handlers.Keys;
import Main.GamePanel;
import TileMap.*;

import javax.sound.midi.Soundbank;
import java.awt.*;
import java.util.Arrays;

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
        player.initPlayer(new Vector2(150, 100));
	}
	
	
	public void update() {
		player.update();
		//handleInput();
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
	}
	
}












