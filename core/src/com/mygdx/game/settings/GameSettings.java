package com.mygdx.game.settings;

/**
 * Created by platypus on 01.06.16.
 */
public final class GameSettings{

    /*
     * Debug Levels:
     *      0 No Debug
     *      1 Only Errors
     *      2 Errors and Warnings
     *      3 Errors Warnings and Messages (default)
     *      4 All (incl Debug Messages)
     */
    public static final int SERVER_DEBUG_LEVEL = 3;
    public static final int CLIENT_DEBUG_LEVEL = 3;

    // Network defaults
    public static final String SERVER_IP = "127.0.0.1";
    public static final int SERVER_PORT = 4325;

    public static final int SLEEP_TIME = 100;

    // Screen
    public static final int SCREEN_WIDTH = 1600;
    public static final int SCREEN_HEIGHT = 900;
    public static final int SCREEN_WIDTH_MID = SCREEN_WIDTH / 2;
    public static final int SCREEN_HEIGHT_MID = SCREEN_HEIGHT / 2;

    public static final int FPS = 30;

    // Space sizes
    public static final int SPACE_WIDTH = 2000;
    public static final int SPACE_HEIGHT = 2000;
    public static final int SPACE_WIDTH_MID = SPACE_WIDTH / 2;
    public static final int SPACE_HEIGHT_MID = SPACE_HEIGHT / 2;

    // Offline default values (TODO defaults have to be fetched from the server)
    public static final int START_ANGLE = 0;
    public static final int START_HEALTH = 100;

    // Bot
    public static final int AGRESSION_RADIUS = 200;


    ////////////
    // Client //
    ////////////

    //How often every Frame of a animation is rendered
    public static final int REPEATS_PER_FRAME = 3;

    //Relative offset to the middle of a image relative to the size of the image.
    public static final double TEXT_OFFSET_FACTOR = 0.43;

    // Healthbar
    public static final int HEALTHBAR_HORIZONTAL_OFFSET = 50;
    public static final int HEALTHBAR_VERTICAL_OFFSET = 70;
    public static final int HEALTHBAR_WIDTH = 102;
    public static final int HEALTHBAR_HEIGHT = 6;

    // The area around the middle that triggeres a shot wenn touched
    public static final int SHOT_TRIGGER_AREA = 64;

    // time that will be waited for a new packet for that client till it will be dropped.
    public static final int DROP_TIMEOUT = 1000;

    // How long a shot lasts.
    public static final int SHOT_LIFE_TIME = 1000;
}
