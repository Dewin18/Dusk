package TileMap;

import java.awt.*;
import java.awt.image.*;

import java.io.*;
import java.util.Arrays;
import javax.imageio.ImageIO;

import Main.GamePanel;

public class TileMap {
	
	// position
	private Vector2 position = new Vector2(0,0);
	
	// bounds
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	
	private double tween;
	
	// map
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;
	
	// tileset
	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile[][] tiles;
	
	// drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		tween = 0.07;
	}
	
	public void loadTiles(String s) {
		try {
			tileset = ImageIO.read(
				getClass().getResourceAsStream(s)
			);
			numTilesAcross = tileset.getWidth() / tileSize;
			tiles = new Tile[2][numTilesAcross];
			
			BufferedImage subimage;
			for(int col = 0; col < numTilesAcross; col++) {
				subimage = tileset.getSubimage(
							col * tileSize,
							0,
							tileSize,
							tileSize
						);
				tiles[0][col] = new Tile(subimage, TileType.EMPTY);
				subimage = tileset.getSubimage(
							col * tileSize,
							tileSize,
							tileSize,
							tileSize
						);
				tiles[1][col] = new Tile(subimage, TileType.BLOCKED);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap(String s) {
		try {
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(
						new InputStreamReader(in)
					);
			
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			
			String delims = "\\s+";
			for(int row = 0; row < numRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for(int col = 0; col < numCols; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getTileSize() { return tileSize; }
	public int getX() { return (int)position.x; }
	public int getY() { return (int)position.y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }

	public BufferedImage printTile(int col, int row) {
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		BufferedImage derp = tiles[r][c].getImage();
		return derp;
	}
	
	public TileType getType(int row, int col) {
		if (row < 0 || row >= numRows|| col < 0 || col >= numCols)
			return TileType.BLOCKED;
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}

	public TileType getType(Vector2i tileCoords) {
		int rc = map[tileCoords.x][tileCoords.y];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}

	public boolean isObstacle(int x, int y) {
		return getType(y, x) == TileType.BLOCKED;
	}

	public boolean isGround(int x, int y) {
		return (getType(y, x) == TileType.BLOCKED || getType(y, x) == TileType.ONEWAY);
	}

	public boolean isEmpty(int x, int y) {
		return getType(y, x) == TileType.EMPTY;
	}

	public Vector2i getMapTileAtPoint(Vector2 point) {
		return new Vector2i((int) (point.x - position.x + tileSize / 2), (int) (point.y - position.y + tileSize / 2));
	}

	public int getMapTileXAtPoint(double x) {
		return (int)Math.round((x - position.x - tileSize / 2) / tileSize);
	}

	public int getMapTileYAtPoint(double y) {
		return (int)Math.round((y - position.y - tileSize / 2) / tileSize);
	}

	public Vector2 getMapTileCoords(int row, int col) {
		return new Vector2(row * tileSize + position.x, col * tileSize + position.y);
	}

	public Vector2 getMapTileCoords(Vector2i tileCoords) {
		return new Vector2(tileCoords.x * tileSize + position.x, tileCoords.y * tileSize + position.y);
	}
	
	public void setPosition(double x, double y) {
		this.position.x += (x - this.position.x) * tween;
		this.position.y += (y - this.position.y) * tween;
		
		fixBounds();
		
		colOffset = (int)-this.position.x / tileSize;
		rowOffset = (int)-this.position.y / tileSize;
		
	}
	
	private void fixBounds() {
		if(position.x < xmin) position.x = xmin;
		if(position.y < ymin) position.y = ymin;
		if(position.x > xmax) position.x = xmax;
		if(position.y > ymax) position.y = ymax;
	}
	
	public void draw(Graphics2D g) {
		for(
			int row = rowOffset;
			row < rowOffset + numRowsToDraw;
			row++) {
			
			if(row >= numRows) break;
			
			for(
				int col = colOffset;
				col < colOffset + numColsToDraw;
				col++) {
				
				if(col >= numCols) break;
				
				if(map[row][col] == 0) continue;
				
				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;
				
				g.drawImage(
					tiles[r][c].getImage(),
					(int)position.x + col * tileSize,
					(int)position.y + row * tileSize,
					null
				);
			}
		}
	}
}



















