package levels;

import Main.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Main.Game.TILES_SIZE;

public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private Level levelOne;

    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprites();
        levelOne = new Level(LoadSave.GetLevelData());
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[7];
        for (int i = 0; i<7; i++){
            int index = i;
            levelSprite[index] = img.getSubimage(i*24,0,24,24);
        }
    }

    public void draw(Graphics g) {
        for (int j = 0; j<Game.TILES_IN_HEIGHT;j++){
            for (int i = 0; i<Game.TILES_IN_WIDTH; i++){
                int index = levelOne.getSpriteIndex(i, j);
                g.drawImage(levelSprite[index],TILES_SIZE * i, TILES_SIZE*j,TILES_SIZE,TILES_SIZE,null);
            }
        }
    }

    public void update() {

    }

    public Level getCurrentLevel() {
        return levelOne;
    }
}