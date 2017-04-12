package com.mygdx.game.client.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.HashMap;
import java.util.Map;

/**
 * Holding all the Assets
 * Created by platypus on 10.04.16.
 */
public class Atlas{

    private final Map<Integer, PlayerAnimation> name2playerAnimation = new HashMap<>();

    private final Map<Integer, Texture> name2orbImage = new HashMap<>();

    private final Map<Integer, Texture> name2unitImage = new HashMap<>();

    private final Map<Integer, Texture> name2fireImage = new HashMap<>();

    private final Map<String, Texture> name2colorImage = new HashMap<>();

    private final Map<Integer, Music> name2fireMusic = new HashMap<>();


    private final Music music;

    private final BitmapFont font = new BitmapFont();


    public Atlas(){

        PlayerAnimation rocket = new PlayerAnimation();
        rocket.idle = new Texture[]{new Texture(Gdx.files.internal("android/assets/units/rocket/idle/rocket.png"))};
        rocket.moving = new Texture[]{new Texture(Gdx.files.internal("android/assets/units/rocket/moving/1_147x220.png")),new Texture(Gdx.files.internal("android/assets/units/rocket/moving/2_147x220.png")),new Texture(Gdx.files.internal("android/assets/units/rocket/moving/3_147x220.png")),new Texture(Gdx.files.internal("android/assets/units/rocket/moving/4_147x220.png")),new Texture(Gdx.files.internal("android/assets/units/rocket/moving/5_147x220.png")),new Texture(Gdx.files.internal("android/assets/units/rocket/moving/6_147x220.png")), new Texture(Gdx.files.internal("android/assets/units/rocket/moving/5_147x220.png")),new Texture(Gdx.files.internal("android/assets/units/rocket/moving/4_147x220.png")),new Texture(Gdx.files.internal("android/assets/units/rocket/moving/3_147x220.png")),new Texture(Gdx.files.internal("android/assets/units/rocket/moving/2_147x220.png"))};
        rocket.exploding = new Texture[]{new Texture(Gdx.files.internal("android/assets/units/rocket/exploding/r00.png")), new Texture(Gdx.files.internal("android/assets/units/rocket/exploding/r01.png")), new Texture(Gdx.files.internal("android/assets/units/rocket/exploding/r02.png")), new Texture(Gdx.files.internal("android/assets/units/rocket/exploding/r03.png")), new Texture(Gdx.files.internal("android/assets/units/rocket/exploding/r04.png")), new Texture(Gdx.files.internal("android/assets/units/rocket/exploding/r05.png")), new Texture(Gdx.files.internal("android/assets/units/rocket/exploding/r06.png")), new Texture(Gdx.files.internal("android/assets/units/rocket/exploding/r07.png"))};
        name2playerAnimation.put(1, rocket);

        PlayerAnimation ufo = new PlayerAnimation();
        ufo.idle = new Texture[]{new Texture(Gdx.files.internal("android/assets/units/fox.png"))};
        ufo.moving = new Texture[]{new Texture(Gdx.files.internal("android/assets/units/fox.png"))};
        ufo.exploding = new Texture[]{new Texture(Gdx.files.internal("android/assets/explosion/r00.png")), new Texture(Gdx.files.internal("android/assets/explosion/r01.png")), new Texture(Gdx.files.internal("android/assets/explosion/r02.png")), new Texture(Gdx.files.internal("android/assets/explosion/r03.png")), new Texture(Gdx.files.internal("android/assets/explosion/r04.png")), new Texture(Gdx.files.internal("android/assets/explosion/r05.png")), new Texture(Gdx.files.internal("android/assets/explosion/r06.png")), new Texture(Gdx.files.internal("android/assets/explosion/r07.png"))};
        name2playerAnimation.put(2, ufo);


        // Orbs
        name2orbImage.put(1, new Texture(Gdx.files.internal("android/assets/orbs/780226 - earth global globe planet wor.png")));
        name2orbImage.put(2, new Texture(Gdx.files.internal("android/assets/orbs/780227 - global moon planet space.png")));
        name2orbImage.put(3, new Texture(Gdx.files.internal("android/assets/orbs/780228 - global mercury planet space.png")));
        name2orbImage.put(4, new Texture(Gdx.files.internal("android/assets/orbs/780229 - global mars planet space.png")));
        name2orbImage.put(5, new Texture(Gdx.files.internal("android/assets/orbs/780230 - global planet space venus.png")));
        name2orbImage.put(6, new Texture(Gdx.files.internal("android/assets/orbs/780231 - global jupiter planet space.png")));
        name2orbImage.put(7, new Texture(Gdx.files.internal("android/assets/orbs/780232 - global planet space uranus.png")));
        name2orbImage.put(8, new Texture(Gdx.files.internal("android/assets/orbs/780233 - global planet saturn space.png")));
        name2orbImage.put(9, new Texture(Gdx.files.internal("android/assets/orbs/780234 - global planet pluto space.png")));
        name2orbImage.put(10, new Texture(Gdx.files.internal("android/assets/orbs/780235 - astronomy global neptune plan.png")));

        // Units
        name2unitImage.put(1, new Texture(Gdx.files.internal("android/assets/units/level1.png")));
        name2unitImage.put(2, new Texture(Gdx.files.internal("android/assets/units/level2.png")));
        name2unitImage.put(3, new Texture(Gdx.files.internal("android/assets/units/level3.png")));
        name2unitImage.put(4, new Texture(Gdx.files.internal("android/assets/units/level4.png")));

        // Fires
        name2fireImage.put(1, new Texture(Gdx.files.internal("android/assets/fire/laser.png")));
        name2fireImage.put(2, new Texture(Gdx.files.internal("android/assets/fire/fireball.png")));
        name2fireMusic.put(1, Gdx.audio.newMusic(Gdx.files.internal("android/assets/sounds/lazer.wav")));
        name2fireMusic.put(2, Gdx.audio.newMusic(Gdx.files.internal("android/assets/sounds/fire.wav")));

        // Colors
        name2colorImage.put("green", new Texture(Gdx.files.internal("android/assets/color/green.png")));
        name2colorImage.put("red", new Texture(Gdx.files.internal("android/assets/color/red.png")));
        name2colorImage.put("black", new Texture(Gdx.files.internal("android/assets/color/black.png")));

        // Music
        music = Gdx.audio.newMusic(Gdx.files.internal("android/assets/spacemusic.mp3"));

        // Fonts
        font.setColor(Color.WHITE);
    }

    public PlayerAnimation getPlayerAnimations(int code){
        return name2playerAnimation.get(code);
    }

    public Texture getFireImage(int code){
        return name2fireImage.get(code);
    }

    public Music getFireMusic(int code){
        return name2fireMusic.get(code);
    }

    public Texture getOrbImage(int code){
        return name2orbImage.get(code);
    }

    public Texture getColorImage(String code){
        return name2colorImage.get(code);
    }

    public Music getMusic(){
        return music;
    }

    public BitmapFont getFont(){
        return font;
    }

    /**
     * Always try to dispose everything.
     */
    public void disposeAll(){
        for(final Texture texture : name2orbImage.values()){
            texture.dispose();
        }
        for(final Texture texture : name2fireImage.values()){
            texture.dispose();
        }
        for(final Texture texture : name2unitImage.values()){
            texture.dispose();
        }
        for(final Texture texture : name2colorImage.values()){
            texture.dispose();
        }
        music.dispose();
        font.dispose();
    }

    static class PlayerAnimation{
        Texture idle[];
        Texture moving[];
        Texture exploding[];
    }
}
