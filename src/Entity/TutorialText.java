package Entity;

import Handlers.FontHandler;
import Handlers.KeyHandler;
import Helpers.Vector2;
import TileMap.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Locale;

public class TutorialText extends MapObject
{
    private Vector2 arrowPosition = new Vector2(5950, 570);
    private String arrowText = "> LEFT, RIGHT <";
    private Vector2 spacePosition = new Vector2(5305, 570);
    private String spaceText = "> SPACE <";
    private Vector2 attackPosition = new Vector2(4543, 700);
    private String attackText = "> A <";
    private Vector2 fallPosition = new Vector2(2873, 1550);
    private String fallText = "> DOWN + SPACE <";

    public TutorialText(TileMap tm)
    {
        super(tm);
        initKeys();
    }

    private void initKeys()
    {
        arrowText = "> " + KeyEvent.getKeyText(KeyHandler.getKeyCodes()[2]) + ", " + KeyEvent.getKeyText(KeyHandler.getKeyCodes()[3]) + " <";
        spaceText = "> " + KeyEvent.getKeyText(KeyHandler.getKeyCodes()[4]) + " <";
        attackText = "> " + KeyEvent.getKeyText(KeyHandler.getKeyCodes()[5]) + " <";
        fallText = "> " + KeyEvent.getKeyText(KeyHandler.getKeyCodes()[1]) + " + " + KeyEvent.getKeyText(KeyHandler.getKeyCodes()[4]) + " <";
    }

    @Override
    public void draw(Graphics2D g)
    {
        g.setColor(Color.WHITE);
        FontHandler.drawCenteredString(g, arrowText, new Rectangle((int)arrowPosition.x + (int)tileMap.cameraPos.x, (int)arrowPosition.y + (int)tileMap.cameraPos.y, tileSize, tileSize), FontHandler.getHudFont());
        FontHandler.drawCenteredString(g, spaceText, new Rectangle((int)spacePosition.x + (int)tileMap.cameraPos.x, (int)spacePosition.y + (int)tileMap.cameraPos.y, tileSize, tileSize), FontHandler.getHudFont());
        FontHandler.drawCenteredString(g, attackText, new Rectangle((int)attackPosition.x + (int)tileMap.cameraPos.x, (int)attackPosition.y + (int)tileMap.cameraPos.y, tileSize, tileSize), FontHandler.getHudFont());
        FontHandler.drawCenteredString(g, fallText, new Rectangle((int)fallPosition.x + (int)tileMap.cameraPos.x, (int)fallPosition.y + (int)tileMap.cameraPos.y, tileSize, tileSize), FontHandler.getHudFont());
    }


}
