package levels;

import entities.Boy;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Main.Game.TILES_IN_WIDTH;
import static Main.Game.TILES_SIZE;
import static utilz.HelpMethods.GetBoys;
import static utilz.HelpMethods.GetLevelData;

public class Level {
    private final BufferedImage img;
    private int[][] lvlData;
    private ArrayList<Boy> boys;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;


    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        calcLvlOffsets();
    }

    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - TILES_IN_WIDTH;
        maxLvlOffsetX = TILES_SIZE * maxTilesOffset;

    }

    private void createEnemies() {
        boys = GetBoys(img);
    }

    private void createLevelData() {
        lvlData = GetLevelData(img);
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][] getLvlData() {
        return lvlData;
    }

    public int getLvlOffset() {
        return maxLvlOffsetX;
    }

    public ArrayList<Boy> getBoys() {
        return boys;
    }
}
