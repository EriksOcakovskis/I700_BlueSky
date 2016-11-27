package game;

/**
 * Created by eriks on 14/11/2016.
 */

/**
 * Allows fire ball actor creation.
 */
public class FireBall extends BaseActor {
    public static final int WIDTH = BlueSky.GAME_HEIGHT/80;
    public static final int HEIGHT = 44;
    public static final int TEXTURE_WIDTH = WIDTH;
    public static final int TEXTURE_HEIGHT = HEIGHT;

    private boolean collision;
    private int movementSpeed;


    /**
     * Creates fire ball without setting its movement speed
     * @param x fire balls x coordinates
     * @param y fire balls y coordinates
     */
    public FireBall(int x, int y){
        super(x, y, WIDTH, HEIGHT);
        this.collision = false;
    }

    /**
     * Creates fire ball and sets movement speed
     * @param x fire balls x coordinates
     * @param y fire balls y coordinates
     * @param mS fire balls movement speed
     */
    public FireBall(int x, int y, int mS){
        super(x, y, WIDTH, HEIGHT);
        this.collision = false;
        this.movementSpeed = mS;
    }

    /**
     * Returns if fireball has collided with something.
     * @return {@link FireBall#collision}
     */
    public boolean collided() {
        return collision;
    }

    /**
     * Sets collision
     * @param collision {@code true} or {@code false}
     */
    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    /**
     * Returns fire ball movement speed
     * @return {@link FireBall#movementSpeed}
     */
    public int getMovementSpeed() {
        return movementSpeed;
    }
}
