package game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.MenuScreens.MainMenu;

/**
 * Created by eriks on 26/10/2016.
 */

/**
 * Games entry point.
 */
public class BlueSky extends Game {

    // Game world size in arbitrary scale, these are not pixels
    public static final int GAME_WIDTH = 640;
    public static final int GAME_HEIGHT = 640;

    // Game version for global use
    public static final double VERSION = 0.2;

    SpriteBatch batch;

    private static SimpleLogger myLog = SimpleLogger.getLogger();

    /**
     * Analogue to instantiation. Loads all the assets to the memory. Creates a {@link SimpleLogger} and {@link SpriteBatch}.
     */
    public void create(){
        batch = new SpriteBatch();

        // Pre-loading all the assets for the game
        Assets.load();

        myLog.setLogFile("BlueSky");
        myLog.setLogLevel(SimpleLogger.DEBUG);
        myLog.info("Starting...");
        this.setScreen(new MainMenu(this));
    }

    /**
     * Renders a {@link Screen}
     */
    public void render(){
        super.render();
    }

    /**
     * Disposes of all teh assets that are not collected by garbage collector. This method runs automatically.
     */
    public void dispose(){
        batch.dispose();
        Assets.dispose();
    }
}
