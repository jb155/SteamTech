package uct.BTHJAC013.CSC2003S.steamtech;

/**
 * Created by Jacques on 9/13/2015.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import javax.swing.*;


public class MainMenuScreen implements Screen {


    MyGame game; // Note it's "MyGame" not "Game"
    private SimpleButton newGame;
    private SimpleButton exit;
    private SpriteBatch batch;
    private  Texture background;

    private SimpleButton aboutButton;
    private Music backgroundMusic;


    // constructor to keep a reference to the main Game class
    public MainMenuScreen(MyGame game){
        this.game = game;
        background = new Texture ("MainMenu/MainMenu.png");
        newGame = new SimpleButton(new Texture("NewGameButton.png"),260,100,100,50);
        aboutButton = new SimpleButton(new Texture("AboutButton.png"),260,50,100,50);
        exit = new SimpleButton(new Texture("ExitButton.png"),260,0,100,50);

        batch = new SpriteBatch();

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/Industrial Revolution.mp3"));
        // start the playback of the background music immediately

        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.8f);

        backgroundMusic.play();

    }

    @Override
    public void render(float delta) {
        batch.begin();
            batch.draw(background, 0, 0, 660, 350);
            //new game
            newGame.update(batch);
            //About
            aboutButton.update(batch);
            //Exit
            exit.update(batch);
        batch.end();

        //New Game
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            //New Game
            if (newGame.checkIfClicked(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                backgroundMusic.stop();
                game.gameScreen = new GameScreen(game);
                game.setScreen(game.gameScreen);
            }
            //About
            if (aboutButton.checkIfClicked(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                game.setScreen(game.aboutScreen);
            }
            //Exit
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
        backgroundMusic.play();
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
