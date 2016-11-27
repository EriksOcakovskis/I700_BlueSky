package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by eriks on 13/11/2016.
 */

/**
 * Allows player actor creation.
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

    /**
     * Creates player with default {@link Player#life} count of 3.
     * @param x player x coordinates
     * @param y player y coordinates
     */
    public Player(int x, int y){
        super(x, y, WIDTH, HEIGHT);
        life = 3;
        score = -10;
        starPickupActive = false;
        isHit = false;
    }

    /**
     * Creates player.
     * @param x player x coordinates
     * @param y player y coordinates
     * @param l player {@link Player#life} count
     */
    public Player(int x, int y, int l){
        super(x, y, WIDTH, HEIGHT);
        life = l;
        score = -10;
        starPickupActive = false;
        isHit = false;
    }

    /**
     * Update player location according to user input.
     * @param delta frame delta time
     */
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

    /**
     * Increases player x coordinates.
     * @param delta frame delta time
     */
    private void movePlayerRight(float delta) {
        hitBox.x += (int)(MOVEMENT_SPEED * delta);
        playerBoundaries();
    }

    /**
     * Decreases player x coordinates.
     * @param delta frame delta time
     */
    private void movePlayerLeft(float delta) {
        hitBox.x -= (int)(MOVEMENT_SPEED * delta);
        playerBoundaries();
    }

    /**
     * Increases player y coordinates.
     * @param delta frame delta time
     */
    private void movePlayerUp(float delta) {
        hitBox.y += (int)(MOVEMENT_SPEED * delta);
        playerBoundaries();
    }

    /**
     * Decreases player y coordinates.
     * @param delta frame delta time
     */
    private void movePlayerDown(float delta) {
        hitBox.y -= (int)(MOVEMENT_SPEED * delta);
        playerBoundaries();
    }

    /**
     * Makes sure player does not leave game area.
     */
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

    /**
     * Sets life.
     * @param l {@code int} value
     */
    private void setLife(int l) {
        this.life += l;
    }

    /**
     * Sets all the values if player has been hit.
     * @param t time in nanoseconds
     */
    public void hitEnemy(long t){
        this.life -= 1;
        setHit(true);
        setDamageScreenActiveTime(t);
    }

    /**
     * Performs actions that are required if {@link LifePickup} is touched.
     */
    public void hitLifePickup(){
        this.setLife(1);
    }

    /**
     * Gets player score
     * @return {@link Player#score}
     */
    public long getScore() {
        return score;
    }

    /**
     * Adds 10 to {@link Player#score}.
     */
    public void setScore() {
        this.score += 10;
    }

    /**
     * Adds some {@code int} to {@link Player#score}.
     * @param s requires {@code int}
     */
    public void setScore(int s) {
        this.score += s;
    }

    /**
     * Gets player life.
     * @return {@link Player#life}
     */
    public int getLife() {
        return life;
    }

    /**
     * Gets {@link Player#starPickupActive} state.
     * @return {@code true} or {@code false}
     */
    public boolean isStarPickupActive() {
        return starPickupActive;
    }

    /**
     * Sets {@link Player#starPickupActive}.
     * @param starPickupActive {@code true} or {@code false}
     */
    public void setStarPickupActive(boolean starPickupActive) {
        this.starPickupActive = starPickupActive;
    }

    /**
     * Gets {@link Player#playerStarActiveTime}.
     * @return time in nanoseconds
     */
    public long getPlayerStarActiveTime() {
        return playerStarActiveTime;
    }

    /**
     * Sets {@link Player#playerStarActiveTime}.
     * @param playerStarActiveTime time in nanoseconds
     */
    public void setPlayerStarActiveTime(long playerStarActiveTime) {
        this.playerStarActiveTime = playerStarActiveTime;
    }

    /**
     * Gets {@link Player#isHit} value.
     * @return {@code true} or {@code false}
     */
    public boolean isHit() {
        return isHit;
    }

    /**
     * Sets {@link Player#isHit} value.
     * @param hit {@code true} or {@code false}
     */
    public void setHit(boolean hit) {
        isHit = hit;
    }

    /**
     * Gets {@link Player#damageScreenActiveTime} time value.
     * @return time in nanoseconds
     */
    public long getDamageScreenActiveTime() {
        return damageScreenActiveTime;
    }

    /**
     * Sets {@link Player#damageScreenActiveTime} time value.
     * @param damageScreenActiveTime time in nanoseconds
     */
    public void setDamageScreenActiveTime(long damageScreenActiveTime) {
        this.damageScreenActiveTime = damageScreenActiveTime;
    }
}
