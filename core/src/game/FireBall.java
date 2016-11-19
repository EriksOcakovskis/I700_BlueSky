package game;

import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by eriks on 14/11/2016.
 */
class FireBall extends Enemy {
    public static final int width = 12;
    public static final int height = 16;
    public static int movementSpeed = 100;
    public static int spawnHeight = 140;

    FireBall(int x, int y){
        super(x, y, width, height);
    }

    public void update(float delta){

    }

    public void hitPlayer(){
        movementSpeed = 100;
        spawnHeight = 140;
    }
}
