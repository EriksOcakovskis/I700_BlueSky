package game.MenuScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import game.BlueSky;
import game.MainGame;
import game.Player;
import game.SimpleLogger;

/**
 * Created by eriks on 23/11/2016.
 */

/**
 * Game Over menu, does not create a {@link Screen}, but just an overlay on top of {@link MainGame} screen.
 */
public class GameOverMenu {
    private final BlueSky myGame;

    private Player player;
    private OrthographicCamera camera;
    private Stage stage;
    private Skin skin;
    private static SimpleLogger myLog = SimpleLogger.getLogger();

    /**
     * Instantiation method, when it is called all assets are allocated in memory.
     * @param g Instance of {@link BlueSky} class
     * @param p instance of {@link Player} class
     */
    public GameOverMenu(final BlueSky g, Player p) {
        myGame = g;
        player = p;
        camera = new OrthographicCamera();
        createUi();
    }

    /**
     * Renders all the assets that are created by {@link GameOverMenu#createUi()} method.
     * @param delta frame delta time, should contain main renderer delta
     */
    public void render(float delta) {
        camera.update();
        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            myLog.info("Enter button pressed on keyboard");
            dispose();
            myGame.setScreen(new MainGame(myGame));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q)){
            myLog.info("q button pressed on keyboard");
            myLog.info("Exiting...");
            Gdx.app.exit();
        }
    }

    /**
     * Loads all the assets for the menu and sets their position in the game world.
     */
    private void createUi(){
        Table table;

        skin = new Skin();
        table = new Table();
        stage = new Stage(new FitViewport(BlueSky.GAME_WIDTH, BlueSky.GAME_HEIGHT, camera));
        Gdx.input.setInputProcessor(stage);

        skin.add("Font64", new BitmapFont(Gdx.files.internal("fonts/font_64.fnt"),
                Gdx.files.internal("fonts/font_64.png"), false)
        );

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("Font64");
        textButtonStyle.fontColor = Color.LIGHT_GRAY;
        textButtonStyle.overFontColor = Color.WHITE;

        Label.LabelStyle LabelStyle = new Label.LabelStyle();
        LabelStyle.font = skin.getFont("Font64");
        LabelStyle.fontColor = Color.GOLD;

        skin.add("default", textButtonStyle);
        skin.add("default", LabelStyle);

        table.setFillParent(true);
        table.align(Align.center|Align.bottom);

        Label gameScore = new Label("", skin);
        gameScore.setText("Your Score: " + player.getScore());
        final TextButton newGameButton = new TextButton("New Game", skin);
        final TextButton exitButton = new TextButton("Exit", skin);

        newGameButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor){
                myLog.info("New Game UI button pressed");
                dispose();
                myGame.setScreen(new MainGame(myGame));
            }
        });

        exitButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor){
                myLog.info("Exit UI button pressed");
                myLog.info("Exiting...");
                Gdx.app.exit();
            }
        });

        table.add(gameScore);
        table.row();
        table.add(newGameButton).padTop(20);
        table.row();
        table.add(exitButton).padTop(10).padBottom(BlueSky.GAME_HEIGHT/2f);
        stage.addActor(table);
    }

    /**
     * Disposes of all the assets that are not taken care of by garbage collector.
     */
    public void dispose(){
        stage.dispose();
        skin.dispose();
    }
}

