package uct.BTHJAC013.CSC2003S.steamtech;

/**
 * Created by Jacques on 9/13/2015.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import javax.swing.*;


public class MainMenuScreen implements Screen {


    MyGame game; // Note it's "MyGame" not "Game"
    private SimpleButton newGame;
    private SimpleButton about;
    private SimpleButton exit;
    private SpriteBatch batch;
    private  Texture background;

    // constructor to keep a reference to the main Game class
    public MainMenuScreen(MyGame game){
        this.game = game;
        background = new Texture ("MainMenu.png");
        newGame = new SimpleButton(new Texture("NewGameButton.png"),260,100,100,50);
        about = new SimpleButton(new Texture("AboutButton.png"),260,50,100,50);
        exit = new SimpleButton(new Texture("ExitButton.png"),260,0,100,50);
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        batch.begin();
            batch.draw(background,0,0,660,350);
            //new game
            newGame.update(batch);
            //About
            about.update(batch);
            //Exit
            exit.update(batch);
        batch.end();

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (newGame.checkIfClicked(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                game.create();
                game.setScreen(game.gameScreen);
            }
        }
        /*if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (newGame.checkIfClicked(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                game.setScreen(game.gameScreen);
            }
        }*/
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (exit.checkIfClicked(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                if ( JOptionPane.showConfirmDialog(null, "Are you sure you wish to abandon your duties Sir?", "Leaving so soon?", JOptionPane.YES_NO_OPTION)==0) {
                    System.exit(0);
                }
            }
        }
    }


    @Override
    public void resize(int width, int height) {
    }


    @Override
    public void show() {
        // called when this screen is set as the screen with game.setScreen();
    }


    @Override
    public void hide() {
        // called when current screen changes from this to a different screen
    }


    @Override
    public void pause() {
    }


    @Override
    public void resume() {
    }


    @Override
    public void dispose() {
        // never called automatically
    }
}
