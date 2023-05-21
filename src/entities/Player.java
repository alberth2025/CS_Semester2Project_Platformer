package entities;

import Main.Game;
import audio.AudioPlayer;
import gamestates.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static Main.Game.GAME_HEIGHT;
import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.GRAVITY;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

public class Player extends Entity {

    private BufferedImage[][] animations;
    private boolean left, right, jump;
    private boolean moving = false, attacking = false;
    private int[][] lvlData;
    private final float xDrawOffset = 1 * Game.SCALE;
    private final float yDrawOffset = 1 * Game.SCALE;

    //jumping and gravity
    private final float jumpSpeed = -2.75f * Game.SCALE;
    private final float fallSpeedAfterCollision = 0.5f * Game.SCALE;

    //StatusBarUI
    private BufferedImage statusBarImg;

    private final int statusBarWidth = (int) (222 * (Game.SCALE + 1));
    private final int statusBarHeight = (int) (15 * (Game.SCALE + 1));
    private final int statusBarX = (int) (1 * (Game.SCALE + 1));
    private final int statusBarY = (int) (1 * (Game.SCALE + 1));

    private final int healthBarWidth = (int) (208 * (Game.SCALE + 1));
    private final int healthBarHeight = (int) (11 * (Game.SCALE + 1));
    private final int healthBarXStart = (int) (13 * (Game.SCALE + 1));
    private final int healthBarYStart = (int) (3 * (Game.SCALE + 1));

    private int healthWidth = healthBarWidth;
    private final Playing playing;

    //AttackBox
    private int flipX = 0;
    private int flipW = 1;
    private boolean attackChecked;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        this.state = IDLE;
        this.maxHealth = 100;
        this.currentHealth = maxHealth;
        this.moveSpeed = Game.SCALE;
        loadAnimations();
        initHitbox(12, 15);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (17 * (Game.SCALE + 1)), (int) (15 * (Game.SCALE + 1)));
    }

    public void update() {
        updateHealthBar();

        if (currentHealth <= 0) {
            if (state != DEATH) {
                state = DEATH;
                aniTick = 0;
                aniIndex = 0;
                playing.setPlayerDying(true);
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
            } else if (aniIndex == GetSpriteAmounts(DEATH) - 1 && aniTick >= ANI_SPEED - 1) {
                playing.setGameOver(true);
            } else
                updateAnimationTick();
            playing.setGameOver(true);
            return;
        }
        updateAttackBox();
        updatePos();
        if (attacking)
            checkAttack();
        updateAnimationTick();
        setAnimation();
    }

    private void checkAttack() {
        if (attackChecked || aniIndex != 0)
            return;
        attackChecked = true;
        playing.checkEnemyHit(attackBox);

    }

    private void updateAttackBox() {
        if (right) {
            attackBox.x = hitBox.x;
        } else if (left) {
            attackBox.x = hitBox.x - attackBox.width + hitBox.width;
        }
        attackBox.y = hitBox.y;
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    public void render(Graphics g, int lvlOffset) {
        g.drawImage(animations[state][aniIndex], (int) (hitBox.x - xDrawOffset) - lvlOffset + flipX, (int) (hitBox.y - yDrawOffset), 48 * flipW, 48, null);
        //drawAttackBox(g, lvlOffset);
        drawHitbox(g, lvlOffset);
        drawUI(g);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.RED);
        g.fillRect(healthBarXStart, healthBarYStart, healthWidth, healthBarHeight);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmounts(state)) {
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
            }
        }
    }

    private void setAnimation() {

        int startAni = state;

        if (moving)
            state = MOVING;
        else
            state = IDLE;

        if (attacking)
            state = ATTACK;

        if (startAni != state)
            resetAniTick();

    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {
        moving = false;

        if (!inAir) {
            if (!IsEntityOnFloor(hitBox, lvlData)) {
                inAir = true;
            }
        }

        if (jump)
            jump();

        if (!inAir)
            if ((!left && !right) || (right && left))
                return;

        float xSpeed = 0;

        if (left) {
            xSpeed -= moveSpeed;
            flipX = (int) width - 7;
            flipW = -1;
        }
        if (right) {
            xSpeed += moveSpeed;
            flipX = 0;
            flipW = 1;
        }

        if (inAir) {
            if (CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)) {
                hitBox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            } else if (hitBox.y + hitBox.height + 1 * (Game.SCALE + 1) >= GAME_HEIGHT) {
                changeHealth(-100);
            } else {
                hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }

        } else
            updateXPos(xSpeed);
        moving = true;
    }

    private void jump() {
        if (inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData)) {
            hitBox.x += xSpeed;
        } else {
            hitBox.x = GetEntityXPosNextToWall(hitBox, xSpeed);
        }
    }

    public void changeHealth(int value) {
        currentHealth += value;

        if (currentHealth <= 0) {
            currentHealth = 0;
            //gameOver();
        } else if (currentHealth >= maxHealth)
            currentHealth = maxHealth;
    }

    private void loadAnimations() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[5][4];
        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(20 * i + 80, 30 * j + 20, 16, 16);
            }
        }
        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitBox, lvlData))
            inAir = true;
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;

    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }


    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }


    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        state = IDLE;
        currentHealth = maxHealth;
        hitBox.x = x;
        hitBox.y = y;
        if (!IsEntityOnFloor(hitBox, lvlData))
            inAir = true;
    }
}
