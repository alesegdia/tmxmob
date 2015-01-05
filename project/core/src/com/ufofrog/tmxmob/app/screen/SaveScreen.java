package com.ufofrog.tmxmob.app.screen;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import net.dermetfan.gdx.maps.tiled.TmxMapWriter;
import net.dermetfan.gdx.maps.tiled.TmxMapWriter.Format;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.ufofrog.core.GameScreen;
import com.ufofrog.tmxmob.app.TmxMobApp;

public class SaveScreen extends GameScreen<TmxMobApp> implements InputProcessor, GestureListener {

	Stage stage;
	private Skin skin;
	private SpriteBatch batch;
	Array<FileHandle> tmxfiles = new Array<FileHandle>();
	Array<FileHandle> imgfiles = new Array<FileHandle>();
	TmxMapWriter tmxwritter;
	TextField tmxFileField;

	public SaveScreen(TmxMobApp game) {
		super(game);
		skin = new Skin(Gdx.files.internal("uiskin.json"));

		batch = new SpriteBatch();
		stage = new Stage();
		
		// CREATE LABELS AND TEXTS
	    Label twLabel = new Label("Destination file", skin);
	    tmxFileField = new TextField("fistro.tmx", skin);

	    final TmxMobApp thegame = this.game;

	    // CREATE "CREATE" BUTTON
	    TextButton saveButton = new TextButton("Save!", skin);
	    saveButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				try {
					FileHandle fh = Gdx.files.external("./data/tmxmob/" + tmxFileField.getText());
					Writer fw = fh.writer(false);
					tmxwritter = new TmxMapWriter(fw);
					tmxwritter.tmx(thegame.editScreen.mapHolder.getTiledMap(), Format.Base64);
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				thegame.setScreen(thegame.editScreen);
			}
	    });
		//stage.addActor(createButton);

	    // DISPLAY GUI
	    Table table = new Table();
	    table.add(twLabel).space(10).center();
	    table.row();
	    table.add(tmxFileField).space(20).center();    // Row 0, column 1.
	    table.row();                       // Move to next row.
	    table.add(saveButton).center();
	    stage.addActor(table);
	    table.setFillParent(true);

	}

	
	@Override
	public void Reset()
	{
		Gdx.input.setInputProcessor(stage);
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
