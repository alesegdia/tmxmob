package com.ufofrog.tmxmob.app;

import com.badlogic.gdx.math.Vector2;
import com.ufofrog.core.ActionResolver;
import com.ufofrog.core.GameApp;
import com.ufofrog.tmxmob.app.config.UserConfig;
import com.ufofrog.tmxmob.app.screen.EditScreen;
import com.ufofrog.tmxmob.app.screen.LoadScreen;
import com.ufofrog.tmxmob.app.screen.NewMapScreen;
import com.ufofrog.tmxmob.app.screen.SaveScreen;
import com.ufofrog.tmxmob.map.MapHolder;

public class TmxMobApp extends GameApp {

	public EditScreen editScreen;
	public NewMapScreen newMapScreen;
	public SaveScreen saveScreen;
	public LoadScreen loadScreen;
	
	public UserConfig usercfg;
	public MapHolder mapHolder;

	public TmxMobApp(ActionResolver actionResolver) {
		super(actionResolver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Create () {
		usercfg = new UserConfig();
		usercfg.Defaults();
		usercfg.LoadFromDisk();
		usercfg.SaveToDisk();

		editScreen = new EditScreen(this);
		newMapScreen = new NewMapScreen(this);
		saveScreen = new SaveScreen(this);
		loadScreen = new LoadScreen(this);
		this.setScreen(this.editScreen);
	}

	@Override
	public void resize (int width, int height) {
	}


	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}
}
