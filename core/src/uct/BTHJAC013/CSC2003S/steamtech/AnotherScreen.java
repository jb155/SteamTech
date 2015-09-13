package uct.BTHJAC013.CSC2003S.steamtech;

/**
 * Created by Jacques on 9/13/2015.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class AnotherScreen implements Screen {

    private Batch batch;
    private int mapWidth = 15;
    private int mapHeight = 10;
    private int tileSize = 30;
    MyGame game; // Note it's "MyGame" not "Game"

    private Texture wall;

    private int[][] map;

    // constructor to keep a reference to the main Game class
    public AnotherScreen(MyGame game){
        this.game = game;
        wall = new Texture("/images/units/wallBasic.png");
        //screenWidth = Gdx.graphics.getWidth();
        //screenHeight = Gdx.graphics.getHeight();

    }

    private void generateMap(){
        try {
            Scanner sc = new Scanner(new FileReader("/maps/map1.txt"));
        }catch (IOException e){
            System.out.println(e);
        }
    }

    @Override
    public void render(float delta) {
        // update and draw stuff
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.enableBlending();
        batch.begin();

        // draw tile map
        // go over each row bottom to top
        for(int y = 0; y < mapHeight; y++) {
            // go over each column left to right
            for(int x = 0; x < mapWidth; x++) {
                // wall
                if(map[x][y] == 1) {
                    batch.draw(wall, x * tileSize, y * tileSize);
                }
                // draw other types here...
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
