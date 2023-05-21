package ui;

import gamestates.Gamestate;
import gamestates.Playing;

import java.awt.*;
import java.awt.event.KeyEvent;

import static Main.Game.GAME_HEIGHT;
import static Main.Game.GAME_WIDTH;

public class GameOverOverlay {
    private final Playing playing;

    public GameOverOverlay(Playing playing) {
        this.playing = playing;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        g.setColor(Color.white);
        g.drawString("Game Over.", GAME_WIDTH / 2 - 50, 150);
        g.drawString("You were defeated by the Orks.", GAME_WIDTH / 2 - 105, 250);
        g.drawString("The beasts take your armor and sword as trophies.", GAME_WIDTH / 2 - 170, 280);
        g.drawString("You spend your last moments in shame, knowing you failed your Chapter.", GAME_WIDTH / 2 - 240, 310);
        g.drawString("Press esc to enter Main Menu!", GAME_WIDTH / 2 - 100, 400);

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            playing.resetAll();
            Gamestate.state = Gamestate.MENU;
        }
    }
}
