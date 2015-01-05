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
		int i = 0;
		TiledMapTileSet tileset = map.getTiledMap().getTileSets().getTileSet(0);
		tableui.clearChildren();
		tilePaletteScrollPane.setWidth(map.getTileWidth());
		for( TiledMapTile tmt : tileset )
		{
			TextureRegion tr = tmt.getTextureRegion();
			ImageButtonStyle ims = new ImageButtonStyle();
			ims.imageUp = new TextureRegionDrawable(tr);
			ims.imageDown = new TextureRegionDrawable(tr);
			ImageButton img = new ImageButton(ims);
			tileImages.add(img);
			img.setUserObject(((Integer)i));
			img.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					int val = ((Integer)event.getListenerActor().getUserObject());
					selectedTileIndex = val;
				}
			});
			tableui.add(img).padLeft(0).pad(0).bottom();
			tableui.row();
			this.tiles.add(tmt);
			i++;
		}

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
