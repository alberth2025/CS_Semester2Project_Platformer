package entities;

import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x, y;
    protected float width, height;
    protected Rectangle2D.Float hitBox;
    protected int aniTick, aniIndex;
    protected int state;
    protected float airSpeed;
    protected boolean inAir = false;
    protected int maxHealth = 10;
    protected int currentHealth = maxHealth;
    protected Rectangle2D.Float attackBox;
    protected float moveSpeed = 2f;

    public Entity(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        initHitbox(width, height);
    }

    public void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.RED);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    protected void drawHitbox(Graphics g, int xLvlOffset) {
        g.setColor(Color.BLUE);
        g.drawRect((int) hitBox.x - xLvlOffset, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
    }

    protected void initHitbox(float width, float height) {
        hitBox = new Rectangle2D.Float(x, y, (int) (width * (Game.SCALE + 1)), (int) (height * (Game.SCALE + 1)));
    }

    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }

    public int getEnemyState() {
        return state;
    }

    public int getAniIndex() {
        return aniIndex;
    }
}
