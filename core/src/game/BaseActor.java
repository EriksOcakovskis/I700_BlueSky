package game;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by eriks on 13/11/2016.
 */

/**
 * Base actor that will be extended by all other actors. It creates a {@link Rectangle} hit box for all actors.
 */
public class BaseActor {
    public final Rectangle hitBox;

    /**
     * Instantiating base actor.
     * @param x {@code int}, {@link Rectangle} x coordinates
     * @param y {@code int}, {@link Rectangle} y coordinates
     * @param width {@code int}, {@link Rectangle} width
     * @param height {@code int}, {@link Rectangle} height
     */
    public BaseActor(int x, int y, int width, int height){
        this.hitBox = new Rectangle(x, y, width, height);
    }
}
