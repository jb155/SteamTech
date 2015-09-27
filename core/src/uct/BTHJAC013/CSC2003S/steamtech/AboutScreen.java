package uct.BTHJAC013.CSC2003S.steamtech;

/**
 * Created by Jacques on 9/13/2015.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class AboutScreen implements Screen {
    private MyGame game;
    private SimpleButton back;
    private Texture aboutTex;
    private SpriteBatch batch;

    // constructor to keep a reference to the main Game class
    public AboutScreen(MyGame game){
        this.game = game;

        back = new SimpleButton(new Texture("BackToLobbyButton.png"),550,0,100,50);
        aboutTex = new Texture("MainMenu/About.png");

        batch = new SpriteBatch();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.begin();
            batch.draw(aboutTex, 0, 0, 660, 350);
            back.update(batch);
        batch.end();

        //Back
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (back.checkIfClicked(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                game.setScreen(game.mainMenuScreen);
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }


}
