package com.mygdx.game.client.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.SpacePatrol;
import com.mygdx.game.client.client.Connection;
import com.mygdx.game.settings.GameSettings;
import com.mygdx.game.shared.geometry.GeoPoint;
import com.mygdx.game.shared.geometry.GeoVector;
import com.mygdx.game.shared.orb.Orb;
import com.mygdx.game.shared.position.Player;
import com.mygdx.game.shared.position.Shot;

public class SpaceScreen implements Screen{


    /**
     * The LibGDX Camera.
     */
    private OrthographicCamera camera;

    /**
     * The game holding the objects essential for all screens.
     */
    private final SpacePatrol game;

    /**
     * The Network connection to the Server.
     */
    private Connection connection;


    /**
     * Constructor for SpaceScreen Objects.
     *
     * @param mainGame The game holding the objects essential for all screens.
     */
    public SpaceScreen(SpacePatrol mainGame){
        assert mainGame != null;

        this.game = mainGame;
        create();
    }

    private void create(){
        game.stage.clear();

        // Start Networking
        connection = new Connection(GameSettings.SERVER_IP, GameSettings.SERVER_PORT, game.getUser(), game.getSpace());
        Thread connectionThread = new Thread(connection);
        connectionThread.setName("Client Connection");
        connectionThread.start();

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
    }

    @Override
    public void show(){
        game.getAtlas().getMusic().setLooping(true);
        game.getAtlas().getMusic().play();
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        getInput();

        game.batch.begin();

        // Render all Objects in the Space
        renderOrbs();
        renderShots();
        renderPlayers();
        game.batch.end();
    }

    /**
     * Handle the input (Touch/Keyboard)
     */
    private void getInput(){
        if(Gdx.input.isTouched()){
            final Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            if(Math.abs(touchPos.x - GameSettings.SCREEN_WIDTH_MID) < GameSettings.SHOT_TRIGGER_AREA && Math.abs(touchPos.y - GameSettings.SCREEN_HEIGHT_MID) < GameSettings.SHOT_TRIGGER_AREA){
                game.getUser().setHasShot(true);
            }
            game.getUser().setMovement(new GeoVector(touchPos.x - GameSettings.SCREEN_WIDTH_MID, touchPos.y - GameSettings.SCREEN_HEIGHT_MID));
        }
    }

    /**
     * Render all the Orbs in this space.
     */
    private void renderOrbs(){
        for(final Orb orb : game.getSpace().getOrbs()){
            final GeoPoint orbPosition = orb.getPosition();
            final int offsetX = (int) (orbPosition.getX() - game.getUser().getPosition().getX());
            final int offsetY = (int) (orbPosition.getY() - game.getUser().getPosition().getY());
            Texture image = game.getAtlas().getOrbImage(orb.getImage());
            if(Math.abs(offsetX) < GameSettings.SCREEN_WIDTH_MID + image.getWidth() / 2 && Math.abs(offsetY) < GameSettings.SCREEN_HEIGHT_MID + image.getHeight() / 2){
                float x = GameSettings.SCREEN_WIDTH_MID + offsetX - image.getWidth() / 2;
                float y = GameSettings.SCREEN_HEIGHT_MID + offsetY - image.getHeight() / 2;
                game.batch.draw(image, x, y);
                game.getAtlas().getFont().draw(game.batch, orb.getName(), GameSettings.SCREEN_WIDTH_MID + offsetX + (int) (image.getWidth() * GameSettings.TEXT_OFFSET_FACTOR), GameSettings.SCREEN_HEIGHT_MID + offsetY + (int) (image.getHeight() * GameSettings.TEXT_OFFSET_FACTOR));
            }
        }
    }

    /**
     * Rendering of all the Shots of the players that havnt exceeded.
     */
    private void renderShots(){
        for(final Shot shot : game.getSpace().getContainer().getShots().values()){
            if(shot.getTimeSinceUpdate() < GameSettings.DROP_TIMEOUT){
                final int offsetX = (int) (shot.getAdjustedX() - game.getUser().getPosition().getX());
                final int offsetY = (int) (shot.getAdjustedY() - game.getUser().getPosition().getY());
                Texture image = game.getAtlas().getFireImage(shot.getImage());
                if(Math.abs(offsetX) < GameSettings.SCREEN_WIDTH_MID + image.getWidth() / 2 && Math.abs(offsetY) < GameSettings.SCREEN_HEIGHT_MID + image.getHeight() / 2){
                    float x = GameSettings.SCREEN_WIDTH_MID + offsetX - image.getWidth() / 2;
                    float y = GameSettings.SCREEN_HEIGHT_MID + offsetY - image.getHeight() / 2;
                    game.batch.draw(image, x, y, image.getWidth() / 2, image.getHeight() / 2, image.getWidth(), image.getHeight(), 1, 1, shot.getMovementAngle(), 0, 0, image.getWidth(), image.getHeight(), false, false);
                    if(shot.isNew()){
                        game.getAtlas().getFireMusic(shot.getImage()).play();
                        shot.setIsNew(false);
                    }
                }
            }else{
                game.getSpace().getContainer().getShots().remove(shot);
            }
        }
    }

