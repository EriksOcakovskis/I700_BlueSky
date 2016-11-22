package game;

/**
 * Created by eriks on 22/11/2016.
 */
class StarPickup extends BasicActor {
    private boolean active;

    static final int width = BlueSky.GAME_WIDTH/10;
    static final int height = BlueSky.GAME_WIDTH/10;


    StarPickup(int x, int y){
        super(x, y, width, height);
        this.active = false;
    }

    boolean isActive() {
        return active;
    }

    void setActive(boolean active) {
        this.active = active;
    }
}
