package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by eriks on 13/11/2016.
 */
public class Player extends BaseActor {
    public static final int WIDTH = BlueSky.GAME_WIDTH/40;
    public static final int HEIGHT = BlueSky.GAME_HEIGHT/10;
    public static final int TEXTURE_WIDTH = BlueSky.GAME_WIDTH/10;
    public static final int TEXTURE_HEIGHT = BlueSky.GAME_HEIGHT/10;
    public static final int BOUNDARIES_X = (TEXTURE_WIDTH - WIDTH) / 2;
    public static final int BOUNDARIES_Y = BlueSky.GAME_HEIGHT/10 + 64;
    private static final int MOVEMENT_SPEED = BlueSky.GAME_HEIGHT/2;

    private int life;
    private long score;
    private long playerStarActiveTime;
    private long damageScreenActiveTime;
    private boolean starPickupActive;
    private boolean isHit;

    public Player(int x, int y){
        super(x, y, WIDTH, HEIGHT);
        life = 3;
        score = -10;
        starPickupActive = false;
        isHit = false;
    }

    public Player(int x, int y, int l){
        super(x, y, WIDTH, HEIGHT);
        life = l;
        score = -10;
        starPickupActive = false;
        isHit = false;
    }

    public void update(float delta){
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
        hitBox.x += (int)(MOVEMENT_SPEED * delta);
        playerBoundaries();
    }

    private void movePlayerLeft(float delta) {
        hitBox.x -= (int)(MOVEMENT_SPEED * delta);
        playerBoundaries();
    }

    private void movePlayerUp(float delta) {
        hitBox.y += (int)(MOVEMENT_SPEED * delta);
        playerBoundaries();
    }

    private void movePlayerDown(float delta) {
        hitBox.y -= (int)(MOVEMENT_SPEED * delta);
        playerBoundaries();
    }

    private void playerBoundaries(){
        if(hitBox.x  - BOUNDARIES_X < 0) {
            hitBox.x = BOUNDARIES_X;
        } else if(hitBox.x > BlueSky.GAME_WIDTH - (BOUNDARIES_X + WIDTH)) {
            hitBox.x = BlueSky.GAME_WIDTH - (BOUNDARIES_X + WIDTH);
        } else if(hitBox.y < 0){
            hitBox.y = 0;
        } else if(hitBox.y > BlueSky.GAME_HEIGHT - BOUNDARIES_Y){
            hitBox.y = BlueSky.GAME_HEIGHT - BOUNDARIES_Y;
        }
    }

    private void setLife(int l) {
        this.life += l;
    }

    public void hitEnemy(long t){
        this.life -= 1;
        setHit(true);
        setDamageScreenActiveTime(t);
    }

    public void hitLifePickup(){
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

    public int getLife() {
        return life;
    }

    public boolean isStarPickupActive() {
        return starPickupActive;
    }

    public void setStarPickupActive(boolean starPickupActive) {
        this.starPickupActive = starPickupActive;
    }

    public long getPlayerStarActiveTime() {
        return playerStarActiveTime;
    }

    public void setPlayerStarActiveTime(long playerStarActiveTime) {
        this.playerStarActiveTime = playerStarActiveTime;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    public long getDamageScreenActiveTime() {
        return damageScreenActiveTime;
    }

    public void setDamageScreenActiveTime(long damageScreenActiveTime) {
        this.damageScreenActiveTime = damageScreenActiveTime;
    }
}
