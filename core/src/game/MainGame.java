package game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import game.MenuScreens.GameOverMenu;
import game.MenuScreens.PauseMenu;

import java.util.Iterator;

/**
 * Main game logic and drawing, implements {@link Screen}.
 */
public class MainGame implements Screen {
    private final BlueSky blueSky;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Player player;
    private LifePickup lifePickup;
    private StarPickup starPickup;
    private FireBall directedFireball;
    private Array<FireBall> fireBalls;
    private PauseMenu pauseMenu;
    private GameOverMenu gameOverMenu;
    private long globalTime;
    private long pauseTime;
    private long pauseDelta;
    private long lifePickupLastSpawnScore;
    private long globalFireBallMovementSpeed;
    private long fireBallSpeedIncStartTime;
    private long scoreStartTime;
    private long directedFireBallStartTime;
    private long lifePickupStartTime;
    private long starPickupStartTime;
    private int fireBallSpawnDistanceY;
    private int gw;
    private int gh;
    private static SimpleLogger myLog;

    public static State gameState;

    public enum State
    {
        RUNNING,
        PAUSE,
        RESUME,
        GAME_OVER,
        GAME_WON
    }

    /**
     * Creates the game {@link Screen}.
     * @param game requires {@link BlueSky} instance
     */
    public MainGame(final BlueSky game) {
        blueSky = game;
        myLog = SimpleLogger.getLogger();

        gameState = State.RUNNING;

        gw = BlueSky.GAME_WIDTH;
        gh = BlueSky.GAME_HEIGHT;

        camera = new OrthographicCamera();

        viewport = new FitViewport(gw, gh, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2,0);

        player = new Player(gw/2, Player.TEXTURE_HEIGHT / 2);

        fireBalls = new Array<FireBall>();

        lifePickupLastSpawnScore = 0;

        globalFireBallMovementSpeed = BlueSky.GAME_HEIGHT/8;

        fireBallSpawnDistanceY = 140;

        setTimers();

        // Creating pause menu when creating a game, we expect it to be used in the future
        pauseMenu = new PauseMenu();
    }

    @Override
    public void render(float delta) {
        switch (gameState) {
            case RUNNING:
                update(delta);
                checkCollisions();
                draw();
                break;
            case PAUSE:
                draw();
                pauseMenu.render(delta);
                break;
            case RESUME:
                gameState = State.RUNNING;
                break;
            case GAME_OVER:
                draw();
                gameOverMenu.render(delta);
                break;
            case GAME_WON:
                gameState = State.GAME_OVER;
                break;
        }
    }

    @Override
    public void show() {
        // Called at the beginning
        Assets.backgroundMusic.play();
    }

