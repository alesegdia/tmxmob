package com.ufofrog.tmxmob.app;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.ufofrog.tmxmob.map.MapHolder;

public class TilePalette {
	// TILE PALETTE
	Array<TiledMapTile> tiles = new Array<TiledMapTile>();
	Array<ImageButton> tileImages = new Array<ImageButton>();
	int selectedTileIndex = 0;
	Table tableui;
	private ScrollPane tilePaletteScrollPane;

	public TilePalette()
	{
		tableui = new Table();
		tilePaletteScrollPane = new ScrollPane(tableui);
		tilePaletteScrollPane.setPosition(0f, 0f);
	}
	
	public ScrollPane getScrollPane()
	{
		return tilePaletteScrollPane;
	}
	
	public void loadFromMap( MapHolder map )
	{
		// get tileset
		TiledMapTileSet tileset = map.getTiledMap().getTileSets().getTileSet(0);
		
		// update ui elements
		tableui.clearChildren();
		tilePaletteScrollPane.setWidth(map.getTileWidth());
		
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
			tableui.add(img).padLeft(0).pad(0).bottom();
			tableui.row();
			
			// add to lists
			tileImages.add(img);
			this.tiles.add(tmt);

			System.out.println(i);
			i++;
		}

		// position table
		tableui.left();
		tableui.setPosition(0f, 0f);
		tableui.setFillParent(true);
		tableui.setWidth(map.getTileWidth());

	}

	public TiledMapTile getSelectedTile() {
		return tiles.get(selectedTileIndex);
	}

	public Actor getUITable() {
		return tableui;
	}
	
}
