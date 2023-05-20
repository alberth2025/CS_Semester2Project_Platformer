package ui;

import gamestates.Playing;

import java.awt.image.BufferedImage;

public class LevelCompletedOverlay {
    private Playing playing;
    private UrmButton menu, next;
    private BufferedImage img;
    private int bgX, bgY, bgW, bgH;
    public LevelCompletedOverlay(Playing playing){
        this.playing = playing;
        initImg();
    }

    private void initImg() {
    }
}
