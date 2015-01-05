package com.ufofrog.tmxmob.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.ufofrog.core.ActionResolverDesktop;
import com.ufofrog.tmxmob.app.TmxMobApp;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new TmxMobApp(new ActionResolverDesktop()), config);
	}
}
