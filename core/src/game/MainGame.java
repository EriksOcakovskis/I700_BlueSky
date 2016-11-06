package game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Iterator;

public class MainGame implements Screen {
    final BlueSky myGame;

    private OrthographicCamera camera;
    Viewport viewport;
    private Rectangle player;
    private Array<Rectangle> raindrops;
    private long lastDropTime;

    public void movePlayerRight(int button, int altButton) {
        if (Gdx.input.isKeyPressed(button) || Gdx.input.isKeyPressed(altButton)) {
            player.x += 100 * Gdx.graphics.getDeltaTime();
            playerBoundaries();
        }
    }

    public void movePlayerLeft(int button, int altButton) {
        if (Gdx.input.isKeyPressed(button) || Gdx.input.isKeyPressed(altButton)) {
            player.x -= 100 * Gdx.graphics.getDeltaTime();
            playerBoundaries();
        }
    }

    public void playerBoundaries(){
        if(player.x < 0) player.x = 0;
        if(player.x > myGame.GAME_WIDTH - player.getWidth()) {
            player.x = myGame.GAME_WIDTH - player.getWidth();
        }
    }

    public void makeItRain(){
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, myGame.GAME_WIDTH - 7);
        raindrop.y = myGame.GAME_HEIGHT;
        raindrop.width = 7;
        raindrop.height = 11;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    public MainGame(final BlueSky g) {
        myGame = g;

        float gw = myGame.GAME_WIDTH;
        float gh = myGame.GAME_HEIGHT;

        camera = new OrthographicCamera();
        viewport = new FitViewport(gw, gh, camera);
        //camera.setToOrtho(false, gw, gh);

//        camera.position.set(gw, gh,0);
        viewport.apply();

        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2,0);
        //camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Creating player
        player = new Rectangle();
        player.y = player.height + gh / 20;
        player.x = gw/2;
        player.height = gh/10;
        player.width = gw/8;

        // Create rain
        raindrops = new Array<Rectangle>();
        makeItRain();
    }

    @Override
    public void render(float delta) {
        // Clear the screen and set background color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Spawn a raindrop every 0.01 second
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) {
            makeItRain();
        }

        // Make raindrop move 80px per second
        Iterator<Rectangle> iter = raindrops.iterator();
        while(iter.hasNext()) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 80 * Gdx.graphics.getDeltaTime();
            if(raindrop.y + 11 < 0) iter.remove();
        }

        // Update camera once per frame
        camera.update();

        // Tell batch to use coordinate system of camera
        myGame.batch.setProjectionMatrix(camera.combined);

        myGame.batch.begin();
        myGame.batch.draw(Assets.backgroundImage, 0, 0 , myGame.GAME_WIDTH, myGame.GAME_HEIGHT);
        myGame.batch.draw(Assets.idleStanceImage1, player.getX(), player.getY(), player.getWidth(), player.getHeight());
        for(Rectangle raindrop: raindrops) {
            myGame.batch.draw(Assets.dropletImage, raindrop.x, raindrop.y);
        }
        myGame.batch.end();

        // Player movement
        movePlayerRight(Input.Keys.D, Input.Keys.RIGHT);
        movePlayerLeft(Input.Keys.A, Input.Keys.LEFT);

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
}
