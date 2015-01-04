package com.ufofrog.tmxmob.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ufofrog.core.ActionResolverDesktop;
import com.ufofrog.tmxmob.TmxMobApp;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 480;
		config.height = 320;
		new LwjglApplication(new TmxMobApp(new ActionResolverDesktop()), config);
	}
}
