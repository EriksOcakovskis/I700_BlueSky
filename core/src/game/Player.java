package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by eriks on 13/11/2016.
 */
class Player extends BasicActor {
    static final int width = 16;
    static final int height = 16;
    private static final int textureWidth = 16;
    private static final int textureHeight = 16;
    private static final int movementSpeed = 250;

    int life;


    Player(int x, int y){
        super(x, y, width, height);
        life = 3;
    }

    void update(float delta){
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)){
            this.movePlayerRight(delta);
        } else if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.movePlayerLeft(delta);
        } else if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)){
            this.movePlayerUp(delta);
        } else  if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)){
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
        if(hitBox.x < 0) {
            hitBox.x = 0;
        } else if(hitBox.x > BlueSky.GAME_WIDTH - textureWidth) {
            hitBox.x = BlueSky.GAME_WIDTH - textureWidth;
        } else if(hitBox.y < 0){
            hitBox.y = 0;
        } else if(hitBox.y > BlueSky.GAME_HEIGHT - textureHeight){
            hitBox.y = BlueSky.GAME_HEIGHT - textureHeight;
        }
    }

    void hitEnemy(){
        this.life -= 1;
    }
}
