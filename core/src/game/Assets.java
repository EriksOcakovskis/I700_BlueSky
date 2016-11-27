package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by eriks on 01/11/2016.
 */

/**
 * Basic assets manager, keeps all the assets in one place and loads them as game logic requires.
 */
public class Assets {
    static Texture backgroundImage;
    static Texture uiBackgroundImage;
    static Texture pauseImage;
    static Texture damageImage;
    static Texture lifePickupImage;
    static Texture quarterLifePickupImage;
    static Texture starPickupImage;
    static Texture lifeUiImage;
    static Texture playerImage;
    static Texture fireBallImage;
    static Music backgroundMusic;
    static BitmapFont font64b;
    static BitmapFont font64w;

    /**
     * Creates a new {@link Texture}, since it is private no try statement needed.
     * @param fileName {@link String} that contains file name,
     *                   full path is not needed, game will check files only in assets directory
     * @return {@link Texture} is returned
     */
    private static Texture loadTexture(String fileName){
        return new Texture(Gdx.files.internal(fileName));
    }

    /**
     * Creates a new {@link Music}, since it is private no try statement needed.
     * @param fileName {@link String} that contains file name
     * @return {@link Music} is returned
     */
    private static Music loadMusic(String fileName){
        return Gdx.audio.newMusic(Gdx.files.internal(fileName));
    }

    /**
     * Loads all the assets for the game.
     */
    public static void load(){
        backgroundImage = loadTexture("background.png");
        uiBackgroundImage = loadTexture("ui.png");
        pauseImage = loadTexture("pause.png");
        damageImage = loadTexture("damage.png");
        lifePickupImage = loadTexture("heart.png");
        quarterLifePickupImage = loadTexture("heart_quarter.png");
        starPickupImage = loadTexture("star.png");
        lifeUiImage = loadTexture("life.png");
        playerImage = loadTexture("plane.png");
        fireBallImage = loadTexture("fireball.png");
        backgroundMusic = loadMusic("Golden_Axe_2_Ravaged_Metal_OC_ReMix.mp3");
        font64b = new BitmapFont(Gdx.files.internal("fonts/font_64_b.fnt"),
                Gdx.files.internal("fonts/font_64_b.png"), false);
        font64w = new BitmapFont(Gdx.files.internal("fonts/font_64.fnt"),
                Gdx.files.internal("fonts/font_64.png"), false);

        // Loop the background music from beginning
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f);
    }

    /**
     * Disposes of all the assets that are not handled by garbage collector.
     */
    public static void dispose(){
        backgroundImage.dispose();
        uiBackgroundImage.dispose();
        pauseImage.dispose();
        damageImage.dispose();
        lifePickupImage.dispose();
        quarterLifePickupImage.dispose();
        starPickupImage.dispose();
        lifeUiImage.dispose();
        playerImage.dispose();
        fireBallImage.dispose();
        backgroundMusic.dispose();
        font64b.dispose();
        font64w.dispose();
    }
}
