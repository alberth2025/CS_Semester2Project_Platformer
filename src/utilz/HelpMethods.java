package utilz;

import Main.Game;
import entities.Boy;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.BOY;

public class HelpMethods {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!IsSolid(x, y, lvlData))
            if (!IsSolid(x + width, y + height, lvlData))
                if (!IsSolid(x + width, y, lvlData))
                    return !IsSolid(x, y + height, lvlData);
        return false;
    }

    private static boolean IsSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Game.TILES_SIZE;
        if (x < 0 || x >= maxWidth)
            return true;
        if (y < 0 || y >= Game.GAME_HEIGHT)
            return true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
        int value = lvlData[yTile][xTile];

        return value != 1;
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitBox, float xSpeed) {
        int currentTile = (int) (hitBox.x / Game.TILES_SIZE);
        if (xSpeed > 0) {
            //Right
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = (int) (Game.TILES_SIZE - hitBox.width);
            return tileXPos + xOffset - 1;
        } else {
            //Left
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitBox, float airSpeed) {
        int currentTile = (int) (hitBox.y / Game.TILES_SIZE);
        if (airSpeed > 0) {
            //Falling - touching floor
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int) (Game.TILES_SIZE - hitBox.height);
            return tileYPos + yOffset - 1;
        } else {
            //Jumping - touching roof
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitBox, int[][] lvlData) {
        //Check the pixel below bottom left and bottom right corners
        if (!IsSolid(hitBox.x, hitBox.y + hitBox.height + 1, lvlData))
            return IsSolid(hitBox.x + hitBox.width, hitBox.y + hitBox.height + 1, lvlData);
        return true;
    }

    public static boolean IsFloor(Rectangle2D.Float hitBox, float xSpeed, int[][] lvlData) {
        if (xSpeed > 0)
            return IsSolid(hitBox.x + hitBox.width + xSpeed, hitBox.y + hitBox.height + 1, lvlData);
        else
            return IsSolid(hitBox.x + xSpeed, hitBox.y + hitBox.height + 1, lvlData);
    }

    public static boolean AreAllTilesWalkable(int startX, int endX, int y, int[][] lvlData) {
        for (int i = 0; i < endX - startX; i++) {
            if (IsTileSolid(startX + i, y, lvlData))
                return false;
            if (!IsTileSolid(startX + i, y + 1, lvlData))
                return false;
        }
        return true;
    }

    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitBox, Rectangle2D.Float secondHitBox, int yTile) {
        int firstXTile = (int) (firstHitBox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitBox.x / Game.TILES_SIZE);

        if (firstXTile > secondXTile)
            return AreAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
        else
            return AreAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
    }

    public static int[][] GetLevelData(BufferedImage img) {
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if (value >= 7)
                    value = 0;
                lvlData[j][i] = value;
            }
        }
        return lvlData;
    }

    public static ArrayList<Boy> GetBoys(BufferedImage img) {
        ArrayList<Boy> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == BOY)
                    list.add(new Boy(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        }
        return list;
    }
}
