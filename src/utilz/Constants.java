package utilz;

import Main.Game;
import gamestates.Gamestate;

public class Constants {

    public static class UI{
        public static class Buttons{
            public static final int B_WIDTH_DEFAULT = 140;
            public static final int B_HEIGHT_DEFAULT = 56;
            public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT* Game.SCALE)/2;
            public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT* Game.SCALE)/2;
        }
        public static class PauseButtons{
            public static final int SOUND_SIZE_DEFAULT = 42;
            public static final int SOUND_SIZE = (int)(SOUND_SIZE_DEFAULT*Game.SCALE)/2;
        }

        public static class URMButtons{
            public static final int URM_DEFAULT_SIZE = 56;
            public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE*Game.SCALE)/2;
        }

        public static class VolumeButtons{
            public static final int VOLUME_DEFAULT_WIDTH = 28;
            public static final int VOLUME_DEFAULT_HEIGHT = 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH = (int)(VOLUME_DEFAULT_WIDTH*Game.SCALE)/2;
            public static final int VOLUME_HEIGHT = (int)(VOLUME_DEFAULT_HEIGHT*Game.SCALE)/2;
            public static final int SLIDER_WIDTH = (int)(SLIDER_DEFAULT_WIDTH * Game.SCALE)/2;
        }
    }
    public static class PlayerConstants{

        public static final int IDLE = 0;
        public static final int MOVING = 1;
        public static final int ATTACK = 2;
        public static final int DEATH = 3;
        public static final int HURT = 4;

        public static int GetSpriteAmounts(int player_action){
            switch (player_action){
                case IDLE:
                case HURT:
                    return 2;
                case MOVING:
                case DEATH:
                    return 4;
                case ATTACK:
                    return 3;
                default:
                    return 1;
            }
        }
    }
}
