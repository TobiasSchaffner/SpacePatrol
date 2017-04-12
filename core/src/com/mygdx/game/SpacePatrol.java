package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.client.assets.AnimationHandler;
import com.mygdx.game.client.assets.Atlas;
import com.mygdx.game.client.user.User;
import com.mygdx.game.settings.GameSettings;
import com.mygdx.game.shared.space.Space;

/**
 * Created by platypus on 15.04.16.
 */
public class SpacePatrol extends Game{

    public SpriteBatch batch;
    public Stage stage;
    private BitmapFont font;

    private Atlas atlas;
    private AnimationHandler animationHandler;
    private User user;
    private Space space;

    public void create(){
        batch = new SpriteBatch();
        stage = new Stage();
        font = new BitmapFont();
        atlas = new Atlas();
        animationHandler = new AnimationHandler(atlas);
        space = new Space(GameSettings.SPACE_WIDTH, GameSettings.SPACE_HEIGHT);
        this.setScreen(new MainMenuScreen(this));
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public final void render(){
        super.render(); //important!
    }

    public void dispose(){
        batch.dispose();
        stage.dispose();
        font.dispose();
    }

    public AnimationHandler getAnimationHandler(){
        return animationHandler;
    }

    public Atlas getAtlas(){
        return atlas;
    }

    public User getUser(){
        return user;
    }

    void setUser(User user){
        assert user != null;
        this.user = user;
    }

    public Space getSpace(){
        return space;
    }
}
