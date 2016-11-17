package game;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by eriks on 13/11/2016.
 */
public class BasicActor {
    public final Rectangle hitBox;

    BasicActor(int x, int y, int width, int height){
        this.hitBox = new Rectangle(x, y, width, height);
    }
}