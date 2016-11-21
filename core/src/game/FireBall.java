package game;

/**
 * Created by eriks on 14/11/2016.
 */
class FireBall extends Enemy {
    private boolean collision;
    static final int width = BlueSky.GAME_HEIGHT/80;
    static final int height = 44;
    static final int textureWidth = BlueSky.GAME_HEIGHT/80;
    static final int textureHeight = 44;
    static int movementSpeed = BlueSky.GAME_HEIGHT/8;
    static int spawnDistanceY = 140;

    FireBall(int x, int y){
        super(x, y, width, height);
        this.collision = false;
    }

    public void update(float delta){

    }

    void hitPlayer(){
        movementSpeed = BlueSky.GAME_HEIGHT/8;
        spawnDistanceY = 140;
    }

    boolean collided() {
        return collision;
    }

    void setCollision(boolean collision) {
        this.collision = collision;
    }
}
