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

class MainGame implements Screen {
    final BlueSky myGame;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Player player;
    private Array<FireBall> fireBalls;
    private long startTime;
    private float gw;
    private float gh;
    private static SimpleLogger myLog;

    MainGame(final BlueSky game) {
        myGame = game;
        myLog = SimpleLogger.getLogger();

        gw = BlueSky.GAME_WIDTH;
        gh = BlueSky.GAME_HEIGHT;

        camera = new OrthographicCamera();
        viewport = new FitViewport(gw, gh, camera);

        viewport.apply();

        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2,0);

        player = new Player(((int)(gw/2)), (Player.textureHeight / 2));
        myLog.debug("Player hit box x location: " + player.hitBox.x );
        myLog.debug("Player hit box y location: " + player.hitBox.y );

        fireBalls = new Array<FireBall>();

        startTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        updateFireballs();
        player.update(delta);
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

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        myGame.batch.dispose();
        Assets.dispose();
    }

    private void updateFireballs(){
        if (fireBalls != null && fireBalls.size != 0) {
            FireBall lastFireball = fireBalls.get(fireBalls.size - 1);
            if (lastFireball.hitBox.getY() <= (int)BlueSky.GAME_HEIGHT - FireBall.spawnHeight){
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
            collisionDetection(fireBall);
            if (fireBall.collided()){
                fireBall.hitPlayer();
                player.hitEnemy();
                iter.remove();
            } else {
                if (TimeUtils.nanoTime() - startTime > 500000000){
                    startTime = TimeUtils.nanoTime();
                    if (FireBall.movementSpeed < 600){
                        FireBall.movementSpeed += 10;
                        if (FireBall.spawnHeight > 65){
                            FireBall.spawnHeight -= 5;
                        }
                        myLog.debug("Fireball movement speed y: " + FireBall.movementSpeed);
                        myLog.debug("Distance between Fireballs y: " + FireBall.spawnHeight);
                    }
                }
            }
            fireBall.hitBox.y -= FireBall.movementSpeed * Gdx.graphics.getDeltaTime();
            if(fireBall.hitBox.y + FireBall.height < 0) iter.remove();
        }
    }


    private void spawnFireBall(){
        int x = (int)MathUtils.random(0, BlueSky.GAME_WIDTH - FireBall.width);
        int y = (int)BlueSky.GAME_HEIGHT;
        FireBall fireBall = new FireBall(x, y);
        fireBalls.add(fireBall);
    }

    private void spawnFireBall(FireBall lastFireball){
        int x;
        int y;

        myLog.debug("last fireball x: " + lastFireball.hitBox.x);

        if (lastFireball.hitBox.x < BlueSky.GAME_WIDTH / 2) {
            x = (int)MathUtils.random(BlueSky.GAME_WIDTH / 2, BlueSky.GAME_WIDTH - FireBall.textureWidth);
        } else {
            x = (int)MathUtils.random(FireBall.textureWidth, BlueSky.GAME_WIDTH / 2);
        }

        myLog.debug("Spawning fireball at x: " + x);

        y = (int)BlueSky.GAME_HEIGHT;
        FireBall fireBall = new FireBall(x, y);
        fireBalls.add(fireBall);
    }

    private void collisionDetection(FireBall fireBall){
        if (player.hitBox.overlaps(fireBall.hitBox)){
            fireBall.setCollision(true);
        }
    }

    private void draw(){
        // Clear the screen and set background color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        myGame.batch.setProjectionMatrix(camera.combined);

        myGame.batch.begin();
        myGame.batch.draw(Assets.backgroundImage, 0, 0 , gw, gh);

        float playerBatchX = player.hitBox.x - ((Player.textureWidth - Player.width) / 2);
        float playerBatchY = player.hitBox.y - ((Player.textureHeight - Player.height) / 2);
        myGame.batch.draw(
                Assets.playerImage, playerBatchX, playerBatchY,
                Player.textureWidth, Player.textureHeight
        );

        for(FireBall fireBall: fireBalls) {
            myGame.batch.draw(
                    Assets.fireBallImage, fireBall.hitBox.x - 2 , fireBall.hitBox.y,
                    FireBall.textureWidth, FireBall.textureHeight
            );
        }
        Assets.font.draw(myGame.batch, Integer.toString(player.life), 16, gh - 16);
        myGame.batch.end();
    }


}
