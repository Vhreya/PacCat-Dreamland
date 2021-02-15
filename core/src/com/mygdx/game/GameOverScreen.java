package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverScreen extends ScreenAdapter {
    private final SpriteBatch batch = new SpriteBatch();
    private final Texture gameover = new Texture("GameOver.png");

    public GameOverScreen() {


    }


    @Override
    public void render(float delta) {
        super.render(delta);


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(gameover, 0.0f, 0.0f);
        batch.end();
    }
}