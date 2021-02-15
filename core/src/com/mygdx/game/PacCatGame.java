package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

public class PacCatGame extends Game {

	public static PacCatGame INSTANCE;
	public static AssetManager manager;

	public PacCatGame() {
		INSTANCE = this;
	}

	@Override
	public void create() {
		manager = new AssetManager();
		setScreen(new LoadingScreen());
	}
}
