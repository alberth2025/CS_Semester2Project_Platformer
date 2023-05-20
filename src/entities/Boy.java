package entities;

import Main.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

public class Boy extends Enemy{

    //Attackbox

    private Rectangle2D.Float attackBox;
    public Boy(float x, float y) {
        super(x, y, BOY_WIDTH, BOY_HEIGHT, BOY);
        initHitbox(x, y, (int)(12* (Game.SCALE+1)), (int) (15*(Game.SCALE+1)));
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
            switch (enemyState){
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

    public void drawAttackBox(Graphics g, int xLvlOffset){
        g.setColor(Color.RED);
        g.drawRect((int)(attackBox.x - xLvlOffset), (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);
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
