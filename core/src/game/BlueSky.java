package game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by eriks on 26/10/2016.
 */
public class BlueSky extends Game {
    static final float GAME_WIDTH = 320;
    static final float GAME_HEIGHT = 640;
    static final double VERSION = 0.1;
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
        Assets.font.dispose();
    }
}
