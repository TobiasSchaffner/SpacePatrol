package com.mygdx.game.shared.util;

import com.mygdx.game.settings.GameSettings;

import java.util.regex.Pattern;

public final class Util{

    private static final int HIGHEST_PORT = 65536;

    public enum Message{
        CLIENT_ERROR, CLIENT_WARNING, CLIENT_MESSAGE, CLIENT_DEBUG,
        SERVER_ERROR, SERVER_WARNING, SERVER_MESSAGE, SERVER_DEBUG
    }

    /**
     * Helpmethod for equals to check two doubles.
     *
     * @param have the first double
     * @param want the second double
     * @return True if equal enough.
     */
    public static boolean nearly(final double have, final double want){
        final double gap = 0.1;
        return Math.abs(have - want) < gap;

    }

    private static final Pattern PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public static boolean validate(final String ip){
        return PATTERN.matcher(ip).matches();
    }

    public static boolean validate(final int port){
        return port > 0 && port < HIGHEST_PORT;
    }

    /**
     * Util to print Data for different Debug Levels
     *
     * @param level
     * @param message
     */
    public static void report(Message level, String message){
        if(level.ordinal() < 4){
            if(GameSettings.CLIENT_DEBUG_LEVEL > level.ordinal()){
                System.out.println(message);
            }
        }else{
            if(GameSettings.SERVER_DEBUG_LEVEL > level.ordinal() % 4){
                System.out.println(message);
            }
        }
    }

}
