package utilz;

public class Constants {

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
