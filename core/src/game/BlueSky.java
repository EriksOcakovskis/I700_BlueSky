package game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by eriks on 26/10/2016.
 */
public class BlueSky extends Game {
    SpriteBatch batch;
    BitmapFont font;

    public void create(){
        batch = new SpriteBatch();
        // Default Arial font
        font =  new BitmapFont();
        this.setScreen(new MainMenu(this));
    }

    public void render(){
        super.render();
    }

    public void dispose(){
        batch.dispose();
        font.dispose();
    }

}
