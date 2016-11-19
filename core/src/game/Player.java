package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by eriks on 13/11/2016.
 */
class Player extends BasicActor {
    int life;
    static final int width = 8;
    static final int height = 16;
    static final int textureWidth = 32;
    static final int textureHeight = 32;
    private static final int movementSpeed = 300;


    Player(int x, int y){
        super(x, y, width, height);
        life = 3;
    }

    void update(float delta){
        boolean right = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);
        boolean left = Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);
        boolean up = Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W);
        boolean down = Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S);
        if(right){
            this.movePlayerRight(delta);
        } else if(left) {
            this.movePlayerLeft(delta);
        } else if(up){
            this.movePlayerUp(delta);
        } else if(down){
            this.movePlayerDown(delta);
        }
    }

    private void movePlayerRight(float delta) {
        hitBox.x += (int)(movementSpeed * delta);
        playerBoundaries();
    }

    private void movePlayerLeft(float delta) {
        hitBox.x -= (int)(movementSpeed * delta);
        playerBoundaries();
    }

    private void movePlayerUp(float delta) {
        hitBox.y += (int)(movementSpeed * delta);
        playerBoundaries();
    }

    private void movePlayerDown(float delta) {
        hitBox.y -= (int)(movementSpeed * delta);
        playerBoundaries();
    }

    private void playerBoundaries(){
        if(hitBox.x  - 12 < 0) {
            hitBox.x = 12;
        } else if(hitBox.x > BlueSky.GAME_WIDTH - 20) {
            hitBox.x = BlueSky.GAME_WIDTH - 20;
        } else if(hitBox.y - 8 < 0){
            hitBox.y = 8;
        } else if(hitBox.y > BlueSky.GAME_HEIGHT - 24){
            hitBox.y = BlueSky.GAME_HEIGHT - 24;
        }
    }

    void hitEnemy(){
        this.life -= 1;
    }
}
