package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by eriks on 01/11/2016.
 */
public class Assets {
    public static Texture backgroundImage;
    public static Texture playerImage;
    public static Texture dropletImage;
    public static Texture fireBallImage;
    public static Music backgroundMusic;

    // Default Arial font
    public static BitmapFont font;

    public static Texture loadTextures(String pathToFile){
        return new Texture(Gdx.files.internal(pathToFile));
    }

    public static Music loadMusic(String pathToFile){
        return Gdx.audio.newMusic(Gdx.files.internal(pathToFile));
    }

    public static void load(){
        backgroundImage = loadTextures("background.png");
        playerImage = loadTextures("plane.png");
        dropletImage = loadTextures("rain.png");
        fireBallImage = loadTextures("fireball.png");
        backgroundMusic = loadMusic("undertreeinrain.mp3");
        font = new BitmapFont();//Gdx.files.internal("3Dventure.ttf"));
        //font.getData().setScale(0.35f);//0.75f / (Gdx.graphics.getWidth()/Gdx.graphics.getHeight()), 0.75f / (Gdx.graphics.getHeight()/Gdx.graphics.getWidth()));

        backgroundImage.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        // Loop the background music from beginning
        backgroundMusic.setLooping(true);


    }

    public static void dispose(){
        backgroundImage.dispose();
        playerImage.dispose();
        fireBallImage.dispose();
        dropletImage.dispose();
        backgroundMusic.dispose();
    }
}
