package game.MenuScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import game.BlueSky;
import game.MainGame;
import game.SimpleLogger;

/**
 * Created by eriks on 21/11/2016.
 */
public class PauseMenu {
    private OrthographicCamera camera;
    private Stage stage;
    private static SimpleLogger myLog = SimpleLogger.getLogger();
    private static PauseMenu pauseMenu = null;

    private PauseMenu() {
        camera = new OrthographicCamera();
        createUi();
    }

    public static PauseMenu getPauseMenue(){
        if (pauseMenu == null){
            pauseMenu = new PauseMenu();
        }
        return pauseMenu;
    }

    public void render(float delta) {
        camera.update();
        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            myLog.info("Enter button pressed on keyboard");
            MainGame.gameState = MainGame.State.RUNNING;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Q)){
            myLog.info("q button pressed on keyboard");
            myLog.info("Exiting...");
            Gdx.app.exit();
        }
    }

    private void createUi(){
        Skin skin;
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

        skin.add("default", textButtonStyle);

        table.setFillParent(true);
        table.align(Align.center|Align.bottom);

        final TextButton newGameButton = new TextButton("Continue", skin);
        final TextButton exitButton = new TextButton("Quit", skin);

        newGameButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor){
                myLog.info("Continue UI button pressed");
                MainGame.gameState = MainGame.State.RUNNING;
            }
        });

        exitButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor){
                myLog.info("Quit UI button pressed");
                myLog.info("Exiting...");
                Gdx.app.exit();
            }
        });

        table.add(newGameButton);
        table.row();
        table.add(exitButton).padTop(10).padBottom(BlueSky.GAME_HEIGHT/2f);
        stage.addActor(table);
    }
}
