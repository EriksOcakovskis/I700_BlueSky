package game;

/**
 * Created by eriks on 14/11/2016.
 */
public class FireBall extends Enemy {
    public static final int width = 12;
    public static final int height = 16;
    private static int movementSpeed = 200;

    FireBall(int x, int y){
        super(x, y, width, height);
    }

    public void update(float delta){

    }

    public static int getMovementSpeed() {
        return movementSpeed;
    }

    public static void setMovementSpeed(int movementSpeed) {
        FireBall.movementSpeed = movementSpeed;
    }

    public void hitPlayer(){
        movementSpeed = 200;
    }


}
