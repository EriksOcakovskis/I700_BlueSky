package game;

/**
 * Created by eriks on 20/11/2016.
 */
public class LifePickup extends BaseActor {
    public static final int WIDTH = BlueSky.GAME_WIDTH/10;
    public static final int HEIGHT = BlueSky.GAME_WIDTH/10;

    private boolean quarterLifeReached;

    public LifePickup(int x, int y){
        super(x, y, WIDTH, HEIGHT);
        this.quarterLifeReached = false;
    }

    public boolean isQuarterLifeReached() {
        return quarterLifeReached;
    }

    public void setQuarterLifeReached(boolean quarterLifeReached) {
        this.quarterLifeReached = quarterLifeReached;
    }
}
