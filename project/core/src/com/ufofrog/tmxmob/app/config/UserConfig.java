package com.ufofrog.tmxmob.app.config;

import java.io.IOException;
import java.io.Writer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class UserConfig {

	
	public UserConfigParams params;

	public UserConfig()
	{
		params = new UserConfigParams();
		Defaults();
	}
	
	public void Defaults()
	{
		params.stageZoom = 500;
	}
	
	public void LoadFromDisk()
	{
		LoadFromJSON(StaticConfig.JSON_CONFIG_FILEPATH);
	}
	
	void LoadFromJSON(String path)
	{
		FileHandle fh = Gdx.files.external(path);
		Json json = new Json();
		if( fh.exists() )
		{
			//System.out.println(fh.readString());
			params = json.fromJson(UserConfigParams.class, fh.readString());
		}
	}

	public void SaveToDisk()
	{
		this.SaveToJSON(StaticConfig.JSON_CONFIG_FILEPATH);
	}

	void SaveToJSON(String path)
	{
		FileHandle fh = Gdx.files.external(path);
		Json json = new Json();
		try {
			Writer fw = fh.writer(false);
			json.toJson(fh);
			System.out.println(json.toJson(this.params));
			fw.write(json.toJson(this.params));
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
