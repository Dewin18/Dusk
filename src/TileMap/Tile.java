package TileMap;

import java.awt.image.BufferedImage;

public class Tile {
	
	private BufferedImage image;
	private TileType type;
	//private int type;

	// tile types
	public static final int NORMAL = 0;
	public static final int BLOCKED = 1;

	public Tile(BufferedImage image, TileType type) {
		this.image = image;
		this.type = type;
	}
	
	public BufferedImage getImage() { return image; }
	public TileType getType() { return type; }
}
