package com.ufofrog.tmxmob.app;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MapHolder {

	public TiledMap currentMap;
	private TiledMapRenderer mapRenderer;
	private AssetManager assetManager;
	private float mapCamUnitScale;
	private TilePalette tilePalette;

	public MapHolder( float mapCamUnitScale, TilePalette tilePalette ) {
		this.mapCamUnitScale = mapCamUnitScale;
		this.tilePalette = tilePalette;
	}
	
	public void LoadFile(String mapfile, String base, FileHandleResolver fhr)
	{
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(fhr));
		assetManager.load(base + mapfile, TiledMap.class);
		assetManager.finishLoading(); 
		currentMap = assetManager.get(base + mapfile);
		mapRenderer = new OrthogonalTiledMapRenderer(currentMap, 1f/this.getTileWidth());
		tilePalette.loadFromMap(this);
	}
	
	public void LoadInternalFile(String mapfile)
	{
		LoadFile(mapfile, StaticConfig.INTERNAL_BASE_PATH, new InternalFileHandleResolver());
	}
	
	public void LoadExternalFile(String mapfile)
	{
		LoadFile(mapfile, StaticConfig.EXTERNAL_BASE_PATH, new ExternalFileHandleResolver());
	}

	public TiledMap getTiledMap() {
		return currentMap;
	}

	public void render(OrthographicCamera camera) {
		mapRenderer.setView(camera);
		mapRenderer.render();
	}
	
	public int getWidth()
	{
		return (Integer) currentMap.getProperties().get("width");
	}

	public int getHeight()
	{
		return (Integer) currentMap.getProperties().get("height");
	}

	public int getTileWidth() {
		return currentMap.getProperties().get("tilewidth", Integer.class);
	}

	public float getTileHeight() {
		return currentMap.getProperties().get("tileheight", Integer.class);
	}

	
	
}
