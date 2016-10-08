package desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import game.BlueSky;

public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.fullscreen = true;
        config.title = "Blue Sky";
        config.width = 1920;
        config.height = 1080;
        new LwjglApplication(new BlueSky(), config);
    }
}
