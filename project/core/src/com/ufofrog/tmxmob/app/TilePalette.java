package com.ufofrog.tmxmob.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.ufofrog.tmxmob.map.MapHolder;

public class TilePalette {
	// TILE PALETTE
	Array<TiledMapTile> tiles = new Array<TiledMapTile>();
	Array<ImageButton> tileImages = new Array<ImageButton>();
	int selectedTileIndex = 0;
	Table tileContainerTable;
	private ScrollPane tilePaletteScrollPane;
	private Table containerTable;

	public TilePalette()
	{
		tileContainerTable = new Table();
		tilePaletteScrollPane = new ScrollPane(tileContainerTable);
		tilePaletteScrollPane.setPosition(0f, 0f);
	}
	
	public ScrollPane getScrollPane()
	{
		return tilePaletteScrollPane;
	}
	
	public Table getTable()
	{
		return tileContainerTable;
	}
	
	public void loadFromMap( MapHolder map )
	{
		selectedTileIndex = 0;

		// get tileset
		TiledMapTileSet tileset = map.getTiledMap().getTileSets().getTileSet(0);
		
		// update ui elements
		tileContainerTable.clearChildren();
		tileContainerTable.clear();
		this.containerTable.clear();
		//tableui.setWidth(1000f);//map.getTileWidth());

		tileImages.clear();
		tiles.clear();
		
		// create tile ui elements
		int i = 0;
		for( TiledMapTile tmt : tileset )
		{
			// create ui element
			TextureRegion tr = tmt.getTextureRegion();
			ImageButtonStyle ims = new ImageButtonStyle();
			ims.imageUp = new TextureRegionDrawable(tr);
			ims.imageDown = new TextureRegionDrawable(tr);
			ImageButton img = new ImageButton(ims);

			// set props
			img.setUserObject(((Integer)i));
			img.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					int val = ((Integer)event.getListenerActor().getUserObject());
					selectedTileIndex = val;
				}
			});
			
			// positioning
			tileContainerTable.add(img);
			if( i % 3 == 0 ) tileContainerTable.row().top();
			
			// add to lists
			tileImages.add(img);
			this.tiles.add(tmt);

			System.out.println(i);
			i++;
		}

		// position table
		tileContainerTable.left().center().pad(0);
		tileContainerTable.setWidth(map.getTileWidth());

		//containerTable.clear();
		containerTable.setFillParent(true);
		getScrollPane().setScrollPercentY(1000f);
		containerTable.center().left();
		containerTable.add(getScrollPane()).align(Align.center);
		
		System.out.println("height: " + this.getScrollPane().getHeight());
		//this.getScrollPane().setHeight(100);

	}

	public TiledMapTile getSelectedTile() {
		return tiles.get(selectedTileIndex);
	}

	public Actor getUITableTileContainer() {
		return tileContainerTable;
	}
	
	public void setContainerTable(Table t) {
		containerTable = t;
	}
	
	
}
