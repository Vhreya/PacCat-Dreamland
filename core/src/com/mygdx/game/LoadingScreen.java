package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class LoadingScreen extends ScreenAdapter {
    private SpriteBatch batch = new SpriteBatch();
    private Texture logo = new Texture("PacCatLogo.png");

    public LoadingScreen() {
        PacCatGame.manager.load("background.atlas", TextureAtlas.class);
        PacCatGame.manager.load("ghosts.atlas", TextureAtlas.class);
        PacCatGame.manager.load("pacCat.atlas", TextureAtlas.class);
        PacCatGame.manager.load("empoweredPacCat-right.atlas", TextureAtlas.class);
        PacCatGame.manager.load("empoweredPacCat-left.atlas", TextureAtlas.class);
        PacCatGame.manager.load("empoweredPacCat-middle.atlas", TextureAtlas.class);
        PacCatGame.manager.load("GameOver.png", Texture.class);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(logo, 0, 0);
        batch.end();

        if (PacCatGame.manager.update()) {
            PacCatGame.INSTANCE.setScreen(new GameScreen());
        }
    }
}
