package com.ufofrog.tmxmob.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.ufofrog.core.GameScreen;
import com.ufofrog.tmxmob.app.MapHolder;
import com.ufofrog.tmxmob.app.StaticConfig;
import com.ufofrog.tmxmob.app.TilePalette;
import com.ufofrog.tmxmob.app.TmxMobApp;

public class EditScreen extends GameScreen<TmxMobApp> implements InputProcessor, GestureListener {
	
	// GDX CORE
	private SpriteBatch batch;
	InputMultiplexer imux;

	// GUI
	private Stage stage;
	private Slider mapZoomSlider;
	private TextButton editMoveButton;
	private TextButton saveButton;
	private TextButton newButton;
	private TextButton loadButton;

	private Skin skin;
	private float stageZoom = 500;

	// CAMERA
	private OrthographicCamera camera;
	FitViewport viewport;
	private float camUnitScale = 1f / 64f;
	private float currentZoom = 1f;
	private float maxZoom = 3f;
	private float minZoom = 0.01f;
	private float zoomStep = 0.01f;

	boolean moveMode = false;
	
	MapHolder mapHolder;
	private TilePalette tilePalette;

	
	@Override
	public void Reset()
	{
		Gdx.input.setInputProcessor(imux);
	}

	public EditScreen( TmxMobApp game )
	{
		super(game);

		// rendering stuff
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera((w / h) * 10, 10);
		camera.zoom = currentZoom;
		camera.update();
		viewport = new FitViewport((w/h)*10, 10, camera);
		batch = new SpriteBatch();
		
		// stage config
		stage = new Stage( new StretchViewport(w, h) );
		stage.getViewport().setWorldSize(stageZoom ,h/w*stageZoom);
		skin = new Skin(Gdx.files.internal("uiskin.json"));

		// input config
		imux = new InputMultiplexer();
		imux.addProcessor(stage);
		imux.addProcessor(this);
		Gdx.input.setInputProcessor(imux);
		
		// create map and tile palette
		tilePalette = new TilePalette();
		mapHolder = new MapHolder( this.camUnitScale, tilePalette );
		
		// load splash map
		mapHolder.LoadInternalFile("splash.tmx");

		//tilePalette.loadFromMap(mapHolder);
		//mapHolder.LoadExternalFile("maps/map0.tmx");


		// screen buttons
		final TmxMobApp thegame = this.game;
		saveButton = new TextButton("SAVE", skin);
		saveButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				thegame.setScreen(thegame.saveScreen);
			}
		});
		newButton = new TextButton("NEW", skin);
		newButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				thegame.setScreen(thegame.newMapScreen);
			}
		});
		loadButton = new TextButton("LOAD", skin);
		loadButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				thegame.setScreen(thegame.loadScreen);
			}
		});
		stage.addActor(saveButton);
		stage.addActor(newButton);
		stage.addActor(loadButton);
		saveButton.setPosition(
				stage.getViewport().getWorldWidth() - saveButton.getWidth() - 6f,
				stage.getViewport().getWorldHeight() - saveButton.getHeight() - 6f);
		newButton.setPosition(
				stage.getViewport().getWorldWidth() - newButton.getWidth() - 6f,
				stage.getViewport().getWorldHeight() - newButton.getHeight() - 12f - saveButton.getHeight());
		loadButton.setPosition(
				stage.getViewport().getWorldWidth() - loadButton.getWidth() - 6f,
				stage.getViewport().getWorldHeight() - newButton.getHeight() * 2 - 16f - saveButton.getHeight());

		// zoom slider
		mapZoomSlider = new Slider(minZoom, maxZoom, zoomStep, false, skin);
		mapZoomSlider.setValue(currentZoom);
		mapZoomSlider.setPosition(72f, 6f);
		stage.addActor(mapZoomSlider);

		// edit/move button
		editMoveButton = new TextButton("EDIT", skin, "toggle");
		editMoveButton.setWidth(60);
		stage.addActor(editMoveButton);


		tilePalette.getScrollPane().setHeight(stage.getViewport().getWorldHeight());
		stage.addActor(tilePalette.getScrollPane());

		camera.position.x = camera.position.x - 4f;
		camera.position.y = camera.position.y - 1f;
		
		mapZoomSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				camera.zoom = mapZoomSlider.getValue();
			}
		});
		//slider.setPosition(Gdx.graphics.getWidth() - slider.getWidth() - 6f, 6f);
		mapZoomSlider.setPosition(stage.getViewport().getWorldWidth() - mapZoomSlider.getWidth() - 6f, 6f);
		editMoveButton.setPosition(stage.getViewport().getWorldWidth() - editMoveButton.getWidth() - 6f, 32f);

		editMoveButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				moveMode = !moveMode;
				if( moveMode ) editMoveButton.setText("MOVE");
				else editMoveButton.setText("EDIT");
				
			}
		});

	}
	
	public void resize (int width, int height) {
	    viewport.update(width, height, true);
	    stage.getViewport().update(width, height, true);
	    camera.setToOrtho(false, 10f*((float)width)/((float)height), 10f);
	    camera.position.x = mapHolder.getWidth()/2f;
	    camera.position.y = mapHolder.getHeight()/2f;
	    
	    //viewport.update(width, height, true);
	    //camera.setToOrtho(false, 10f*((float)width)/((float)height), 10f);

	}

	@Override
	public void render ( float delta ) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		camera.update();

		mapHolder.render(camera);

		batch.begin();
		stage.draw();
		batch.end();

	}

	@Override
	public boolean keyDown(int keycode) {
		if( keycode == Keys.W ) camera.position.y -= 1;
		if( keycode == Keys.S ) camera.position.y += 1;
		if( keycode == Keys.A ) camera.position.x += 1;
		if( keycode == Keys.D ) camera.position.x -= 1;
		if( keycode == Keys.SPACE ) this.moveMode = !this.moveMode;

		return false;
	}


	private boolean IsValidTile( Vector3 tile )
	{
		int mapw = (Integer) mapHolder.getWidth();
		int maph = (Integer) mapHolder.getHeight();
		return tile.x >= 0 && tile.x < mapw &&
			   tile.y >= 0 && tile.y < maph;
	}
	
	int prevx, prevy;

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		prevx = screenX;
		prevy = screenY;

		if( moveMode == false)
		{
			// get world coordinates
			Vector3 clickPos = camera.unproject(new Vector3( screenX, screenY, 0f));
			
			// tilemap is at 0, round to get tile pos
			clickPos.x = (float) Math.floor(clickPos.x);
			clickPos.y = (float) Math.floor(clickPos.y);
			
			// if it's a valid tile of our map
			if( IsValidTile( clickPos ) )
			{
				TiledMapTileLayer ml = (TiledMapTileLayer) mapHolder.getTiledMap().getLayers().get(0);
				Cell c = ml.getCell(((int)clickPos.x), ((int)clickPos.y));
				c.setTile(tilePalette.getSelectedTile());
				System.out.println(ml.getCell(((int)clickPos.x), ((int)clickPos.y)).getTile().getId());
			}
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	float speed = 0.01f;

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if( this.moveMode == true )
		{
			int dx = prevx - screenX;
			int dy = prevy - screenY;
			prevx = screenX;
			prevy = screenY;
			camera.position.x += dx * speed * currentZoom;
			camera.position.y -= dy * speed * currentZoom;
			System.out.println(dx);
		}
		touchDown(screenX, screenY, pointer, 0);

		return false;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
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
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
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
		System.out.println("FLING!");
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
