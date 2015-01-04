package com.ufofrog.tmxmob;

import java.io.FileWriter;
import java.io.IOException;
import net.dermetfan.gdx.maps.tiled.TmxMapWriter;
import net.dermetfan.gdx.maps.tiled.TmxMapWriter.Format;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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

public class TmxMobApp extends ApplicationAdapter implements InputProcessor {

	// GDX CORE
	private SpriteBatch batch;
	InputMultiplexer imux;

	// GUI
	private Stage stage;
	private Slider mapZoomSlider;
	private TextButton editMoveButton;
	private TextButton saveButton;
	private ScrollPane tilePaletteScrollPane;
	private Skin skin;
	private float stageZoom = 600;
	
	// MAP
	private TiledMap currentMap;
	private TiledMapRenderer mapRenderer;
	private AssetManager assetManager;

	// TILE PALETTE
	Array<TiledMapTile> tiles = new Array<TiledMapTile>();
	Array<ImageButton> tileImages = new Array<ImageButton>();

	// CAMERA
	private OrthographicCamera mapCamera;
	FitViewport mapViewport;
	private float mapUnitScale = 1f / 64f;
	private float currentZoom = 1f;
	private float maxZoom = 3f;
	private float minZoom = 0.5f;
	private float zoomStep = 0.01f;

	boolean moveMode = false;
	
	TmxMapWriter tmxwritter;
	
	@Override
	public void create () {
		
		Gdx.input.setInputProcessor(this);
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		mapCamera = new OrthographicCamera((w / h) * 10, 10);
		mapCamera.zoom = currentZoom;
		mapCamera.update();
		mapViewport = new FitViewport((w/h)*10, 10, mapCamera);
		
		batch = new SpriteBatch();
		
		imux = new InputMultiplexer();
		stage = new Stage( new StretchViewport(w, h) );
		stage.getViewport().setWorldSize(stageZoom ,h/w*stageZoom);
		imux.addProcessor(stage);
		imux.addProcessor(this);

		Gdx.input.setInputProcessor(imux);
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		mapZoomSlider = new Slider(minZoom, maxZoom, zoomStep, false, skin);
		editMoveButton = new TextButton("edit", skin, "toggle");
		saveButton = new TextButton("save", skin);
		stage.addActor(mapZoomSlider);
		stage.addActor(editMoveButton);
		stage.addActor(saveButton);
		saveButton.setPosition(200f,200f);
		
		mapZoomSlider.setValue(currentZoom);
		mapZoomSlider.setPosition(72f, 6f);
		editMoveButton.setWidth(60);

		new ShapeRenderer();

		// map
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(
				new InternalFileHandleResolver()));
		assetManager.load("maps/map0.tmx", TiledMap.class);
		assetManager.finishLoading(); 
		currentMap = assetManager.get("maps/map0.tmx");
		mapRenderer = new OrthogonalTiledMapRenderer(currentMap, mapUnitScale);

		Table t = new Table();
		
