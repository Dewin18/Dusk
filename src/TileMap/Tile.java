package TileMap;

import java.awt.image.BufferedImage;

/** PTP 2017
 * Basic Tile including an image and a TileType enum.
 *
 * @author Ali Popa, Dewin Bagci
 * @version 13.08.
 * @since 31.05.
 */
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
