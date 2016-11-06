package game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by eriks on 26/10/2016.
 */
public class BlueSky extends Game {
    public final static float GAME_WIDTH = 60;
    public final static float GAME_HEIGHT = 108;
    SpriteBatch batch;

    public void create(){
        batch = new SpriteBatch();
        Assets.load();
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
