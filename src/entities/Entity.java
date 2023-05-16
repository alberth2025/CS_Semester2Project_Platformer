package entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x, y;
    protected float width, height;
    protected Rectangle2D.Float hitBox;

    public Entity(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        initHitbox(x, y, width, height);
    }

    protected void initHitbox(float x, float y, float width, float height){
        hitBox = new Rectangle2D.Float(x, y, width, height);
    }

    public Rectangle2D.Float getHitBox(){
        return hitBox;
    }
}