    /**
     * Rendering of all the player except of the user.
     */
    private void renderPlayers(){
        //Rendering of all other Players
        for(final Player player : game.getSpace().getContainer().getPositions().values()){
            if(player.getName().equals(game.getUser().getName())){
                if(player.isNew()){
                    game.getUser().setPosition(player.getShip().toString(), player.getHealth(), player.getX(), player.getY(), game.getUser().getAngle(), game.getUser().isMoving() ? 1 : 0, player.getShotId());
                    player.setIsNew(false);
                }
                drawPlayer(0, 0, game.getUser());
            }else{
                final int offsetX = (int) (player.getAdjustedX() - game.getUser().getPosition().getX());
                final int offsetY = (int) (player.getAdjustedY() - game.getUser().getPosition().getY());
                drawPlayer(offsetX, offsetY, player);
            }
        }
    }

    /**
     * Renders the image for a player at a specific position.
     *
     * @param offsetX The offset on the X-Axis to the position to the player.
     * @param offsetY The offset on the X-Axis to the position to the player.
     * @param player  The player to render the image for.
     */
    private void drawPlayer(int offsetX, int offsetY, Player player){
        Texture image = game.getAnimationHandler().getPlayerImage(player);
        // If the animationhandler returns null explosionanimation finished.
        if(image == null){
            game.getSpace().getContainer().getPositions().remove(player);
            return;
        }
        if(Math.abs(offsetX) < GameSettings.SCREEN_WIDTH_MID + image.getWidth() / 2 && Math.abs(offsetY) < GameSettings.SCREEN_HEIGHT_MID + image.getHeight() / 2){
            float userPosX = GameSettings.SCREEN_WIDTH_MID + offsetX - image.getWidth() / 2;
            float userPosY = GameSettings.SCREEN_HEIGHT_MID + offsetY - image.getHeight() / 2;
            game.batch.draw(image, userPosX, userPosY, image.getWidth() / 2, image.getHeight() / 2, image.getWidth(), image.getHeight(), 1, 1, player.getMovementAngle(), 0, 0, image.getWidth(), image.getHeight(), false, false);
            game.getAtlas().getFont().draw(game.batch, player.getName(), GameSettings.SCREEN_WIDTH_MID + offsetX + (int) (image.getWidth() * GameSettings.TEXT_OFFSET_FACTOR), GameSettings.SCREEN_HEIGHT_MID + offsetY + (int) (image.getHeight() * GameSettings.TEXT_OFFSET_FACTOR));
            drawHealthbar(offsetX, offsetY, player);
        }
    }

    /**
     * Drawing of the healthbars over players.
     *
     * @param offsetX The offset on the X-Axis to the position over the player.
     * @param offsetY The offset on the Y-Axis to the position over the player.
     * @param player  The player to geht the health from.
     */
    private void drawHealthbar(int offsetX, int offsetY, Player player){
        final Texture black = game.getAtlas().getColorImage("black");
        final Texture red = game.getAtlas().getColorImage("red");
        final Texture green = game.getAtlas().getColorImage("green");
        float colorPosX = GameSettings.SCREEN_WIDTH_MID + offsetX - GameSettings.HEALTHBAR_HORIZONTAL_OFFSET - 1;
        float colorPosY = GameSettings.SCREEN_HEIGHT_MID + offsetY + GameSettings.HEALTHBAR_VERTICAL_OFFSET - 1;
        game.batch.draw(black, colorPosX, colorPosY, GameSettings.HEALTHBAR_WIDTH, GameSettings.HEALTHBAR_HEIGHT);
        colorPosX = GameSettings.SCREEN_WIDTH_MID + offsetX - GameSettings.HEALTHBAR_HORIZONTAL_OFFSET;
        colorPosY = GameSettings.SCREEN_HEIGHT_MID + offsetY + GameSettings.HEALTHBAR_VERTICAL_OFFSET;
        game.batch.draw(red, colorPosX, colorPosY, GameSettings.HEALTHBAR_WIDTH - 2, GameSettings.HEALTHBAR_HEIGHT - 2);
        colorPosX = GameSettings.SCREEN_WIDTH_MID + offsetX - GameSettings.HEALTHBAR_HORIZONTAL_OFFSET;
        colorPosY = GameSettings.SCREEN_HEIGHT_MID + offsetY + GameSettings.HEALTHBAR_VERTICAL_OFFSET;
        game.batch.draw(green, colorPosX, colorPosY, (int) ((double) player.getHealth() / player.getShip().getMaxHealth() * (GameSettings.HEALTHBAR_WIDTH - 2)), GameSettings.HEALTHBAR_HEIGHT - 2);
    }

    @Override
    public void resize(int width, int height){
    }

    @Override
    public void pause(){
    }

    @Override
    public void resume(){
    }

    @Override
    public void hide(){
    }

    @Override
    public void dispose(){
        game.getAtlas().disposeAll();
        game.batch.dispose();
    }
}
