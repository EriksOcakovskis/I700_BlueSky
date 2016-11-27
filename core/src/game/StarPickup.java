package game;

/**
 * Created by eriks on 22/11/2016.
 */
public class StarPickup extends BaseActor {
    static final int width = BlueSky.GAME_WIDTH/10;
    static final int height = BlueSky.GAME_WIDTH/10;


    public StarPickup(int x, int y){
        super(x, y, width, height);
    }
}
