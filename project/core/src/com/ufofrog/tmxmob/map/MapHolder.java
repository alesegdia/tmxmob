package com.ufofrog.tmxmob.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector3;
import com.ufofrog.tmxmob.app.TilePalette;
import com.ufofrog.tmxmob.app.config.StaticConfig;

public class MapHolder {

	
	public TiledMap currentMap;
	private TiledMapRenderer mapRenderer;
	private AssetManager assetManager;
	public TilePalette tilePalette;
	
	// new map stuff
	private Texture tilesTex;
	TextureRegion[][] splitTiles;

	public MapHolder( TilePalette tilePalette ) {
		this.tilePalette = tilePalette;
	}
	
	public void LoadFile(String mapfile, String base, FileHandleResolver fhr)
	{
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(fhr));
		assetManager.load(mapfile, TiledMap.class);
		assetManager.finishLoading(); 
		currentMap = assetManager.get(base + mapfile);
		mapRenderer = new OrthogonalTiledMapRenderer(currentMap, 1f/this.getTileWidth());
		tilePalette.loadFromMap(this);
	}

	public void CreateFromScratch(MapParams params)
	{
		tilesTex = new Texture(params.imgfile);
		int numTilesWidth = tilesTex.getWidth() / params.tilewidth;
		int numTilesHeight = tilesTex.getHeight() / params.tileheight;

		splitTiles = TextureRegion.split(tilesTex, params.tilewidth, params.tileheight);
		currentMap = new TiledMap();
		TiledMapTileLayer layer = new TiledMapTileLayer(
				params.mapwidth, params.mapheight,
				params.tilewidth, params.tileheight);
		currentMap.getLayers().add(layer);

		TiledMapTileSet tileset = new TiledMapTileSet();
		currentMap.getTileSets().addTileSet(tileset);

		tileset.getProperties().put("imagesource", params.imgfile.name());
		tileset.getProperties().put("imagewidth", tilesTex.getWidth());
		tileset.getProperties().put("imageheight", tilesTex.getHeight());
		tileset.setName("pueblopaleta1");
		for( int i = 0; i < splitTiles.length; i++ )
		{
			for( int j = 0; j < splitTiles[0].length; j++ )
			{
				System.out.println( i + j * numTilesWidth );
				TiledMapTile tile = new StaticTiledMapTile(splitTiles[i][j]);
				tile.setId(j + i * numTilesWidth + 1);
				tileset.putTile(j + i * numTilesWidth + 1, tile);
			}
		}
		
		layer.setName("layer1");
		for( int x = 0; x < params.mapwidth; x++ )
		{
			for(int y = 0; y < params.mapheight; y++ )
			{
				Cell cell = new Cell();
				cell.setTile(tileset.getTile(1));
				layer.setCell(x, y, cell);
			}
		}

		tileset.getProperties().put("tilewidth", params.tilewidth);
		tileset.getProperties().put("tileheight", params.tilewidth);
		tileset.getProperties().put("name", params.imgfile.name());

		currentMap.getProperties().put("width", params.mapwidth);
		currentMap.getProperties().put("height", params.mapheight);
		currentMap.getProperties().put("tilewidth", params.tilewidth);
		currentMap.getProperties().put("tileheight", params.tileheight);
		tileset.getProperties().put("spacing", 0);
		tileset.getProperties().put("margin", 0);

		mapRenderer = new OrthogonalTiledMapRenderer(currentMap, 1f/this.getTileWidth());
		tilePalette.loadFromMap(this);

	}
	
	public void LoadInternalFile(String mapfile)
	{
		LoadFile(mapfile, StaticConfig.INTERNAL_BASE_PATH, new InternalFileHandleResolver());
	}
	
	public void LoadExternalFile(String mapfile)
	{
		LoadFile(mapfile, "", new ExternalFileHandleResolver());
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

	public boolean IsValidTile(Vector3 tile) {
		int mapw = (Integer) getWidth();
		int maph = (Integer) getHeight();
		return tile.x >= 0 && tile.x < mapw &&
			   tile.y >= 0 && tile.y < maph;
	}

	
	
}
