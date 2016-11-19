package game;

import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by eriks on 14/11/2016.
 */
class FireBall extends Enemy {
    private boolean collision;
    static final int width = 12;
    static final int height = 16;
    static final int textureWidth = 16;
    static final int textureHeight = 16;
    static int movementSpeed = 100;
    static int spawnHeight = 140;

    FireBall(int x, int y){
        super(x, y, width, height);
        this.collision = false;
    }

    public void update(float delta){

    }

    void hitPlayer(){
        movementSpeed = 100;
        spawnHeight = 140;
    }

    boolean collided() {
        return collision;
    }

    void setCollision(boolean collision) {
        this.collision = collision;
    }
}
