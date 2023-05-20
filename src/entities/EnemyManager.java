package entities;

import gamestates.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Main.Game.TILES_SIZE;
import static utilz.Constants.EnemyConstants.*;
public class EnemyManager {
    private Playing playing;
    private BufferedImage[][] boyArr;
    private ArrayList<Boy> boys = new ArrayList<>();
    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
        addEnemies();
    }

    private void addEnemies() {
        boys = LoadSave.GetBoys();
        System.out.println("Number of boys " + boys.size());
    }

    public void update(int[][] lvlData, Player player){
        for (Boy b: boys)
            b.update(lvlData, player);
    }

    public void draw(Graphics g, int xLvlOffset){
        drawBoys(g, xLvlOffset);
    }

    private void drawBoys(Graphics g, int xLvlOffset) {
        for (Boy b: boys) {
            g.drawImage(boyArr[b.getEnemyState()][b.getAniIndex()], (int) b.getHitBox().x - xLvlOffset - BOY_DRAWOFFSET_X, (int) b.getHitBox().y - BOY_DRAWOFFSET_Y, BOY_WIDTH, BOY_HEIGHT, null);
            b.drawHitbox(g, xLvlOffset);
        }
    }

    private void loadEnemyImgs() {
        boyArr = new BufferedImage[5][4];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.BOY_SPRITE);
        for (int j = 0; j < boyArr.length; j++)
            for (int i = 0; i < boyArr[j].length; i++)
                boyArr[j][i] = temp.getSubimage(20 * i + 80, 30 * j + 20, 16, 16);
    }
}
