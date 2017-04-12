package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;
import com.mygdx.game.client.screens.SpaceScreen;
import com.mygdx.game.client.user.User;
import com.mygdx.game.server.server.Server;
import com.mygdx.game.settings.GameSettings;
import com.mygdx.game.shared.util.Util;

import java.io.IOException;
import java.net.ServerSocket;

import static com.mygdx.game.shared.util.Util.report;

/**
 * Created by platypus on 15.04.16.
 */
class MainMenuScreen implements Screen{
    private final SpacePatrol game;
    private Skin buttonSkin;
    private OrthographicCamera camera;

    private Table table;
    private Image logo;
    private Label usernameLabel;
    private TextField usernameField;
    private Label passwordLabel;
    private TextField passwordField;
    private TextButton loginButton;
    private TextButton serverButton;

    MainMenuScreen(final SpacePatrol game){
        assert game != null;

        this.game = game;
        create();
    }

    private void create(){
        createBasicSkin();
        Gdx.input.setInputProcessor(game.stage);

        final BitmapFont font = new BitmapFont();

        final Label.LabelStyle infoStyle = new Label.LabelStyle(font, Color.WHITE);

        table = new Table();

        logo = new Image(new Texture(Gdx.files.internal("android/assets/logo.jpg")));
        logo.setBounds(0, 0, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
        logo.setScaling(Scaling.stretch);

        usernameLabel = new Label("Username:", infoStyle);
        passwordLabel = new Label("Password:", infoStyle);

        final TextField.TextFieldStyle fieldStyle = new TextField.TextFieldStyle();
        fieldStyle.font = font;
        fieldStyle.fontColor = Color.WHITE;

        usernameField = new TextField("", fieldStyle);
        usernameField.setMessageText("username");
        passwordField = new TextField("", fieldStyle);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        passwordField.setMessageText("password");

        usernameField.setTextFieldListener(new TextField.TextFieldListener(){
            @Override
            public void keyTyped(TextField textField, char key){
                report(Util.Message.CLIENT_DEBUG, "Skillaria: " + key);
            }
        });

        passwordField.setTextFieldListener(new TextField.TextFieldListener(){
            @Override
            public void keyTyped(TextField textField, char key){
                report(Util.Message.CLIENT_DEBUG, "Skillaria: " + key);
            }
        });

        loginButton = new TextButton("Login", buttonSkin); // Use the initialized skin

        loginButton.addListener(new ChangeListener(){
            public void changed(ChangeEvent event, Actor actor){
                // Create User Object
                game.setUser(new User(usernameField.getText(),
                        passwordField.getText(),
                        "",
                        GameSettings.START_HEALTH,
                        GameSettings.SPACE_WIDTH_MID,
                        GameSettings.SPACE_HEIGHT_MID,
                        GameSettings.START_ANGLE,
                        0));
                game.setScreen(new SpaceScreen(game));
            }
        });

        serverButton = new TextButton("start server", buttonSkin); // Use the initialized skin

        serverButton.setChecked(serverUp(GameSettings.SERVER_PORT));

        serverButton.addListener(new ChangeListener(){
            public void changed(ChangeEvent event, Actor actor){
                if(serverUp(GameSettings.SERVER_PORT)){
                    serverButton.setChecked(true);
                }else{
                    Server server = new Server();
                    new Thread(server).start();
                }
            }
        });

        // Table with the components for input and buttons
        table.add(usernameLabel).padTop(GameSettings.SCREEN_HEIGHT / 3);
        table.row();
        table.add(usernameField).pad(20);
        table.row().pad(10);
        table.add(passwordLabel).pad(2);
        table.row();
        table.add(passwordField).pad(20);
        table.row();
        table.add(loginButton).pad(2);
        table.row();
        table.add(serverButton).pad(2);
        table.setBounds((GameSettings.SCREEN_WIDTH_MID) / 2, (GameSettings.SCREEN_HEIGHT - GameSettings.SCREEN_HEIGHT / 3) / 2, GameSettings.SCREEN_WIDTH_MID, GameSettings.SCREEN_HEIGHT / 3);

        game.stage.addActor(logo);
        game.stage.addActor(table);

    }

    @Override
    public void show(){

    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.stage.act();
        game.stage.draw();
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
        buttonSkin.dispose();
        game.stage.dispose();
    }

    private void createBasicSkin(){
        //Create a font
        final BitmapFont font = new BitmapFont();
        buttonSkin = new Skin();
        buttonSkin.add("default", font);

        //Create a texture
        final Pixmap pixmap = new Pixmap(GameSettings.SCREEN_WIDTH / 4, GameSettings.SCREEN_HEIGHT / 10, Pixmap.Format.RGB888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        buttonSkin.add("background", new Texture(pixmap));

        //Create a button style
        final TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = buttonSkin.newDrawable("background", Color.GRAY);
        textButtonStyle.down = buttonSkin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.checked = buttonSkin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.over = buttonSkin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.font = buttonSkin.getFont("default");
        buttonSkin.add("default", textButtonStyle);
    }

    private boolean serverUp(int port){
        boolean portTaken = false;
        //noinspection EmptyTryBlock
        try(ServerSocket ignored = new ServerSocket(port, 1)){
            // nope
        }catch(IOException e){
            portTaken = true;
        }
        return portTaken;
    }
}