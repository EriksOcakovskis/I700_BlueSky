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

        player = new Player(((int)(gw/2)), (Player.height));

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
        myGame.batch.draw(Assets.backgroundImage, 0, 0 , gw, gh);
        myGame.batch.draw(Assets.playerImage, player.hitBox.x, player.hitBox.y, gw/10, gh/20);
        for(FireBall fireBall: fireBalls) {
            myGame.batch.draw(Assets.fireBallImage, fireBall.hitBox.getX(), fireBall.hitBox.getY());
        }
        Assets.font.draw(myGame.batch, Integer.toString(player.life), 16, gh - 16);
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
        if (fireBalls != null && fireBalls.size != 0) {
            FireBall lastFireball = fireBalls.get(fireBalls.size - 1);
            if (lastFireball.hitBox.getY() <= (int)BlueSky.GAME_HEIGHT - FireBall.spawnHeight){
                myLog.debug("last fireball y: " + lastFireball.hitBox.getY());
                if (fireBalls != null && fireBalls.size > 1){
                    myLog.debug("second to lase last fireball y: " + fireBalls.get(fireBalls.size - 2).hitBox.getY());
                }
                spawnFireBall();
            }
        } else {
            spawnFireBall();
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
                    if (FireBall.movementSpeed < 400){
                        FireBall.movementSpeed += 20;
                        FireBall.spawnHeight -= 5;
                    }
                }
            }
            fireBall.hitBox.y -= FireBall.movementSpeed * Gdx.graphics.getDeltaTime();
            if(fireBall.hitBox.y + FireBall.height < 0) iter.remove();
        }
    }


    public void spawnFireBall(){
        int x = (int)MathUtils.random(0, BlueSky.GAME_WIDTH - FireBall.height);
        int y = (int)BlueSky.GAME_HEIGHT;
        FireBall fireBall = new FireBall(x, y);
        fireBalls.add(fireBall);
    }

    public boolean collisionDetection(FireBall fireBall){
        boolean result = false;
        if (player.hitBox.overlaps(fireBall.hitBox)){
            result = true;
        }
        return result;
    }


}
