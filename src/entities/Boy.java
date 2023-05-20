package entities;

import Main.Game;
import gamestates.Playing;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;
import static utilz.Constants.EnemyConstants.*;

public class Boy extends Enemy{

    private Playing playing;

    public Boy(float x, float y) {
        super(x, y, BOY_WIDTH, BOY_HEIGHT, BOY);
        initHitbox(12,15);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(5*(Game.SCALE+1)), (int)(15*(Game.SCALE+1)));
    }

    public void update(int[][] lvlData, Player player){
        updateBehavior(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }
    private void updateAttackBox(){
        if (walkDir == RIGHT){
            attackBox.x = hitBox.x + hitBox.width + 1*Game.SCALE;
        }else if(walkDir == LEFT){
            attackBox.x = hitBox.x - attackBox.width - 1*Game.SCALE;
        }
        attackBox.y = hitBox.y;
    }
    private void updateBehavior(int[][] lvlData, Player player){
        if (firstUpdate)
            firstUpdateCheck(lvlData);
        if (inAir){
            updateInAir(lvlData);
        }else {
            switch (state){
                case IDLE:
                    newState(MOVING);
                    break;
                case MOVING:
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardsPlayer(player);
                        if (isPLayerCloseForAttack(player))
                            newState(ATTACK);
                    }
                    move(lvlData);
                    break;
                case ATTACK:
                    if (aniIndex == 0)
                        attackChecked = false;
                    if (aniIndex==1 && !attackChecked)
                        checkPlayerHit(attackBox,player);
                    break;
                case HURT:
                    break;
            }
        }
    }


    public int flipX(){
        if (walkDir == LEFT)
            return (int)(width - 8);
        else
            return 0;
    }
    public int flipW(){
        if (walkDir == LEFT)
            return -1;
        else
            return 1;
    }
}
