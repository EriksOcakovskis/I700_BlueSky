package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by eriks on 26/10/2016.
 */
public class MainMenu implements Screen {
    final BlueSky myGame;
    OrthographicCamera camera;
    Viewport viewport;


    public MainMenu(final BlueSky g) {
        myGame = g;

        // Assign screen resolution for easy access
//        int w = Gdx.graphics.getWidth();
//        int h = Gdx.graphics.getHeight();
        //float aspectRatio = (float)h / (float)w;

        float gw = myGame.GAME_WIDTH;
        float gh = myGame.GAME_HEIGHT;

        camera = new OrthographicCamera();
        viewport = new FitViewport(gw, gh, camera);
        //camera.setToOrtho(false, gw, gh);

//        camera.position.set(gw, gh,0);
        viewport.apply();

        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2,0);

    }

    @Override
    public void render(float delta) {
        // Clear the screen and set background color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update camera once per frame
        camera.update();

        // Tell batch to use coordinate system of camera
        myGame.batch.setProjectionMatrix(camera.combined);

        myGame.batch.begin();

        myGame.batch.draw(Assets.backgroundImage, 0, 0 , myGame.GAME_WIDTH, myGame.GAME_HEIGHT);
        // Create game text with default font
        Assets.font.draw(myGame.batch, "Project BlueSky", 15, 150);
        Assets.font.draw(myGame.batch, "Press ENTER to begin!", 2, 130);
        myGame.batch.end();

        // When user presses any key, game will start
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            myGame.setScreen(new MainGame(myGame));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2,0);
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

}
