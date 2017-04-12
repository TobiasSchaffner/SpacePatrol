package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.SpacePatrol;
import com.mygdx.game.settings.GameSettings;

public final class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "SpaceRoamer";
		config.width = GameSettings.SCREEN_WIDTH;
		config.height = GameSettings.SCREEN_HEIGHT;
		config.backgroundFPS = GameSettings.FPS;
		config.foregroundFPS = GameSettings.FPS;
		new LwjglApplication(new SpacePatrol(), config);
	}
}
