package game;

import sun.applet.Main;

/**
 * Created by eriks on 20/11/2016.
 */
class LifePickup extends BasicActor {
    boolean collision;
    private boolean quarterLifeReached;

    static final int width = BlueSky.GAME_WIDTH/10;
    static final int height = BlueSky.GAME_WIDTH/10;


    LifePickup(int x, int y){
        super(x, y, width, height);
        this.collision = false;
        this.quarterLifeReached = false;
    }

    boolean isQuarterLifeReached() {
        return quarterLifeReached;
    }

    void setQuarterLifeReached(boolean quarterLifeReached) {
        this.quarterLifeReached = quarterLifeReached;
    }
}
