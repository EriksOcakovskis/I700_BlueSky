package game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Iterator;

public class MainGame implements Screen {
    final BlueSky myGame;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Player player;
    public static LifePickup lifePickup;
//    private Array<LifePickup> lifePickups;
    private long lifePickupLastSpawnScore;
    private FireBall directedFireball;
    private Array<FireBall> fireBalls;
    private long startTime;
    private long scoreStartTime;
    private long directedFireBallStartTime;
    private long lifePickupStartTime;
    private int gw;
    private int gh;
    private static SimpleLogger myLog;
    private State state;

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

        state = State.RUNNING;

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
        //lifePickups = new Array<LifePickup>();

        startTime = TimeUtils.nanoTime();
        directedFireBallStartTime = startTime;
    }

    @Override
    public void render(float delta) {
        update(delta);
        checkCollisions();
        draw();
    }

    @Override
    public void show() {
        Assets.backgroundMusic.play();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2,0);
    }

    @Override
    public void pause() {
        state = State.PAUSE;
    }

    @Override
    public void resume() {
        state = State.PAUSE;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        myGame.batch.dispose();
        Assets.dispose();
    }

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
        Assets.font64.draw(myGame.batch, Integer.toString(player.getLife()), gw/10 + 6, gh - gh/40);

        // Draw bomb
        myGame.batch.draw(Assets.bombUiImage, gw/4, (gh + 2) - gh/10, gw/10, gh/10);
        Assets.font64.draw(myGame.batch, Integer.toString(player.getBombPickup()), gw/4 + gw/10 + 6, gh - gh/40);

        //Draw score
        Assets.font64.draw(myGame.batch, Long.toString(player.getScore()), gw/2 + 76, gh - gh/40);
        myGame.batch.end();
    }

    private void update(float delta){
        updateFireballs();
        updateDirectedFireBall();
        player.update(delta);
        updatePlayerScore();
        spawnLifePickup();
    }

    private void checkCollisions(){
        pickupCollisionDetection();
        directedFireballCollisionDetection();
    }

    private void updateFireballs(){
        spawnDirectedFireBall();
        if (fireBalls != null && fireBalls.size != 0) {
            FireBall lastFireball = fireBalls.get(fireBalls.size - 1);
            if (lastFireball.hitBox.y <= BlueSky.GAME_HEIGHT - FireBall.spawnDistanceY){
                myLog.debug("last fireball y: " + lastFireball.hitBox.getY());
                if (fireBalls != null && fireBalls.size > 1){
                    myLog.debug("second to lase last fireball y: " + fireBalls.get(fireBalls.size - 2).hitBox.getY());
                }
                spawnFireBall(lastFireball);
            }
        } else {
            spawnFireBall();
        }

        Iterator<FireBall> iter = fireBalls.iterator();
        while(iter.hasNext()) {
            FireBall fireBall = iter.next();
            fireBallsCollisionDetection(fireBall);
            if (fireBall.collided()){
                iter.remove();
            } else {
                if (TimeUtils.nanoTime() - startTime > 500000000){
                    startTime = TimeUtils.nanoTime();
                    if (FireBall.globalMovementSpeed < gh){
                        FireBall.globalMovementSpeed += 10;
                        if (FireBall.spawnDistanceY > FireBall.height*3){
                            FireBall.spawnDistanceY -= 4;
                        }
                        myLog.debug("Distance between Fireballs y: " + FireBall.spawnDistanceY);
                    }
                    myLog.debug("Fireball movement speed y: " + FireBall.globalMovementSpeed);
                }
            }
            fireBall.hitBox.y -= FireBall.globalMovementSpeed * Gdx.graphics.getDeltaTime();
            if(fireBall.hitBox.y + FireBall.height < 0) iter.remove();
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

    private void updateDirectedFireBall(){
        if (directedFireball != null){
            float speed = (directedFireball.getMovementSpeed() + FireBall.globalMovementSpeed) * Gdx.graphics.getDeltaTime();
            directedFireball.hitBox.y -= speed;
            if (directedFireball.hitBox.y + FireBall.height < 0 || directedFireball.collided()){
                directedFireball = null;
            }
        }
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
            fireBall.hitPlayer();
            player.hitEnemy();
        }
    }

    private void pickupCollisionDetection(){
        if (lifePickup != null){
            if (player.hitBox.overlaps(lifePickup.hitBox)){
                player.hitLifePickup();
                lifePickup = null;
            }
        }

    }

    private void updatePlayerScore(){
        if (TimeUtils.nanoTime() - scoreStartTime > TimeUtils.millisToNanos(2080 - FireBall.globalMovementSpeed)){
            if (player.getScore() >= 999990){
                state = State.GAMEWON;
            } else {
                player.setScore();
            }
            scoreStartTime = TimeUtils.nanoTime();
        }
    }

    private void spawnLifePickup(){
        if (lifePickup == null){
            //int playerScoreCheck = (int)(player.getScore() / 1000);
            int x = MathUtils.random(0, gw - LifePickup.width);
            int y = MathUtils.random(gh - gh/2, gh - (gh/10 + LifePickup.height + 2));
            //long temp = player.getScore() / (1000 * (LifePickup.timesSpawned + 1));
            if (player.getScore() - lifePickupLastSpawnScore >= 1000){
                lifePickup = new LifePickup(x, y);
                //lifePickups.add(lifePickup);
                lifePickupStartTime = TimeUtils.nanoTime();
                lifePickupLastSpawnScore = player.getScore();
            }
        } else {
            if (TimeUtils.nanoTime() - lifePickupStartTime > TimeUtils.millisToNanos(3750)){
                lifePickup.setQuarterLifeReached(true);
            }

            if (TimeUtils.nanoTime() - lifePickupStartTime > TimeUtils.millisToNanos(5000)){
                lifePickup = null;
            }
        }
    }

    private void checkGameState(){
        checkGameWon();
        checkGameOver();
        checkGamePause();
        checkGameResume();
    }

    private void checkGameWon(){
        if (state == State.GAMEWON){
            // Win screen
        }
    }

    private void checkGameOver(){
        if (player.getLife() <= 0){
            state = State.GAMEOVER;
        }
        if (state == State.GAMEOVER){
            // Game over screen
        }
    }

    private void checkGamePause(){

    }

    private void checkGameResume(){

    }
}
