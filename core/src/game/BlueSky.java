package game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.MenuScreens.MainMenu;

/**
 * Created by eriks on 26/10/2016.
 */
public class BlueSky extends Game {
    public static final int GAME_WIDTH = 640;
    public static final int GAME_HEIGHT = 640;
    public static final double VERSION = 0.2;
    SpriteBatch batch;

    private static SimpleLogger myLog = SimpleLogger.getLogger();

    public void create(){
        batch = new SpriteBatch();
        Assets.load();
        myLog.setLogFile("BlueSky");
        myLog.setLogLevel(SimpleLogger.DEBUG);
        myLog.info("Starting...");
        this.setScreen(new MainMenu(this));
    }

    public void render(){
        super.render();
    }

    public void dispose(){
        batch.dispose();
        Assets.dispose();
    }
}
