package com.ufofrog.tmxmob;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class TmxMobApp extends ApplicationAdapter implements InputProcessor {
	
	// MAP
	private TiledMap map;
	private TiledMapRenderer renderer;

	private OrthographicCamera mapCamera;
	FitViewport mapViewport;

	private OrthographicCamera camera;
	
	private AssetManager assetManager;
	
	// TILE PALETTE
	Array<TextureRegion> editsprites = new Array<TextureRegion>();
	//Array<Sprite> editsprites = new Array<Sprite>();
	Array<TiledMapTile> edittiles = new Array<TiledMapTile>();
	
	// GDX GUI
	private Stage stage;
	private Slider slider;
	private ShapeRenderer shaperenderer;
	
	InputMultiplexer imux;
	
	// GDX DRAWING
	private BitmapFont font;
	private SpriteBatch batch;
	
	// CAMERA
	private float mapUnitScale = 1f / 64f;
	private float currentZoom = 1f;
	private float maxZoom = 3f;
	private float minZoom = 0.5f;
	private float zoomStep = 0.01f;
	private Skin skin;
	//private FitViewport viewport;
	
	boolean modo_mover = false;
	
	
	@Override
	public void create () {
		
		batch2 = new SpriteBatch();
		
		Gdx.input.setInputProcessor(this);
		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		mapCamera = new OrthographicCamera((w / h) * 10, 10);
		mapCamera.zoom = currentZoom;
		mapCamera.update();
		mapViewport = new FitViewport((w/h)*10, 10, mapCamera);
		
		camera = new OrthographicCamera((w / h) * 10, 10);
		camera.zoom = 2322;
		camera.update();
		//viewport = new FitViewport((w/h)*10, 10, camera);

		font = new BitmapFont();
		batch = new SpriteBatch();
		
		imux = new InputMultiplexer();
		stage = new Stage();
		imux.addProcessor(this);
		imux.addProcessor(stage);
		Gdx.input.setInputProcessor(imux);
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		slider = new Slider(minZoom, maxZoom, zoomStep, false, skin);
		stage.addActor(slider);
		slider.setValue(currentZoom);
		slider.setPosition(72f, 6f);
		
		shaperenderer = new ShapeRenderer();

		// map
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(
				new InternalFileHandleResolver()));
		assetManager.load("maps/map0.tmx", TiledMap.class);
		assetManager.finishLoading(); 
		map = assetManager.get("maps/map0.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, mapUnitScale);

		for( TiledMapTile tmt : map.getTileSets().getTileSet(0) )
		{
			TextureRegion tr = tmt.getTextureRegion();
			tr.getTexture();
			Sprite sp = new Sprite();
			sp.setPosition(0, 0);
			sp.setRegion(tr);
			
			this.edittiles.add(tmt);
			this.editsprites.add(tr);
			//this.editsprites.add(tr);
			
		}
		mapCamera.position.x = mapCamera.position.x - 4f;
		mapCamera.position.y = mapCamera.position.y - 1f;
		
		slider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor)
			{
				mapCamera.zoom = slider.getValue();
			}
		});
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

	SpriteBatch batch2;
	@Override
	public void render () {
		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		mapCamera.update();

		renderer.setView(mapCamera);
		renderer.render();

		//batch.setProjectionMatrix(camera.combined);
		batch.begin();
		stage.draw();
		batch.end();

		batch.begin();
		for( int i = 0; i < editsprites.size-1; i++ )
		{
			//editsprites.get(i).scale((float) 0.00000001);
			//batch.draw(editsprites.get(i), Gdx.graphics.getWidth() - 64f, i*64f);
			batch.draw(editsprites.get(i), 0f, i*64f);
			//editsprites.get(i).draw(batch);
			//batch.draw(editsprites.get(i), i*64f, 0f);
		}
		batch.end();

	}

	@Override
	public boolean keyDown(int keycode) {
		if( keycode == Keys.W ) mapCamera.position.y -= 1;
		if( keycode == Keys.S ) mapCamera.position.y += 1;
		if( keycode == Keys.A ) mapCamera.position.x += 1;
		if( keycode == Keys.D ) mapCamera.position.x -= 1;
		if( keycode == Keys.SPACE ) this.modo_mover = !this.modo_mover;
		
		return false;
	}


	private boolean IsValidTile( Vector3 tile )
	{
		int mapw = (Integer) map.getProperties().get("width");
		int maph = (Integer) map.getProperties().get("height");
		return tile.x >= 0 && tile.x < mapw &&
			   tile.y >= 0 && tile.y < maph;
	}
	
	Vector3 testPoint = new Vector3();
	BoundingBox bb = new BoundingBox();

	int CheckClickInPalette( float x, float y )
	{
		int nt = editsprites.size;
		for( int i = 0; i < editsprites.size; i++ )
		{
			bb.min.x = 0f;
			bb.min.y = (nt-i-2) * 64f;
			//Vector3 umin = mapCamera.unproject(bb.min);
			
			bb.max.x = 64f;
			bb.max.y = (nt-i+1-2) * 64f;
			//Vector3 umax = mapCamera.unproject(bb.max);
			
			//bb.set(umin, umax);

			testPoint.x = x;
			testPoint.y = y;
			if( bb.contains(testPoint) ) return i;
		}
		return -1;
	}
	
	int selectedTileIndex = 0;
	int prevx, prevy;

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		prevx = screenX;
		prevy = screenY;

		if( modo_mover == false)
		{
		System.out.println("screenXY: " + screenX + ", " + screenY);
		int justSelectedTileIndex = CheckClickInPalette(screenX, screenY);

		if( justSelectedTileIndex != -1 )
		{
			selectedTileIndex = justSelectedTileIndex;
		}

		// get world coordinates
		Vector3 clickPos = mapCamera.unproject(new Vector3( screenX, screenY, 0f));
		
		// tilemap is at 0, round to get tile pos
		clickPos.x = (float) Math.floor(clickPos.x);
		clickPos.y = (float) Math.floor(clickPos.y);
		
		// if it's a valid tile of our map
		if( IsValidTile( clickPos ) )
		{
			TiledMapTileLayer ml = (TiledMapTileLayer) map.getLayers().get(0);
			Cell c = ml.getCell(((int)clickPos.x), ((int)clickPos.y));
			c.setTile(edittiles.get(selectedTileIndex));
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
	boolean startNoMove = false;
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if( this.modo_mover == true )
		{
		int dx = prevx - screenX;
		int dy = prevy - screenY;
		prevx = screenX;
		prevy = screenY;
		mapCamera.position.x += dx * speed * currentZoom;
		mapCamera.position.y -= dy * speed * currentZoom;
		System.out.println(dx);
		}
		startNoMove = true;
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
