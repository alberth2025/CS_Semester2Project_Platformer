package entities;

import Main.Game;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

public class Boy extends Enemy{
    public Boy(float x, float y) {
        super(x, y, BOY_WIDTH, BOY_HEIGHT, BOY);
        initHitbox(x, y, (int)(12* (Game.SCALE+1)), (int) (15*(Game.SCALE+1)));
    }

    public void update(int[][] lvlData, Player player){
        updateMove(lvlData, player);
        updateAnimationTick();
    }
    private void updateMove(int[][] lvlData, Player player){
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
                    if (canSeePlayer(lvlData, player))
                        turnTowardsPlayer(player);
                    if (isPLayerCloseForAttack(player))
                        newState(ATTACK);
                    move(lvlData);
                    break;
            }
        }
    }
}
