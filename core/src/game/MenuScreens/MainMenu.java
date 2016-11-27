package game.MenuScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import game.BlueSky;
import game.MainGame;
import game.SimpleLogger;

/**
 * Created by eriks on 26/10/2016.
 */

/**
 * Implements {@link Screen}
 */
public class MainMenu implements Screen {
    private final BlueSky myGame;
    private OrthographicCamera camera;
    private Skin skin;
    private Stage stage;

    private static SimpleLogger myLog = SimpleLogger.getLogger();

    /**
     * Instantiation method, when it is called all assets are allocated in memory.
     * @param g Instance of {@link BlueSky} class
     */
    public MainMenu(final BlueSky g) {
        myGame = g;
        camera = new OrthographicCamera();
        createUi();
    }

    @Override
    public void render(float delta) {
        // Clear the screen and set background color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            myLog.info("Enter button pressed on keyboard");
            myGame.setScreen(new MainGame(myGame));
            dispose();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            myLog.info("Escape button pressed on keyboard");
            myLog.info("Exiting...");
            Gdx.app.exit();
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        skin.dispose();
    }

    /**
     * Loads all the assets for the menu and sets their position in the game world.
     */
    private void createUi(){
        myLog.info("Creating Main menu");
        Table table;
        Texture background;

        skin = new Skin();
        table = new Table();
        stage = new Stage(new FitViewport(BlueSky.GAME_WIDTH, BlueSky.GAME_HEIGHT, camera));
        Gdx.input.setInputProcessor(stage);

        background = new Texture(Gdx.files.internal("background.png"));

        skin.add("background", background);

        skin.add("Font64", new BitmapFont(Gdx.files.internal("fonts/font_64.fnt"),
                Gdx.files.internal("fonts/font_64.png"), false)
        );
        skin.add("Font64B", new BitmapFont(Gdx.files.internal("fonts/font_64_b.fnt"),
                Gdx.files.internal("fonts/font_64_b.png"), false)
        );

        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = skin.getFont("Font64");
        textButtonStyle.fontColor = Color.BLACK;
        textButtonStyle.overFontColor = Color.WHITE;

        Label.LabelStyle LabelStyle = new Label.LabelStyle();
        LabelStyle.font = skin.getFont("Font64B");

        skin.add("default", textButtonStyle);
        skin.add("default", LabelStyle);

        table.setFillParent(true);
        table.align(Align.center|Align.bottom);

        table.setBackground(skin.getDrawable("background"));

        Label gameInfo = new Label("", skin);
        gameInfo.setText("V" + BlueSky.VERSION + " Eriks Ocakovskis C11");
        gameInfo.setFontScale(0.5f);

        Label musicCredit = new Label("", skin);
        musicCredit.setText("Music by: \"Shinray\"\n\"OverClocked ReMix (http://ocremix.org)\"");
        musicCredit.setFontScale(0.3f);

        final TextButton newGameButton = new TextButton("New Game", skin);
        final TextButton exitButton = new TextButton("Exit", skin);

        newGameButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor){
                myLog.info("New Game UI button pressed");
                myGame.setScreen(new MainGame(myGame));
                dispose();
            }
        });

        exitButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor){
                myLog.info("Exit UI button pressed");
                myLog.info("Exiting...");
                Gdx.app.exit();
                dispose();
            }
        });

        table.add(newGameButton);
        table.row();
        table.add(exitButton).padTop(10);
        table.row();
        table.add(gameInfo).padTop(BlueSky.GAME_HEIGHT/2.6f);
        table.row();
        table.add(musicCredit).padTop(10);
        stage.addActor(table);
    }
}
