package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Created by eriks on 26/10/2016.
 */
public class MainMenu implements Screen {
    final BlueSky myGame;
    OrthographicCamera camera;

    public MainMenu(final BlueSky g) {
        myGame = g;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        // Clear the screen and set background color
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update camera once per frame
        camera.update();

        // Tell batch to use coordinate system of camera
        myGame.batch.setProjectionMatrix(camera.combined);

        myGame.batch.begin();

        // Create game text with default font
        myGame.font.draw(myGame.batch, "Project BlueSky", 80, 120);
        myGame.font.draw(myGame.batch, "Press Enter to begin!", 80, 100);
        myGame.batch.end();

        // When user presses any key, game will start
        if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)){
            myGame.setScreen(new MainGame(myGame));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
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
