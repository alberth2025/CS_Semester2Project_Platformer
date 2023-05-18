package gamestates;

import Main.Game;
import entities.Player;
import levels.LevelManager;
import ui.PauseOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static Main.Game.*;

public class Playing extends State implements Statemethods {
    private Player player;
    private LevelManager levelManager;
    private PauseOverlay pauseOverlay;
    private boolean paused = false;
    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * GAME_WIDTH);
    private int rightBorder = (int) (0.8 * GAME_WIDTH);
    private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
    private int maxTilesOffset = lvlTilesWide - TILES_IN_WIDTH;
    private int maxLvlOffsetX = maxTilesOffset * TILES_SIZE;

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        player = new Player(200, 200, (int) (16 * (Game.SCALE + 1)), (int) (16 * (Game.SCALE + 1)));
        player.loadLvlData(levelManager.getCurrentLevel().getLvlData());
        pauseOverlay = new PauseOverlay(this);

    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    @Override
    public void update() {
        if (!paused) {
            levelManager.update();
            player.update();
            checkCloseToBorder();
        } else {
            pauseOverlay.update();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitBox().x;
        int diff = playerX - xLvlOffset;

        if (diff > rightBorder)
            xLvlOffset += diff - rightBorder;
        else if(diff < leftBorder)
            xLvlOffset += diff-leftBorder;

        if (xLvlOffset > maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        else if (xLvlOffset<0)
            xLvlOffset = 0;
    }


    @Override
    public void draw(Graphics g) {
        levelManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);

        if (paused)
            pauseOverlay.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        if (paused)
            pauseOverlay.mouseDragged(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1)
            player.setAttacking(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (paused)
            pauseOverlay.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused)
            pauseOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused)
            pauseOverlay.mouseMoved(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                player.setJump(true);
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                player.setRight(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setAttacking(true);
                break;
            case KeyEvent.VK_ESCAPE:
                paused = !paused;
                break;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                player.setJump(false);
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                player.setRight(false);
                break;
        }
    }

    public void unpauseGame() {
        paused = false;
    }

    public Player getPlayer() {
        return player;
    }
}
