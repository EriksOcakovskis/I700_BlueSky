package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by eriks on 13/11/2016.
 */
class Player extends BasicActor {
    private int life;
    private long score;
    private int bombPickup;

    static final int width = BlueSky.GAME_WIDTH/40;
    static final int height = BlueSky.GAME_WIDTH/10;
    static final int textureWidth = BlueSky.GAME_WIDTH/10;
    static final int textureHeight = BlueSky.GAME_HEIGHT/10;
    static final int boundariesX = (textureWidth - width) / 2;
    static final int boundariesY = BlueSky.GAME_HEIGHT/10 + 64;
    private static final int movementSpeed = BlueSky.GAME_HEIGHT/2;


    Player(int x, int y){
        super(x, y, width, height);
        life = 3;
        score = -10;
        bombPickup = 0;
    }

    Player(int x, int y, int l){
        super(x, y, width, height);
        life = l;
        score = -10;
        bombPickup = 0;
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
        if(hitBox.x  - boundariesX < 0) {
            hitBox.x = boundariesX;
        } else if(hitBox.x > BlueSky.GAME_WIDTH - (boundariesX + width)) {
            hitBox.x = BlueSky.GAME_WIDTH - (boundariesX + width);
        } else if(hitBox.y < 0){
            hitBox.y = 0;
        } else if(hitBox.y > BlueSky.GAME_HEIGHT - boundariesY){
            hitBox.y = BlueSky.GAME_HEIGHT - boundariesY;
        }
    }

    void hitEnemy(){
        this.life -= 1;
    }

    void hitEnemy(int l){
        this.life -= l;
    }

    void hitLifePickup(){
        this.setLife(1);
    }

    public long getScore() {
        return score;
    }

    public void setScore() {
        this.score += 10;
    }

    public void setScore(int s) {
        this.score += s;
    }

    public int getBombPickup() {
        return bombPickup;
    }

    public void setBombPickup() {
        this.bombPickup += 1;
    }

    public int getLife() {
        return life;
    }

    public void setLife() {
        this.life += 1;
    }

    public void setLife(int l) {
        this.life += l;
    }
}
