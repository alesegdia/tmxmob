package com.ufofrog.tmxmob.app.screen;

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
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.ufofrog.core.GameScreen;
import com.ufofrog.tmxmob.app.TmxMobApp;

public class NewMapScreen extends GameScreen<TmxMobApp> implements InputProcessor, GestureListener {

	Stage stage;
	private Skin skin;
	private SpriteBatch batch;
	Array<FileHandle> tmxfiles = new Array<FileHandle>();
	Array<FileHandle> imgfiles = new Array<FileHandle>();

	public NewMapScreen(TmxMobApp game) {
		super(game);
		skin = new Skin(Gdx.files.internal("uiskin.json"));

		batch = new SpriteBatch();
		stage = new Stage();
		
		// CREATE LABELS AND TEXTS
	    Label twLabel = new Label("Tile width: ", skin);
	    TextField twText = new TextField("32", skin);
	    Label thLabel = new Label("Tile height: ", skin);
	    TextField thText = new TextField("32", skin);
	    Label mwLabel = new Label("Map height: ", skin);
	    TextField mwText = new TextField("32", skin);
	    Label mhLabel = new Label("Map height: ", skin);
	    TextField mhText = new TextField("32", skin);

	    // CREATE AVAILABLE IMAGE LIST
	    RefreshFiles();
	    SelectBox<FileHandle> list = new SelectBox<FileHandle>(skin);
	    list.setItems(imgfiles);

	    final TmxMobApp thegame = this.game;

	    // CREATE "CREATE" BUTTON
	    TextButton createButton = new TextButton("Create!", skin);
		createButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				// create new game here with parameters
				thegame.setScreen(thegame.editScreen);
			}
	    });

		//stage.addActor(createButton);

	    // DISPLAY GUI
	    Table table = new Table();
	    table.add(twLabel);              // Row 0, column 0.
	    table.add(twText).width(100).space(10);    // Row 0, column 1.
	    table.row();                       // Move to next row.
	    table.add(thLabel);           // Row 1, column 0.
	    table.add(thText).width(100).space(10); // Row 1, column 1.
	    table.row();                       // Move to next row.
	    table.add(mwLabel);           // Row 1, column 0.
	    table.add(mwText).width(100).space(10); // Row 1, column 1.
	    table.row();                       // Move to next row.
	    table.add(mhLabel);           // Row 1, column 0.
	    table.add(mhText).width(100).space(10); // Row 1, column 1.
	    table.row();
	    table.add(list).colspan(2).space(10);
	    table.row();
	    table.add(createButton).colspan(2);
	    stage.addActor(table);
	    table.setFillParent(true);

	}

	void RefreshFiles()
	{
		imgfiles.clear();
		tmxfiles.clear();
	    FileHandle dirHandle;
		if (Gdx.app.getType() == ApplicationType.Android) {
			dirHandle = Gdx.files.external("./data/tmxmob");
		} else {
			// ApplicationType.Desktop ..
			dirHandle = Gdx.files.external("./data/tmxmob");
		}
	    for (FileHandle entry: dirHandle.list()) {
	    	if( ExtensionEquals(entry,"jpg") || ExtensionEquals(entry,"png") )
	    	{
		    	System.out.println("IMAGE: " + entry);
		    	imgfiles.add(entry);
	    	}
	    	else if( ExtensionEquals(entry,"tmx") )
	    	{
		    	System.out.println("TILED: " + entry);
	    		tmxfiles.add(entry);
	    	}
	    }
	}
	
	private boolean ExtensionEquals( FileHandle fh, String ext )
	{
		return fh.extension().compareToIgnoreCase(ext) == 0;
	}
	
	@Override
	public void Reset()
	{
		Gdx.input.setInputProcessor(stage);
		RefreshFiles();
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
