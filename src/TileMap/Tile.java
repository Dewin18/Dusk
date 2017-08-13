package TileMap;

import java.awt.image.BufferedImage;

class Tile
{
    private BufferedImage image;
    private TileType type;

    Tile(BufferedImage image, TileType type)
    {
        this.image = image;
        this.type = type;
    }

    BufferedImage getImage()
    {
        return image;
    }

    TileType getType()
    {
        return type;
    }
}