		int i = 0;
		for( TiledMapTile tmt : currentMap.getTileSets().getTileSet(0) )
		{
			TextureRegion tr = tmt.getTextureRegion();
			tr.getTexture();
			Sprite sp = new Sprite();
			sp.setPosition(0, 0);
			sp.setRegion(tr);
			ImageButtonStyle ims = new ImageButtonStyle();
			
			ims.imageUp = new TextureRegionDrawable(tr);
			ims.imageDown = new TextureRegionDrawable(tr);
			
			ImageButton img = new ImageButton(ims);
			tileImages.add(img);
			img.setPosition(0, i*64);
			img.setUserObject(((Integer)i));
			img.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					int val = ((Integer)event.getListenerActor().getUserObject());
					selectedTileIndex = val;
				}
			});
			t.add(img).padLeft(0).pad(0).bottom();
			t.row();

			//stage.addActor(img);
			i++;

			this.tiles.add(tmt);
			//this.editsprites.add(tr);
			
		}
		//l.setItems(uimages);
		t.left();
		t.setPosition(0f, 0f);
		t.setFillParent(true);
		t.setWidth(tileImages.get(0).getWidth());
		Label label = new Label("", skin);
		label.setText("sample tileset by Nosghy");
		label.setPosition(72f, 10f);
		stage.addActor(label);
		tilePaletteScrollPane = new ScrollPane(t);
		tilePaletteScrollPane.setPosition(0f, 0f);
		tilePaletteScrollPane.setHeight(stage.getViewport().getWorldHeight());
		tilePaletteScrollPane.setWidth(tileImages.get(0).getWidth());
		stage.addActor(tilePaletteScrollPane);
		mapCamera.position.x = mapCamera.position.x - 4f;
		mapCamera.position.y = mapCamera.position.y - 1f;
		
		mapZoomSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				mapCamera.zoom = mapZoomSlider.getValue();
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
				if( moveMode ) editMoveButton.setText("move");
				else editMoveButton.setText("edit");
				
			}
		});
		saveButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				moveMode = !moveMode;
				if( moveMode ) editMoveButton.setText("move");
				else editMoveButton.setText("edit");
				
			}
		});

		FileWriter fw;
		try {
			fw = new FileWriter("asd.tmx");
			tmxwritter = new TmxMapWriter(fw);
			tmxwritter.tmx(currentMap, Format.Base64);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void resize (int width, int height) {
	    mapViewport.update(width, height, true);
	    stage.getViewport().update(width, height, true);
	    mapCamera.setToOrtho(false, 10f*((float)width)/((float)height), 10f);
	    mapCamera.position.x = 3.5f;
	    mapCamera.position.y = 3.5f;
	    
	    //viewport.update(width, height, true);
	    //camera.setToOrtho(false, 10f*((float)width)/((float)height), 10f);

	}

	@Override
	public void render () {
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		mapCamera.update();

		mapRenderer.setView(mapCamera);
		mapRenderer.render();

		batch.begin();
		stage.draw();
		batch.end();

	}

	@Override
	public boolean keyDown(int keycode) {
		if( keycode == Keys.W ) mapCamera.position.y -= 1;
		if( keycode == Keys.S ) mapCamera.position.y += 1;
		if( keycode == Keys.A ) mapCamera.position.x += 1;
		if( keycode == Keys.D ) mapCamera.position.x -= 1;
		if( keycode == Keys.SPACE ) this.moveMode = !this.moveMode;
		
		return false;
	}


	private boolean IsValidTile( Vector3 tile )
	{
		int mapw = (Integer) currentMap.getProperties().get("width");
		int maph = (Integer) currentMap.getProperties().get("height");
		return tile.x >= 0 && tile.x < mapw &&
			   tile.y >= 0 && tile.y < maph;
	}
	
	int selectedTileIndex = 0;
	int prevx, prevy;

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		prevx = screenX;
		prevy = screenY;

		if( moveMode == false)
		{
			// get world coordinates
			Vector3 clickPos = mapCamera.unproject(new Vector3( screenX, screenY, 0f));
			
			// tilemap is at 0, round to get tile pos
			clickPos.x = (float) Math.floor(clickPos.x);
			clickPos.y = (float) Math.floor(clickPos.y);
			
			// if it's a valid tile of our map
			if( IsValidTile( clickPos ) )
			{
				TiledMapTileLayer ml = (TiledMapTileLayer) currentMap.getLayers().get(0);
				Cell c = ml.getCell(((int)clickPos.x), ((int)clickPos.y));
				c.setTile(tiles.get(selectedTileIndex));
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
			mapCamera.position.x += dx * speed * currentZoom;
			mapCamera.position.y -= dy * speed * currentZoom;
			System.out.println(dx);
		}
		touchDown(screenX, screenY, pointer, 0);

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
