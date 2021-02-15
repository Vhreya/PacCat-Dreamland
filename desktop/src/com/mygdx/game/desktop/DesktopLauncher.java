package com.mygdx.game.desktop;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.mygdx.game.PacCatGame;

public class DesktopLauncher {

	public static void main (String[] arg) {
		TexturePacker.Settings settings = new TexturePacker.Settings();
		settings.maxHeight = 4096;
		settings.maxWidth = 4096;
		settings.edgePadding = true;
		settings.duplicatePadding = true;
		settings.filterMin = Texture.TextureFilter.Linear;
		settings.filterMag = Texture.TextureFilter.Linear;
		TexturePacker.process("Background", ".", "background");
		TexturePacker.process("PacCat", ".", "pacCat");
		TexturePacker.process("Ghosts", ".", "ghosts");
		TexturePacker.process("EmpoweredPacCat-right", ".", "empoweredPacCat-right");
		TexturePacker.process("EmpoweredPacCat-left", ".", "empoweredPacCat-left");
		TexturePacker.process("EmpoweredPacCat-middle", ".", "empoweredPacCat-middle");


		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowIcon("EmpoweredPacCat-middle/glittercatMiddle_1.png");
		config.setTitle("PacCat Dreamland");
		config.useVsync(true);
		config.setForegroundFPS(60);
		//config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode(Lwjgl3ApplicationConfiguration.getPrimaryMonitor()));
		config.setWindowedMode(930, 840);
		config.setResizable(false);
		new Lwjgl3Application(new PacCatGame(), config);
	}
}
