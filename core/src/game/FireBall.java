package game;

import com.badlogic.gdx.Gdx;

/**
 * Created by eriks on 14/11/2016.
 */
public class FireBall extends Enemy {
    private boolean collision;
    static final int width = BlueSky.GAME_HEIGHT/80;
    static final int height = 44;
    static final int textureWidth = BlueSky.GAME_HEIGHT/80;
    static final int textureHeight = 44;
    private int movementSpeed;


    FireBall(int x, int y){
        super(x, y, width, height);
        this.collision = false;
    }

    FireBall(int x, int y, int mS){
        super(x, y, width, height);
        this.collision = false;
        this.movementSpeed = mS;
    }

    boolean collided() {
        return collision;
    }

    void setCollision(boolean collision) {
        this.collision = collision;
    }

    int getMovementSpeed() {
        return movementSpeed;
    }

    void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }
}
