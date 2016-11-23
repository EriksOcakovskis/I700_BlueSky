package game;

/**
 * Created by eriks on 22/11/2016.
 */
class StarPickup extends BasicActor {
    static final int width = BlueSky.GAME_WIDTH/10;
    static final int height = BlueSky.GAME_WIDTH/10;


    StarPickup(int x, int y){
        super(x, y, width, height);
    }
}
