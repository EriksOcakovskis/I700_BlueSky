package game;

import com.badlogic.gdx.Gdx;

/**
 * Created by eriks on 14/11/2016.
 */
class FireBall extends Enemy {
    private boolean collision;
    static final int width = BlueSky.GAME_HEIGHT/80;
    static final int height = 44;
    static final int textureWidth = BlueSky.GAME_HEIGHT/80;
    static final int textureHeight = 44;
    static int globalMovementSpeed = BlueSky.GAME_HEIGHT/8;
    private int movementSpeed;
    static int spawnDistanceY = 140;

    FireBall(int x, int y){
        super(x, y, width, height);
        this.collision = false;
    }

    FireBall(int x, int y, int mS){
        super(x, y, width, height);
        this.collision = false;
        this.movementSpeed = mS;
    }

    void hitPlayer(){
        globalMovementSpeed = BlueSky.GAME_HEIGHT/8;
        spawnDistanceY = 140;
    }

    boolean collided() {
        return collision;
    }

    void setCollision(boolean collision) {
        this.collision = collision;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(int movementSpeed) {
        this.movementSpeed = movementSpeed;
    }
}
