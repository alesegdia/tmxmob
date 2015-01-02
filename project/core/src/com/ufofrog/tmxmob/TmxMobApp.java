package com.ufofrog.tmxmob;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.Array;

public class TmxMobApp extends ApplicationAdapter implements InputProcessor {
	
	private TiledMap map;
	private TiledMapRenderer renderer;
	private OrthographicCamera camera;
	private AssetManager assetManager;
	private BitmapFont font;
	private SpriteBatch batch;
	
	private float mapUnitScale = 1f / 64f;
	private float currentZoom = 1f;
	
	Array<TextureRegion> editsprites = new Array<TextureRegion>();
	Array<TiledMapTile> edittiles = new Array<TiledMapTile>();
	private Texture maptex;
	
	@Override
	public void create () {
		Gdx.input.setInputProcessor(this);
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, (w / h) * 10, 10);
		camera.zoom = currentZoom;
		camera.update();
		font = new BitmapFont();
		batch = new SpriteBatch();
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(
				new InternalFileHandleResolver()));
		assetManager.load("maps/map0.tmx",
				TiledMap.class);
		assetManager.finishLoading();
		map = assetManager.get("maps/map0.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, mapUnitScale);

		for( TiledMapTile tmt : map.getTileSets().getTileSet(0) )
		{
			TextureRegion tr = tmt.getTextureRegion();
			maptex = tr.getTexture();
			this.edittiles.add(tmt);
			this.editsprites.add(tr);
		}
		camera.position.x = camera.position.x - 4f;
		camera.position.y = camera.position.y - 1f;
		
	}

	void update()
	{
		
	}


	void DrawAvailableTiles()
	{
		for( int i = 0; i < editsprites.size; i++ )
		{
			batch.draw(editsprites.get(i), i*64f, 320f-64f);
		}
	}
	
	@Override
	public void render () {
		update();
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		renderer.setView(camera);
		renderer.render();
		batch.begin();
		DrawAvailableTiles();
		font.draw(batch, "SAMPLE TILESET BY NOSGHY", 10, 20);
		batch.end();
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
		for( int i = 0; i < editsprites.size; i++ )
		{
			bb.min.x = i * 64f;
			bb.min.y = 0f;
			bb.max.x = (i+1) * 64f;
			bb.max.y = 64f;

			testPoint.x = x;
			testPoint.y = y;
			if( bb.contains(testPoint) ) return i;
		}
		return -1;
	}
	
	int selectedTileIndex = 0;
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		System.out.println("screenXY: " + screenX + ", " + screenY);
		int justSelectedTileIndex = CheckClickInPalette(screenX, screenY);

		if( justSelectedTileIndex != -1 )
		{
			selectedTileIndex = justSelectedTileIndex;
		}

		// get world coordinates
		Vector3 clickPos = camera.unproject(new Vector3( screenX, screenY, 0f));
		
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
