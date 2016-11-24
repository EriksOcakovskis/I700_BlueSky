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

public class MainGame implements Screen {
    private final BlueSky myGame;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Player player;
    private LifePickup lifePickup;
    private StarPickup starPickup;
    private FireBall directedFireball;
    private Array<FireBall> fireBalls;
    private PauseMenu pauseMenu;
    private GameOverMenu gameOverMenu;
    private long lifePickupLastSpawnScore;
    private long globalFireBallMovementSpeed;
    private long startTime;
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
        GAMEOVER,
        GAMEWON
    }

    public MainGame(final BlueSky game) {
        myGame = game;
        myLog = SimpleLogger.getLogger();

        gameState = State.RUNNING;

        gw = BlueSky.GAME_WIDTH;
        gh = BlueSky.GAME_HEIGHT;

        camera = new OrthographicCamera();
        viewport = new FitViewport(gw, gh, camera);

        viewport.apply();

        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2,0);

        player = new Player(gw/2, Player.textureHeight / 2);
        myLog.debug("Player hit box x location: " + player.hitBox.x );
        myLog.debug("Player hit box y location: " + player.hitBox.y );

        fireBalls = new Array<FireBall>();
        lifePickupLastSpawnScore = 0;

        globalFireBallMovementSpeed = BlueSky.GAME_HEIGHT/8;

        fireBallSpawnDistanceY = 140;

        startTime = TimeUtils.nanoTime();
        directedFireBallStartTime = startTime;
        starPickupStartTime = startTime;

        pauseMenu = PauseMenu.getPauseMenue();
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
            case GAMEOVER:
                draw();
                gameOverMenu.render(delta);
                break;
            case GAMEWON:
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
        if (gameState != State.GAMEOVER) {
            myLog.info("Pause menu entered via 'Pause' call");
            gameState = State.PAUSE;
        }
        //Save high score
    }

    @Override
    public void resume() {
        // called when game came back from pause(), this is for mobile
        if (gameState != State.GAMEOVER) {
            gameState = State.PAUSE;
        }
    }

    @Override
    public void hide() {
        // Called right before exit or changing screen
    }

    @Override
    public void dispose() {

    }

    // Draw game objects
    private void draw(){
        // Clear the screen and set background color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        myGame.batch.setProjectionMatrix(camera.combined);

        myGame.batch.begin();
        myGame.batch.draw(Assets.backgroundImage, 0, 0 , gw, gh);
        myGame.batch.draw(Assets.uiBackgroundImage, 0, gh-gh/10, gw, gh/10);

        // Draw life pickup
        if (lifePickup != null){
            //LifePickup lifePickup = lifePickups.first();
            if (lifePickup.isQuarterLifeReached()){
                myGame.batch.draw(
                        Assets.quarterLifePickupImage, lifePickup.hitBox.x, lifePickup.hitBox.y,
                        LifePickup.width, LifePickup.height
                );
            } else {
                myGame.batch.draw(
                        Assets.lifePickupImage, lifePickup.hitBox.x, lifePickup.hitBox.y,
                        LifePickup.width, LifePickup.height
                );
            }
        }

        // Draw star pickup
        if (starPickup != null){
            myGame.batch.draw(
                    Assets.starPickupImage, starPickup.hitBox.x, starPickup.hitBox.y,
                    StarPickup.width, StarPickup.height
            );
        }

        // Draw payer
        float playerBatchX = player.hitBox.x - Player.boundariesX;
        float playerBatchY = player.hitBox.y;
        myGame.batch.draw(
                Assets.playerImage, playerBatchX, playerBatchY,
                Player.textureWidth, Player.textureHeight
        );

        // Draw fireballs
        if (directedFireball != null){
            myGame.batch.draw(
                    Assets.fireBallImage, directedFireball.hitBox.x, directedFireball.hitBox.y,
                    FireBall.textureWidth, FireBall.textureHeight
            );
        }

        for(FireBall fireBall: fireBalls) {
            myGame.batch.draw(
                    Assets.fireBallImage, fireBall.hitBox.x, fireBall.hitBox.y,
                    FireBall.textureWidth, FireBall.textureHeight
            );
        }

        // Draw player life
        myGame.batch.draw(Assets.lifeUiImage, gw/320, gh - gh/10, gw/10, gh/10);
        Assets.font64b.draw(myGame.batch, Integer.toString(player.getLife()), gw/10 + 6, gh - gh/40);

        //Draw score
        if (player.isStarPickupActive()){
            Assets.font64w.setColor(Color.YELLOW);
            Assets.font64w.draw(myGame.batch, Long.toString(player.getScore()), gw/2 + 76, gh - gh/40);

        } else {
            Assets.font64b.draw(myGame.batch, Long.toString(player.getScore()), gw/2 + 76, gh - gh/40);
        }
        myGame.batch.end();
    }

    // Object Updates
    private void update(float delta){
        checkGameOver();
        checkGamePause();
        updateFireballs();
        updateDirectedFireBall();
        player.update(delta);
        updatePlayerScore();
        updateLifePickup();
        updateStarPickup();
    }

    private void updateFireballs(){
        spawnDirectedFireBall();
        spawnFireBallsBasedOnY();
        allFireBallCollisionAndMovement();
    }

    private void updateDirectedFireBall(){
        if (directedFireball != null){
            float speed = (directedFireball.getMovementSpeed() + globalFireBallMovementSpeed) * Gdx.graphics.getDeltaTime();
            directedFireball.hitBox.y -= speed;
            if (directedFireball.hitBox.y + FireBall.height < 0 || directedFireball.collided()){
                directedFireball = null;
            }
        }
    }

    private void updatePlayerScore(){
        if (TimeUtils.nanoTime() - scoreStartTime > TimeUtils.millisToNanos(2080 - globalFireBallMovementSpeed)){
            if (player.getScore() >= 999990){
                gameState = State.GAMEWON;
            } else {
                if (player.isStarPickupActive()){
                    long timeDif = TimeUtils.nanoTime() - player.getPlayerStarActiveTime();
                    myLog.debug("Time since Star pickup: " + timeDif);
                    if (timeDif > TimeUtils.millisToNanos(5000)){
                        player.setStarPickupActive(false);
                    }
                    player.setScore(100);
                } else {
                    player.setScore();
                }
            }
            scoreStartTime = TimeUtils.nanoTime();
        }
    }

    private void updateLifePickup(){
        spawnLifePickup();
        if (lifePickup != null){
            if (TimeUtils.nanoTime() - lifePickupStartTime > TimeUtils.millisToNanos(3750)){
                lifePickup.setQuarterLifeReached(true);
            }
            if (TimeUtils.nanoTime() - lifePickupStartTime > TimeUtils.millisToNanos(5000)){
                lifePickup = null;
            }
        }
    }

    private void updateStarPickup(){
        spawnStarPickup();
        if (starPickup != null){
            if (TimeUtils.nanoTime() - starPickupStartTime > TimeUtils.millisToNanos(5000)){
                starPickup = null;
            }
        }
    }

    // Object Collisions
    private void checkCollisions(){
        pickupCollisionDetection();
        directedFireballCollisionDetection();
    }

    private void directedFireballCollisionDetection(){
        if (directedFireball != null){
            if (player.hitBox.overlaps(directedFireball.hitBox)){
                directedFireball.setCollision(true);
                player.hitEnemy();
            }
        }
    }

    private void fireBallsCollisionDetection(FireBall fireBall){
        if (player.hitBox.overlaps(fireBall.hitBox)){
            fireBall.setCollision(true);
            player.hitEnemy();
            globalFireBallMovementSpeed = BlueSky.GAME_HEIGHT/8;
            fireBallSpawnDistanceY = 140;
        }
    }

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
                player.setPlayerStarActiveTime();
                starPickup = null;
            }
        }
    }

    private void allFireBallCollisionAndMovement(){
        Iterator<FireBall> iter = fireBalls.iterator();
        while(iter.hasNext()) {
            FireBall fireBall = iter.next();
            fireBallsCollisionDetection(fireBall);

            if (fireBall.collided()){
                iter.remove();
            }

            fireBall.hitBox.y -= globalFireBallMovementSpeed * Gdx.graphics.getDeltaTime();

            if(fireBall.hitBox.y + FireBall.height < 0){
                iter.remove();
            }
        }
        allFireBallSpeedAndSpawnDistance();
    }

    private void allFireBallSpeedAndSpawnDistance(){
        if (TimeUtils.nanoTime() - startTime > 500000000){
            startTime = TimeUtils.nanoTime();
            if (globalFireBallMovementSpeed < gh){
                globalFireBallMovementSpeed += 10;
                if (fireBallSpawnDistanceY > FireBall.height*3){
                    fireBallSpawnDistanceY -= 4;
                }
                myLog.debug("Distance between Fireballs y: " + fireBallSpawnDistanceY);
            }
            myLog.debug("Fireball movement speed y: " + globalFireBallMovementSpeed);
        }
    }

    // Object Spawns
    private void spawnLifePickup(){
        if (lifePickup == null){
            int x = MathUtils.random(0, gw - LifePickup.width);
            int y = MathUtils.random(gh - gh/2, gh - (gh/10 + LifePickup.height + 2));
            if (player.getScore() - lifePickupLastSpawnScore >= 1000){
                lifePickup = new LifePickup(x, y);
                lifePickupStartTime = TimeUtils.nanoTime();
                lifePickupLastSpawnScore = player.getScore();
            }
        }
    }

    private void spawnStarPickup(){
        if (starPickup == null){
            int x = MathUtils.random(0, gw - LifePickup.width);
            int y = MathUtils.random(gh - gh/2, gh - (gh/10 + LifePickup.height + 2));
            if(TimeUtils.nanoTime() - starPickupStartTime > TimeUtils.millisToNanos(20000)){
                starPickup = new StarPickup(x, y);
                starPickupStartTime = TimeUtils.nanoTime();
            }

        }
    }

    private void spawnFireBallsBasedOnY(){
        if (fireBalls != null && fireBalls.size != 0) {
            FireBall lastFireball = fireBalls.get(fireBalls.size - 1);
            if (lastFireball.hitBox.y <= BlueSky.GAME_HEIGHT - fireBallSpawnDistanceY){
                myLog.debug("last fireball y: " + lastFireball.hitBox.getY());
                if (fireBalls != null && fireBalls.size > 1){
                    myLog.debug("second to lase last fireball y: " + fireBalls.get(fireBalls.size - 2).hitBox.getY());
                }
                spawnFireBall(lastFireball);
            }
        } else {
            spawnFireBall();
        }
    }

    private void spawnFireBall(){
        int x = MathUtils.random(FireBall.textureWidth, BlueSky.GAME_WIDTH - FireBall.textureWidth);
        int y = BlueSky.GAME_HEIGHT;
        FireBall fireBall = new FireBall(x, y);
        fireBalls.add(fireBall);
    }

    private void spawnFireBall(FireBall lastFireball){
        int x;
        int y;

        myLog.debug("last fireball x: " + lastFireball.hitBox.x);

        if (lastFireball.hitBox.x < BlueSky.GAME_WIDTH / 2) {
            x = MathUtils.random(BlueSky.GAME_WIDTH / 2, BlueSky.GAME_WIDTH - FireBall.textureWidth);
        } else {
            x = MathUtils.random(FireBall.textureWidth, BlueSky.GAME_WIDTH / 2);
        }

        myLog.debug("Spawning fireball at x: " + x);

        y = BlueSky.GAME_HEIGHT;
        FireBall fireBall = new FireBall(x, y);
        fireBalls.add(fireBall);
    }

    private void spawnDirectedFireBall(){
        if (directedFireball == null){
            if (TimeUtils.nanoTime() - directedFireBallStartTime > TimeUtils.millisToNanos(2000)){
                int x = (int)player.hitBox.x;
                int y = BlueSky.GAME_HEIGHT;
                directedFireball = new FireBall(x,y,gh/2);
                directedFireBallStartTime = TimeUtils.nanoTime();
            }
        }
    }

    //Game states
    private void checkGameState(){
        checkGameWon();
        checkGameOver();
        checkGamePause();
        checkGameResume();
    }

    private void checkGameWon(){
        if (gameState == State.GAMEWON){
            // Win screen
        }
    }

    private void checkGameOver(){
        if (player.getLife() <= 0){
            myLog.info("Game Over");
            gameOverMenu = new GameOverMenu(myGame, player);
            gameState = State.GAMEOVER;
            //return;
        }
    }

    private void checkGamePause(){
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            myLog.info("Escape button pressed on keyboard");
            pause();
            //return;
        }
    }

    private void checkGameResume(){

    }
}

