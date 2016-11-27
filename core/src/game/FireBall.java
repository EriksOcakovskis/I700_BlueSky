package game;

/**
 * Created by eriks on 14/11/2016.
 */
public class FireBall extends BaseActor {
    public static final int WIDTH = BlueSky.GAME_HEIGHT/80;
    public static final int HEIGHT = 44;
    public static final int TEXTURE_WIDTH = WIDTH;
    public static final int TEXTURE_HEIGHT = HEIGHT;

    private boolean collision;
    private int movementSpeed;


    public FireBall(int x, int y){
        super(x, y, WIDTH, HEIGHT);
        this.collision = false;
    }

    public FireBall(int x, int y, int mS){
        super(x, y, WIDTH, HEIGHT);
        this.collision = false;
        this.movementSpeed = mS;
    }

    public boolean collided() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }
}
