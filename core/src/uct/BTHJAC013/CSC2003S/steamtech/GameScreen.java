package uct.BTHJAC013.CSC2003S.steamtech;

/**
 * Created by Jacques on 9/13/2015.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class GameScreen implements Screen {

    private Batch batch;
    private int mapWidth = 22;
    private int mapHeight = 10;
    private int tileSize = 30;
    private int toolbarHieght = 50;
    private MyGame game; // Note it's "MyGame" not "Game"

    private Texture wallTexture;
    private Texture baseTexture;
    private Texture enemySpawnTexture;
    private Texture floorTexture;

    private Texture towerAHUDTex;

    private Texture mouseTexture;
    private Sprite mouseSprite;
    //Classes
    private Map map;

    private int towerDBCount = 1;
    private int towerCount = 1;
    private TowerObject[] towerDB;
    private TowerObject[] towerIngameDB;

    //controls
    private boolean leftMouseDown = false;

    //HUD
    private SimpleButton TowerAButton;

    // constructor to keep a reference to the main Game class
    public GameScreen(MyGame game){
        this.game = game;
        batch = new SpriteBatch();
        wallTexture = new Texture("wallBasic.png");
        baseTexture = new Texture("base.png");
        enemySpawnTexture = new Texture("enemySpawn.png");
        floorTexture = new Texture("floor.png");
        towerAHUDTex = new Texture("towerBasicButton.png");
        //screenWidth = Gdx.graphics.getWidth();
        //screenHeight = Gdx.graphics.getHeight();
        map = new Map(mapWidth, mapHeight);

        mouseTexture = new Texture("mouseCursor.png");
        mouseSprite = new Sprite(mouseTexture);

        setupTowers();
    }

    private void setupTowers(){
        towerDB = new TowerObject[towerDBCount];
        for (int i = 0; i < towerDBCount; i++){
            towerDB[i] = new TowerObject(i);
        }

        //Select random 3 towers
        for(int i = 0; i < towerCount; i++){
            asdfhg
        }
        //basicTurretTexture = new Texture("towerBasic.png");
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
                // Floor
                if(map.playingField[x][y] == 0) {
                    batch.draw(floorTexture, x * tileSize, y * tileSize + toolbarHieght);
                }
                // wall
                if(map.playingField[x][y] == 1) {
                    batch.draw(wallTexture, x * tileSize, y * tileSize + toolbarHieght);
                }
                //enemySpawn
                if(map.playingField[x][y] == -1) {
                    batch.draw(enemySpawnTexture, x * tileSize, y * tileSize + toolbarHieght);
                }
                //basse
                if(map.playingField[x][y] == -2) {
                    batch.draw(baseTexture, x * tileSize, y * tileSize + toolbarHieght);
                }
                // draw other types here...
            }
        }

        //Hud
        for(int i = 0; i < towerIngameDB.length; i++){
            //batch.draw(basicTurretTexture,0,0,50,50);
            towerIngameDB[i].updateButton(batch,0,0);
        }


        if(leftMouseDown){
            mouseSprite.setPosition(Gdx.input.getX() - mouseSprite.getWidth()/2,Gdx.graphics.getHeight() - Gdx.input.getY() - mouseSprite.getHeight()/2);
            mouseSprite.draw(batch);
        }
        batch.end();


        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            if(TowerAButton.checkIfClicked(Gdx.input.getX() - mouseSprite.getWidth() / 2, Gdx.graphics.getHeight() - Gdx.input.getY() - mouseSprite.getHeight()/2)) {
                leftMouseDown = true;
            }
        }else{
            leftMouseDown = false;
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
