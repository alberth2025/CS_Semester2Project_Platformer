package ui;

import Main.Game;
import gamestates.Gamestate;
import gamestates.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.PauseButtons.*;
import static utilz.Constants.UI.URMButtons.*;
import static utilz.Constants.UI.VolumeButtons.*;

public class PauseOverlay {
    private BufferedImage backgroundImg;
    private int backgroundX, backgroundY, backgroundW, backgroundH;

    private UrmButton menuB, replayB, unpauseB;
    private Playing playing;
    private AudioOptions audioOptions;

    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        audioOptions = playing.getGame().getAudioOptions();
        createUrmButtons();
    }



    private void createUrmButtons() {
        int menuX = (int)(516*Game.SCALE)/2;
        int replayX = (int)(596*Game.SCALE)/2;
        int unPauseX = (int)(676*Game.SCALE)/2;
        int bY = (int)(402*Game.SCALE)/2;

        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
        unpauseB = new UrmButton(unPauseX, bY, URM_SIZE, URM_SIZE, 0);
    }



    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        backgroundW = (int) (backgroundImg.getWidth() * Game.SCALE)/2;
        backgroundH = (int) (backgroundImg.getHeight() * Game.SCALE)/2;
        backgroundX = Game.GAME_WIDTH / 2 - backgroundW / 2;
        backgroundY = (int)(50*Game.SCALE);
    }

    public void update() {


        menuB.update();
        replayB.update();
        unpauseB.update();

        audioOptions.update();


    }

    public void draw(Graphics g) {
        //background
        g.drawImage(backgroundImg,backgroundX,backgroundY,backgroundW,backgroundH,null);



        //urm buttons
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);

        audioOptions.draw(g);


    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB))
            menuB.setMousePressed(true);
        else if (isIn(e, replayB))
            replayB.setMousePressed(true);
        else if (isIn(e, unpauseB))
            unpauseB.setMousePressed(true);
        else
            audioOptions.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                Gamestate.state = Gamestate.MENU;
                playing.unpauseGame();
            }
        }else if (isIn(e, replayB)) {
            if (replayB.isMousePressed()){
                playing.resetAll();
                playing.unpauseGame();
            }
        }else if (isIn(e, unpauseB)) {
            if (unpauseB.isMousePressed())
                playing.unpauseGame();
        }else
            audioOptions.mouseReleased(e);
        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();
    }

    public void mouseMoved(MouseEvent e) {

        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);

        if (isIn(e, menuB))
            menuB.setMouseOver(true);
        else if (isIn(e, replayB))
            replayB.setMouseOver(true);
        else if (isIn(e, unpauseB))
            unpauseB.setMouseOver(true);
        else
            audioOptions.mouseMoved(e);
    }

    private boolean isIn(MouseEvent e, PauseButton b){
        return b.getBounds().contains(e.getX(),e.getY());
    }


}
