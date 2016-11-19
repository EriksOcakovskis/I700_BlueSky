package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by eriks on 01/11/2016.
 */
class Assets {
    static Texture backgroundImage;
    static Texture playerImage;
    static Texture fireBallImage;
    static Music backgroundMusic;
    static BitmapFont font;

    private static Texture loadTextures(String pathToFile){
        return new Texture(Gdx.files.internal(pathToFile));
    }

    private static Music loadMusic(String pathToFile){
        return Gdx.audio.newMusic(Gdx.files.internal(pathToFile));
    }

    static void load(){
        backgroundImage = loadTextures("background.png");
        playerImage = loadTextures("plane.png");
        fireBallImage = loadTextures("fireball.png");
        backgroundMusic = loadMusic("undertreeinrain.mp3");
        font = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);

        backgroundImage.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        // Loop the background music from beginning
        backgroundMusic.setLooping(true);


    }

    static void dispose(){
        backgroundImage.dispose();
        playerImage.dispose();
        fireBallImage.dispose();
        backgroundMusic.dispose();
    }
}
