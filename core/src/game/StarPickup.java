package game;

/**
 * Created by eriks on 22/11/2016.
 */

/**
 * Allows star pickup actor creation.
 */
public class StarPickup extends BaseActor {
    static final int width = BlueSky.GAME_WIDTH/10;
    static final int height = BlueSky.GAME_WIDTH/10;

    /**
     * Creates star pickup.
     * @param x star pickup x coordinates
     * @param y star pickup y coordinates
     */
    public StarPickup(int x, int y){
        super(x, y, width, height);
    }
}
