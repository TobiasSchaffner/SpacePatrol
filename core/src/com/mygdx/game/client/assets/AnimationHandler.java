package com.mygdx.game.client.assets;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.settings.GameSettings;
import com.mygdx.game.shared.position.Player;

import java.util.HashMap;

/**
 * Saves information about the animations and gives back the nex frame.
 * Created by platypus on 04.06.16.
 */
public class AnimationHandler{

    /**
     * Helps us to save the playerstate to see if it changed.
     */
    private enum playerState{
        idle, moving, exploding
    }

    /**
     * The Container we store the information in.
     */
    private HashMap<String, ImageCodePlayerState> playerId2ImageCodePlayerState;
    private Atlas atlas;

    /**
     * Initializes an AnimationHandler.
     *
     * @param atlas The Atlas with the assets.
     */
    public AnimationHandler(Atlas atlas){
        assert atlas != null;
        this.atlas = atlas;
        playerId2ImageCodePlayerState = new HashMap<>();
    }

    /**
     * Returns the next image to be displayed for a player. The image is determined by the kind of
     * ship the player has, if its still down and therefore exploding and its moving.
     * We assume that we will have a animation for each state represented by an array of Textures.
     *
     * @param player The player id for the player to get the image for.
     * @return The Texture of the next image. Null if animation is over and shall not be repeated.
     */
    public Texture getPlayerImage(Player player){
        assert player.getName() != null;
        adjustPlayerStates(player);
        ImageCodePlayerState icps = playerId2ImageCodePlayerState.get(player.getName());
        int imageCode = player.getShip().getImage();
        Texture result;
        if(icps.playerState == playerState.exploding){
            if(atlas.getPlayerAnimations(imageCode).exploding.length > icps.imageCode / GameSettings.REPEATS_PER_FRAME){
                result = atlas.getPlayerAnimations(imageCode).exploding[icps.imageCode / GameSettings.REPEATS_PER_FRAME];
                ++icps.imageCode;
            }else{
                return null;
            }
            // Hier können jetzt noch andere animationen eingehangen werden ähnlich wie oben. Diese sollten aber im loop abgespielt werden.
        }else if(icps.playerState == playerState.moving){
            if(atlas.getPlayerAnimations(imageCode).moving.length <= icps.imageCode / GameSettings.REPEATS_PER_FRAME){
                icps.imageCode = 0;
            }
            result = atlas.getPlayerAnimations(imageCode).moving[icps.imageCode / GameSettings.REPEATS_PER_FRAME];
            ++icps.imageCode;
        }else{
            if(atlas.getPlayerAnimations(imageCode).idle.length <= icps.imageCode / GameSettings.REPEATS_PER_FRAME){
                icps.imageCode = 0;
            }
            result = atlas.getPlayerAnimations(imageCode).idle[icps.imageCode / GameSettings.REPEATS_PER_FRAME];
            ++icps.imageCode;
        }
        return result;
    }

    /**
     * Checks for changes in the state of the player. For Example if a player changed from idle to moving,
     * we have to change the animation to moving and set the counter to 0 (the beginning of the animmation).
     *
     * @param player
     */
    private void adjustPlayerStates(Player player){
        assert player != null;
        if(playerId2ImageCodePlayerState.containsKey(player.getName())){
            ImageCodePlayerState icps = playerId2ImageCodePlayerState.get(player.getName());
            if(player.getHealth() <= 0 && icps.playerState != playerState.exploding){
                icps.playerState = playerState.exploding;
                icps.imageCode = 0;
            }else if(player.getHealth() > 0 && player.isMoving() && icps.playerState != playerState.moving){
                icps.playerState = playerState.moving;
                icps.imageCode = 0;
            }else if(player.getHealth() > 0 && !player.isMoving() && icps.playerState != playerState.idle){
                icps.playerState = playerState.idle;
                icps.imageCode = 0;
            }
        }else{
            playerId2ImageCodePlayerState.put(player.getName(), new ImageCodePlayerState());
            ImageCodePlayerState icps = playerId2ImageCodePlayerState.get(player.getName());
            if(player.getHealth() <= 0){
                icps.playerState = playerState.exploding;
                icps.imageCode = 0;
            }else if(player.isMoving()){
                icps.playerState = playerState.moving;
                icps.imageCode = 0;
            }else{
                icps.playerState = playerState.idle;
                icps.imageCode = 0;
            }
        }
    }

    /**
     * A pair for the Hashmap.
     */
    private static class ImageCodePlayerState{
        Integer imageCode;
        playerState playerState;
    }

}
