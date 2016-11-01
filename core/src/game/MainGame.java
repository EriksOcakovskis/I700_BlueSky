package game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MainGame implements Screen {
    final BlueSky myGame;

    private OrthographicCamera camera;
    private Texture idleStanceImage1;
    private Texture backgroundImage;
    private Texture dropletImage;
    private Music backgroundMusic;
    private Rectangle player;
    private Array<Rectangle> raindrops;
    private long lastDropTime;

    public void movePlayerRight(int button, int altButton) {
        if (Gdx.input.isKeyPressed(button) || Gdx.input.isKeyPressed(altButton)) {
            player.x += 300 * Gdx.graphics.getDeltaTime();
            playerBoundaries();
        }
    }

    public void movePlayerLeft(int button, int altButton) {
        if (Gdx.input.isKeyPressed(button) || Gdx.input.isKeyPressed(altButton)) {
            player.x -= 300 * Gdx.graphics.getDeltaTime();
            playerBoundaries();
        }
    }

    public void playerBoundaries(){
        if(player.x < 0) player.x = 0;
        if(player.x > Gdx.graphics.getWidth() - player.getWidth()) {
            player.x = Gdx.graphics.getWidth() - player.getWidth();
        }
    }

    public void makeItRain(){
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, Gdx.graphics.getWidth() - 7);
        raindrop.y = Gdx.graphics.getHeight();
        raindrop.width = 7;
        raindrop.height = 11;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    public MainGame(final BlueSky g) {
        myGame = g;

        backgroundImage = new Texture(Gdx.files.internal("backdrop.png"));
        backgroundImage.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        dropletImage = new Texture(Gdx.files.internal("rain.png"));

        idleStanceImage1 = new Texture(Gdx.files.internal("stance1_1.png"));

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("undertreeinrain.mp3"));

        // Loop the background music from beginning
        backgroundMusic.setLooping(true);

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Creating player
        player = new Rectangle();
        player.y = 20;
        player.x = 100;
        player.height = 184;
        player.width = 72;

        // Create rain
        raindrops = new Array<Rectangle>();
        makeItRain();
    }

    @Override
    public void render(float delta) {
        // Clear the screen and set background color
        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Spawn a raindrop every 0.01 second
        if (TimeUtils.nanoTime() - lastDropTime > 10000000) {
            makeItRain();
        }

        // Make raindrop move 800px per second
        Iterator<Rectangle> iter = raindrops.iterator();
        while(iter.hasNext()) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 800 * Gdx.graphics.getDeltaTime();
            if(raindrop.y + 11 < 0) iter.remove();
        }

        // Update camera once per frame
        camera.update();

        // Tell batch to use coordinate system of camera
        myGame.batch.setProjectionMatrix(camera.combined);

        myGame.batch.begin();
        myGame.batch.draw(backgroundImage, 0, 0 , Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        myGame.batch.draw(idleStanceImage1, player.getX(), player.getY(), player.getWidth(), player.getHeight());
        for(Rectangle raindrop: raindrops) {
            myGame.batch.draw(dropletImage, raindrop.x, raindrop.y);
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
        backgroundMusic.play();
    }

    @Override
    public void resize(int width, int height) {

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
        idleStanceImage1.dispose();
        backgroundImage.dispose();
        backgroundMusic.dispose();
        dropletImage.dispose();
    }
}
