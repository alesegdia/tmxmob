package com.ufofrog.tmxmob.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ufofrog.core.GameScreen;
import com.ufofrog.tmxmob.app.TmxMobApp;

public class LoadScreen extends GameScreen<TmxMobApp> implements InputProcessor, GestureListener {

	Stage stage;
	private Skin skin;
	private SpriteBatch batch;
	SelectBox<FileHandle> fileList;

	public LoadScreen(TmxMobApp game) {
		super(game);
		skin = new Skin(Gdx.files.internal("uiskin.json"));

		batch = new SpriteBatch();
		stage = new Stage();
		
		// CREATE LABELS AND TEXTS
	    Label twLabel = new Label("Source", skin);
	    fileList = new SelectBox<FileHandle>(skin);
	    game.newMapScreen.RefreshFiles();
	    fileList.setItems(game.newMapScreen.tmxfiles);
	    
	    final TmxMobApp thegame = this.game;

	    // CREATE "LOAD" BUTTON
	    TextButton loadButton = new TextButton("Load!", skin);
	    loadButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				System.out.println(fileList.getSelected().toString());
				thegame.mapHolder.LoadExternalFile(fileList.getSelected().toString());					
				thegame.setScreen(thegame.editScreen);
			}
	    });

	    // DISPLAY GUI
	    Table table = new Table();
	    table.add(twLabel).space(10).center();
	    table.row();
	    table.add(fileList).space(10).center();
	    table.row();
	    table.add(loadButton).center();
	    stage.addActor(table);
	    table.setFillParent(true);

	}

	
	@Override
	public void Reset()
	{
		Gdx.input.setInputProcessor(stage);
	    game.newMapScreen.RefreshFiles();
	    fileList.setItems(game.newMapScreen.tmxfiles);
	}
	
	@Override
	public void render( float deltatime )
	{
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		batch.begin();
		stage.draw();
		batch.end();
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

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
