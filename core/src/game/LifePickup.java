package game;

/**
 * Created by eriks on 20/11/2016.
 */

/**
 * Allows life pickup actor creation.
 */
public class LifePickup extends BaseActor {
    public static final int WIDTH = BlueSky.GAME_WIDTH/10;
    public static final int HEIGHT = BlueSky.GAME_WIDTH/10;

    private boolean quarterLifeReached;

    /**
     * Creates a life pickup
     * @param x life pickup x coordinates
     * @param y life pickup y coordinates
     */
    public LifePickup(int x, int y){
        super(x, y, WIDTH, HEIGHT);
        this.quarterLifeReached = false;
    }

    /**
     * Returns if life pickup has lived its quarter life
     * @return {@link LifePickup#quarterLifeReached}
     */
    public boolean isQuarterLifeReached() {
        return quarterLifeReached;
    }

    /**
     * Sets {@link LifePickup#quarterLifeReached}
     * @param quarterLifeReached {@code true} or {@code false}
     */
    public void setQuarterLifeReached(boolean quarterLifeReached) {
        this.quarterLifeReached = quarterLifeReached;
    }
}
