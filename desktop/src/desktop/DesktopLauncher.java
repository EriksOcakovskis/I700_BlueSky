package desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import game.BlueSky;

public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.fullscreen = false;
        config.title = "Blue Sky";
        config.width = 1440;
        config.height = 900;
        config.forceExit = true;
        new LwjglApplication(new BlueSky(), config);
    }
}
