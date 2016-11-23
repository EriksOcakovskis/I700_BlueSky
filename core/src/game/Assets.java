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
    static Texture uiBackgroundImage;
    static Texture bombPickupImage;
    static Texture lifePickupImage;
    static Texture quarterLifePickupImage;
    static Texture starPickupImage;
    static Texture bombUiImage;
    static Texture lifeUiImage;
    static Texture bossImage;
    static Texture playerImage;
    static Texture fireBallImage;
    static Music backgroundMusic;
    static BitmapFont font64b;
    static BitmapFont font64w;

    private static Texture loadTexture(String pathToFile){
        return new Texture(Gdx.files.internal(pathToFile));
    }

    private static Music loadMusic(String pathToFile){
        return Gdx.audio.newMusic(Gdx.files.internal(pathToFile));
    }

    static void load(){
        backgroundImage = loadTexture("background.png");
        uiBackgroundImage = loadTexture("ui.png");
        bombPickupImage = loadTexture("bomb.png");
        lifePickupImage = loadTexture("heart.png");
        quarterLifePickupImage = loadTexture("heart_quarter.png");
        starPickupImage = loadTexture("star.png");
        bombUiImage = loadTexture("bomb.png");
        lifeUiImage = loadTexture("life.png");
        bossImage = loadTexture("boss.png");
        playerImage = loadTexture("plane.png");
        fireBallImage = loadTexture("fireball.png");
        backgroundMusic = loadMusic("undertreeinrain.mp3");
        font64b = new BitmapFont(Gdx.files.internal("fonts/font_64_b.fnt"), Gdx.files.internal("fonts/font_64_b.png"), false);
        font64w = new BitmapFont(Gdx.files.internal("fonts/font_64.fnt"), Gdx.files.internal("fonts/font_64.png"), false);
//        font64b.setColor(Color.BLACK);

        // Loop the background music from beginning
        backgroundMusic.setLooping(true);
    }

    static void dispose(){
        backgroundImage.dispose();
        uiBackgroundImage.dispose();
        bombPickupImage.dispose();
        lifePickupImage.dispose();
        quarterLifePickupImage.dispose();
        starPickupImage.dispose();
        bombUiImage.dispose();
        lifeUiImage.dispose();
        bossImage.dispose();
        playerImage.dispose();
        fireBallImage.dispose();
        backgroundMusic.dispose();
        font64b.dispose();
        font64w.dispose();
    }
}
