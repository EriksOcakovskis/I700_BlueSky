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
    private Array<FireBall> fireBalls;
    private long lastFireBallSpawnTime;
    private long startTime;

    public MainGame(final BlueSky g) {
        myGame = g;

        float gw = myGame.GAME_WIDTH;
        float gh = myGame.GAME_HEIGHT;

        camera = new OrthographicCamera();
        viewport = new FitViewport(gw, gh, camera);

        viewport.apply();

        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2,0);

        // Creating player
        player = new Player(((int)(gw/2)), (Player.textureHeight));

        // Fireball array
        fireBalls = new Array<FireBall>();

        startTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        // Clear the screen and set background color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateFireballs();
        player.update(delta);

        // Update camera once per frame
        camera.update();

        // Tell batch to use coordinate system of camera
        myGame.batch.setProjectionMatrix(camera.combined);

        myGame.batch.begin();
        myGame.batch.draw(Assets.backgroundImage, 0, 0 , myGame.GAME_WIDTH, myGame.GAME_HEIGHT);
        myGame.batch.draw(Assets.playerImage, player.hitBox.x, player.hitBox.y, myGame.GAME_WIDTH/10, myGame.GAME_HEIGHT/20);
        for(FireBall fireBall: fireBalls) {
            myGame.batch.draw(Assets.fireBallImage, fireBall.hitBox.getX(), fireBall.hitBox.getY());
        }
        Assets.font.draw(myGame.batch, Integer.toString(player.life), 16, myGame.GAME_HEIGHT - 16);
        myGame.batch.end();

        // Player movement
    }
//    Structure of code

//    void gameLoop(){
//        // game logic
//    }
//    void inputloop{
//        // player inpyt
          // it will go to actor
//    }
//    void physicsLoop(){
//        // rain and such
//        // moving an object
//    }
//    void renderLoop(){
//        // draw shit
//    }

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
        if (TimeUtils.nanoTime() - lastFireBallSpawnTime > 500000000) {
            for (FireBall fB:fireBalls) {
                fB.hitBox.getX();
            }
            int x = (int)MathUtils.random(0, myGame.GAME_WIDTH - 16);
            int y = (int)myGame.GAME_HEIGHT;
            FireBall fireBall = new FireBall(x, y);
            fireBalls.add(fireBall);
            lastFireBallSpawnTime = TimeUtils.nanoTime();
        }

        Iterator<FireBall> iter = fireBalls.iterator();
        while(iter.hasNext()) {
            FireBall fireBall = iter.next();
            if (collisionDetection(fireBall)){
                fireBall.hitPlayer();
                player.hitEnemy();
                iter.remove();
            } else {
                if (TimeUtils.nanoTime() - startTime > 500000000){
                    startTime = TimeUtils.nanoTime();
                    fireBall.setMovementSpeed(fireBall.getMovementSpeed() + 20);
                }
            }
            fireBall.hitBox.y -= fireBall.getMovementSpeed() * Gdx.graphics.getDeltaTime();
            if(fireBall.hitBox.y + 16 < 0) iter.remove();
        }
    }


    public void spawnFireBall(){
        int x = (int)MathUtils.random(0, myGame.GAME_WIDTH - 16);
        int y = (int)myGame.GAME_HEIGHT;
        FireBall fireBall = new FireBall(x, y);
        fireBalls.add(fireBall);
        lastFireBallSpawnTime = TimeUtils.nanoTime();
    }

    public boolean collisionDetection(FireBall fireBall){
        boolean result = false;
        if (player.hitBox.overlaps(fireBall.hitBox)){
            result = true;
        }
        return result;
    }


}