    @Override
    public void resize(int width, int height) {
        // Called in loop when resizing
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2,0);
    }

    @Override
    public void pause() {
        // Called when game is paused and right before exit
        if (gameState != State.GAME_OVER) {
            myLog.info("Pause menu entered via 'Pause' call");
            gameState = State.PAUSE;
        }
    }

    @Override
    public void resume() {
        // called when game came back from pause(), this is for mobile
        if (gameState != State.GAME_OVER) {
            gameState = State.PAUSE;
        }
    }

    @Override
    public void hide() {
        // Called right before exit or changing screen, since we don't change screen it is empty
    }

    @Override
    public void dispose() {
        // Nothing to dispose of, all is done in BlueSky class
    }

    /**
     * Draws all the game object {@link com.badlogic.gdx.graphics.g2d.SpriteBatch}, uses {@link Assets}
     *
     * TODO Place some of the drawing in to their respective classes.
     *
     */
    private void draw(){
        // Clear the screen and set background color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        blueSky.batch.setProjectionMatrix(camera.combined);

        blueSky.batch.begin();

        // Draw background
        blueSky.batch.draw(Assets.backgroundImage, 0, 0 , gw, gh);

        // Draw damage indicator
        if (player.isHit() && player.getLife() > 0){
            if (globalTime - player.getDamageScreenActiveTime() < TimeUtils.millisToNanos(100)){
                blueSky.batch.draw(Assets.damageImage, 0, 0 , gw, gh);
            } else {
                player.setHit(false);
            }
        }

        // Draw fireballs
        if (directedFireball != null){
            blueSky.batch.draw(
                    Assets.fireBallImage, directedFireball.hitBox.x, directedFireball.hitBox.y,
                    FireBall.TEXTURE_WIDTH, FireBall.TEXTURE_HEIGHT
            );
        }
        for(FireBall fireBall: fireBalls) {
            blueSky.batch.draw(
                    Assets.fireBallImage, fireBall.hitBox.x, fireBall.hitBox.y,
                    FireBall.TEXTURE_WIDTH, FireBall.TEXTURE_HEIGHT
            );
        }

        // Draw solid UI
        blueSky.batch.draw(Assets.uiBackgroundImage, 0, gh-gh/10, gw, gh/10);

        // Draw life pickup
        if (lifePickup != null){
            if (lifePickup.isQuarterLifeReached()){
                blueSky.batch.draw(
                        Assets.quarterLifePickupImage, lifePickup.hitBox.x, lifePickup.hitBox.y,
                        LifePickup.WIDTH, LifePickup.HEIGHT
                );
            } else {
                blueSky.batch.draw(
                        Assets.lifePickupImage, lifePickup.hitBox.x, lifePickup.hitBox.y,
                        LifePickup.WIDTH, LifePickup.HEIGHT
                );
            }
        }

        // Draw star pickup
        if (starPickup != null){
            blueSky.batch.draw(
                    Assets.starPickupImage, starPickup.hitBox.x, starPickup.hitBox.y,
                    StarPickup.width, StarPickup.height
            );
        }

        // Draw payer
        float playerBatchX = player.hitBox.x - Player.BOUNDARIES_X;
        float playerBatchY = player.hitBox.y;
        blueSky.batch.draw(
                Assets.playerImage, playerBatchX, playerBatchY,
                Player.TEXTURE_WIDTH, Player.TEXTURE_HEIGHT
        );

        // Draw player life
        blueSky.batch.draw(Assets.lifeUiImage, gw/320, gh - gh/10, gw/10, gh/10);
        Assets.font64b.draw(blueSky.batch, Integer.toString(player.getLife()), gw/10 + 6, gh - gh/40);

        //Draw score
        if (player.isStarPickupActive()){
            Assets.font64w.setColor(Color.YELLOW);
            Assets.font64w.draw(blueSky.batch, Long.toString(player.getScore()), gw/2 + 76, gh - gh/40);
        } else {
            Assets.font64b.draw(blueSky.batch, Long.toString(player.getScore()), gw/2 + 76, gh - gh/40);
        }

        // Draw pause menu background
        if (gameState == State.PAUSE || gameState == State.GAME_OVER){
            blueSky.batch.draw(Assets.pauseImage, 0, 0 , gw, gh);
        }

        blueSky.batch.end();
    }

    // Object Updates

    /**
     * All the updates are placed here, also contains music volume.
     * When game returns from pause volume is set here.
     * @param delta frame delta time
     */
    private void update(float delta){
        Assets.backgroundMusic.setVolume(0.5f);
        setGlobalTime();
        checkGameState();
        updateFireballs();
        updateDirectedFireBall();
        player.update(delta);
        updatePlayerScore();
        updateLifePickup();
        updateStarPickup();
    }

    /**
     * All the fire ball updates are placed here.
     */
    private void updateFireballs(){
        spawnDirectedFireBall();
        spawnFireBallsBasedOnY();
        allFireBallCollisionAndMovement();
    }

    /**
     * Updates directed fireballs x and y x coordinates or destroys it.
     */
    private void updateDirectedFireBall(){
        if (directedFireball != null){
            float speed = (directedFireball.getMovementSpeed() + globalFireBallMovementSpeed) * Gdx.graphics.getDeltaTime();
            directedFireball.hitBox.y -= speed;
            if (directedFireball.hitBox.y + FireBall.HEIGHT < 0 || directedFireball.collided()){
                directedFireball = null;
            }
        }
    }

    /**
     * Updates player score according to {@link MainGame#scoreStartTime},
     * {@link MainGame#globalFireBallMovementSpeed}
     * and {@link Player#starPickupActive}
     */
    private void updatePlayerScore(){
        if (globalTime - scoreStartTime > TimeUtils.millisToNanos(2080 - globalFireBallMovementSpeed)){
            if (player.isStarPickupActive()){
                long timeDif = globalTime - player.getPlayerStarActiveTime();

                player.setScore(100);

                if (timeDif > TimeUtils.millisToNanos(5600)){
                    player.setStarPickupActive(false);
                }
            } else {
                player.setScore();
            }
            scoreStartTime = globalTime;
        }
    }

    /**
     * Updates life pickup, sets {@link LifePickup#quarterLifeReached} or destroys it.
     */
    private void updateLifePickup(){
        spawnLifePickup();
        if (lifePickup != null){
            if (globalTime - lifePickupStartTime > TimeUtils.millisToNanos(3750)){
                lifePickup.setQuarterLifeReached(true);
            }
            if (globalTime - lifePickupStartTime > TimeUtils.millisToNanos(5000)){
                lifePickup = null;
            }
        }
    }

    /**
     * Updates star pickup, destroys it if necessary.
     */
    private void updateStarPickup(){
        spawnStarPickup();
        if (starPickup != null){
            if (globalTime - starPickupStartTime > TimeUtils.millisToNanos(5000)){
                starPickup = null;
            }
        }
    }

    // Object Collisions

    /**
     * All collisions are placed here
     */
    private void checkCollisions(){
        pickupCollisionDetection();
        directedFireballCollisionDetection();
    }

    /**
     * Check if directed {@link FireBall} has hit {@link Player}
     */
    private void directedFireballCollisionDetection(){
        if (directedFireball != null){
            if (player.hitBox.overlaps(directedFireball.hitBox)){
                directedFireball.setCollision(true);
                player.hitEnemy(globalTime);
            }
        }
    }

    /**
     * Checks if {@link FireBall} has hit {@link Player}.
     * This triggers global {@link FireBall} movement speed and spawn distance reset
     * @param fireBall requires {@link FireBall}
     */
    private void fireBallsCollisionDetection(FireBall fireBall){
        if (player.hitBox.overlaps(fireBall.hitBox)){
            fireBall.setCollision(true);
            player.hitEnemy(globalTime);
            globalFireBallMovementSpeed = BlueSky.GAME_HEIGHT/8;
            fireBallSpawnDistanceY = 140;
        }
    }

    /**
     * All beneficial pickup collision with {@link Player}
     */
    private void pickupCollisionDetection(){
        if (lifePickup != null){
            if (player.hitBox.overlaps(lifePickup.hitBox)){
                player.hitLifePickup();
                lifePickup = null;
            }
        }

        if (starPickup != null){
            if (player.hitBox.overlaps(starPickup.hitBox)){
                player.setStarPickupActive(true);
                player.setPlayerStarActiveTime(globalTime);
                starPickup = null;
            }
        }
    }

    /**
     * {@link FireBall} {@link Array} is iterated here.
     * {@link MainGame#fireBallsCollisionDetection} and {@link MainGame#allFireBallSpeedAndSpawnDistance} is called.
     * x and y coordinates are updated depending on {@link MainGame#globalFireBallMovementSpeed}.
     */
    private void allFireBallCollisionAndMovement(){
        Iterator<FireBall> iter = fireBalls.iterator();
        while(iter.hasNext()) {
            FireBall fireBall = iter.next();
            fireBallsCollisionDetection(fireBall);

            if (fireBall.collided()){
                iter.remove();
            }

            fireBall.hitBox.y -= globalFireBallMovementSpeed * Gdx.graphics.getDeltaTime();

            if(fireBall.hitBox.y + FireBall.HEIGHT < 0){
                iter.remove();
            }
        }
        allFireBallSpeedAndSpawnDistance();
    }

    /**
     * Increases fireball speed and decreases spawn y distance every half a second.
     */
    private void allFireBallSpeedAndSpawnDistance(){
        if (globalTime - fireBallSpeedIncStartTime > 500000000){
            fireBallSpeedIncStartTime = globalTime;

            if (globalFireBallMovementSpeed < gh) {
                globalFireBallMovementSpeed += 10;
            }

            if (fireBallSpawnDistanceY > FireBall.HEIGHT *3){
                fireBallSpawnDistanceY -= 4;
            }
        }
    }

    // Object Spawns

    /**
     * Spawns a {@link LifePickup} every 1000 points in a random location on top half of the screen.
     */
    private void spawnLifePickup(){
        if (lifePickup == null){
            int x = MathUtils.random(0, gw - LifePickup.WIDTH);
            int y = MathUtils.random(gh - gh/2, gh - (gh/10 + LifePickup.HEIGHT + 2));
            if (player.getScore() - lifePickupLastSpawnScore >= 1000){
                lifePickup = new LifePickup(x, y);
                lifePickupStartTime = globalTime;
                lifePickupLastSpawnScore = player.getScore();
            }
        }
    }

    /**
     * Spawns a {@link StarPickup} every 10 seconds in a random location on top half of the screen.
     */
    private void spawnStarPickup(){
        if (starPickup == null){
            int x = MathUtils.random(0, gw - LifePickup.WIDTH);
            int y = MathUtils.random(gh - gh/2, gh - (gh/10 + LifePickup.HEIGHT + 2));
            if(globalTime - starPickupStartTime > TimeUtils.millisToNanos(20000)){
                starPickup = new StarPickup(x, y);
                starPickupStartTime = globalTime;
            }
        }
    }

    /**
     * Spawn a {@link FireBall} using {@link MainGame#spawnFireBall()} or {@link MainGame#spawnFireBall(FireBall)}.
     */
    private void spawnFireBallsBasedOnY(){
        if (fireBalls != null && fireBalls.size != 0) {
            FireBall lastFireball = fireBalls.get(fireBalls.size - 1);
            if (lastFireball.hitBox.y <= BlueSky.GAME_HEIGHT - fireBallSpawnDistanceY){
                spawnFireBall(lastFireball);
            }
        } else {
            spawnFireBall();
        }
    }

    /**
     * Spawn a {@link FireBall} on a random point of x axis.
     */
    private void spawnFireBall(){
        int x = MathUtils.random(FireBall.TEXTURE_WIDTH, BlueSky.GAME_WIDTH - FireBall.TEXTURE_WIDTH);
        int y = BlueSky.GAME_HEIGHT;
        FireBall fireBall = new FireBall(x, y);
        fireBalls.add(fireBall);
    }

    /**
     * Spawn a {@link FireBall} depending on how far has previous {@link FireBall} traveled on y axis.
     * @param lastFireball requires {@link FireBall}
     */
    private void spawnFireBall(FireBall lastFireball){
        int x;
        int y;

        if (lastFireball.hitBox.x < BlueSky.GAME_WIDTH / 2) {
            x = MathUtils.random(BlueSky.GAME_WIDTH / 2, BlueSky.GAME_WIDTH - FireBall.TEXTURE_WIDTH);
        } else {
            x = MathUtils.random(FireBall.TEXTURE_WIDTH, BlueSky.GAME_WIDTH / 2);
        }

        y = BlueSky.GAME_HEIGHT;
        FireBall fireBall = new FireBall(x, y);
        fireBalls.add(fireBall);
    }

    /**
     * Spawn a {@link FireBall} depending on {@link Player} location on x axis.
     */
    private void spawnDirectedFireBall(){
        if (directedFireball == null){
            if (globalTime - directedFireBallStartTime > TimeUtils.millisToNanos(2000)){
                int x = (int)player.hitBox.x;
                int y = BlueSky.GAME_HEIGHT;
                directedFireball = new FireBall(x,y,gh/2);
                directedFireBallStartTime = globalTime;
            }
        }
    }

    // Game states

    /**
     * All game state checks are located here
     */
    private void checkGameState(){
        checkGameWon();
        checkGameOver();
        checkGamePause();
    }

    /**
     * Check if {@link Player} has won by collecting 999990 points.
     */
    private void checkGameWon(){
        if (player.getScore() >= 999990){
            myLog.info("Game Won");
            gameState = State.GAME_WON;
        }
    }

    /**
     * Check if {@link Player} has lost by loosing all {@link Player#life}.
     */
    private void checkGameOver(){
        if (player.getLife() <= 0){
            myLog.info("Game Over");
            gameOverMenu = new GameOverMenu(blueSky, player);
            pauseMenu.dispose();
            gameState = State.GAME_OVER;
        }
    }

    /**
     * Check if game entered pause {@link MainGame#gameState} by user pressing escape key.
     */
    private void checkGamePause(){
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            myLog.info("Escape button pressed on keyboard");
            pauseTime = TimeUtils.nanoTime();
            Assets.backgroundMusic.setVolume(0);
            pause();
        }
    }

    // Game timers

    /**
     * Set all the game timers according to {@link MainGame#globalTime}.
     */
    private void setTimers(){
        setGlobalTime();
        fireBallSpeedIncStartTime = globalTime;
        directedFireBallStartTime = globalTime;
        starPickupStartTime = globalTime;
    }

    /**
     * Set {@link MainGame#globalTime} by subtracting time that game has spent in pause {@link MainGame#gameState}.
     */
    private void setGlobalTime(){
        if (pauseTime != 0) {
            pauseDelta += TimeUtils.nanoTime() - pauseTime;
        }
        globalTime = TimeUtils.nanoTime() - pauseDelta;
        pauseTime = 0;
    }
}
