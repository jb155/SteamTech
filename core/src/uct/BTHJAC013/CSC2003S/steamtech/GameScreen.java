package uct.BTHJAC013.CSC2003S.steamtech;

/**
 * Created by Jacques on 9/13/2015.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import javax.swing.*;
import java.util.ArrayList;


public class GameScreen implements Screen {

    private Batch batch;
    private int mapWidth = 22;
    private int mapHeight = 10;
    private int tileSize = 30;
    private int toolbarHieght = 50;
    private MyGame game;

    private Texture wallTexture;
    private Texture baseTexture;
    private Texture enemySpawnTexture;
    private Sprite floorSprite;

    private Sprite mainHUD;


    private Sprite turretMountSprite;

    private Sprite towerRangeRaius;

    //Classes
    private Map map;

    private int towerDBCount = 2;
    private int towerCount = 2;
    private TowerObject[] towerDB;
    private TowerObject[] towerIngameDB;
    private ArrayList<TowerObject> towersOnField;

    private ArrayList<EnemyUnit> enemyUnitsOnField;
    private int enemyWaveCount = 5;
    private int waveGrowthNum = 3;
    private int currentWaveNumber = 0;
    private int waveNum = 0;
    private int spawnRate = 1000;
    private long mosnsterLastSpawned = 0;
    private boolean spawnMonsters = false;
    private int enemyTypes = 2;

    //controls
    private boolean leftMouseDown = false;
    private int buildTowerSelected = -1;
    private TowerObject tempTower;
    private SimpleButton readyButton;

    //Game values
    private int steamPoints = 10;
    private int baseHP = 10;

    //HUD
    private BitmapFont font;

    // constructor to keep a reference to the main Game class
    public GameScreen(MyGame game){
        this.game = game;
        batch = new SpriteBatch();
        wallTexture = new Texture("wallBasic.png");
        baseTexture = new Texture("base.png");
        enemySpawnTexture = new Texture("enemySpawn.png");
        floorSprite = new Sprite(new Texture("floor.png"));
        floorSprite.setSize(tileSize,tileSize);

        towerRangeRaius = new Sprite (new Texture("selectRing.png"));
        towerRangeRaius.setScale(0.1f);

        turretMountSprite = new Sprite(new Texture("TurretPlatform.png"));
        turretMountSprite.setSize(tileSize,tileSize);

        mainHUD = new Sprite(new Texture("HUD.png"));

        //screenWidth = Gdx.graphics.getWidth();
        //screenHeight = Gdx.graphics.getHeight();
        map = new Map(mapWidth, mapHeight);

        //HUD
        //Steam Points
        font = new BitmapFont();
        font.setColor(Color.RED);

        //Ready button
        readyButton = new SimpleButton(new Texture("ReadyButton.png"), 280, 0, 100, 50);

        //Enemies
        enemyUnitsOnField = new ArrayList<EnemyUnit>();

        setupTowers();
        System.out.println("Towers setup completed");
    }

    private void setupTowers(){
        //setup arraylist to handel towers on field
        towersOnField = new ArrayList<TowerObject>();

        //Setup database
        towerDB = new TowerObject[towerDBCount];
        for (int i = 0; i < towerDBCount; i++){
            towerDB[i] = new TowerObject(i,tileSize,tileSize);
        }

        //Select random 3 towers
        ArrayList<Integer>temp = new ArrayList<Integer>();
        towerIngameDB = new TowerObject[towerCount];
        for(int i = 0; i < towerCount; i++){
            boolean run = true;
            while (run){
                int randomTower = (int )(Math.random() * towerDBCount);
                if(!temp.contains(randomTower)){
                    //ie this tower hsnt been added to the list
                    temp.add(randomTower);
                    run = false;
                    towerIngameDB[i] = towerDB[randomTower];
                }
            }

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
        for (int y = 0; y < mapHeight; y++) {
            // go over each column left to right
            for (int x = 0; x < mapWidth; x++) {
                // Floor
                if (map.playingField[x][y] == 0) {
                    floorSprite.setPosition(x * tileSize, y * tileSize + toolbarHieght);
                    floorSprite.draw(batch);
                }
                // wall
                if (map.playingField[x][y] == 1) {
                    batch.draw(wallTexture, x * tileSize, y * tileSize + toolbarHieght);
                }
                // Turret Mounting point
                if (map.playingField[x][y] == 2) {
                    turretMountSprite.setPosition(x * tileSize, y * tileSize + toolbarHieght);
                    turretMountSprite.draw(batch);
                    //batch.draw(turretMountTexture, x * tileSize, y * tileSize + toolbarHieght);
                }
                //enemySpawn
                if (map.playingField[x][y] == -1) {
                    batch.draw(enemySpawnTexture, x * tileSize, y * tileSize + toolbarHieght);
                }
                //basse
                if (map.playingField[x][y] == -2) {
                    batch.draw(baseTexture, x * tileSize, y * tileSize + toolbarHieght);
                }
                // draw other types here...
            }
        }
        //Enemies
        if((spawnMonsters)&&(waveNum<=enemyWaveCount)&&(currentWaveNumber<waveGrowthNum*waveNum)){
            //spawn monsters;
            if(System.currentTimeMillis()>mosnsterLastSpawned+spawnRate){
                mosnsterLastSpawned = System.currentTimeMillis();
                enemyUnitsOnField.add(new EnemyUnit((int)(Math.random()*enemyTypes),20,20,map.getSpawnPoint()));
                currentWaveNumber++;
                System.out.println ("Spawn enemy");
            }
        }
        //go through enemies and check if dead (if they are...remove them and also render them
        for (int i = 0; i < enemyUnitsOnField.size(); i++){
            enemyUnitsOnField.get(i).getSprite().draw(batch);
            if(!enemyUnitsOnField.get(i).tick()){
                enemyUnitsOnField.remove(i);
            }
        }

        //Towers
        for (int i = 0; i < towersOnField.size(); i++) {
            TowerObject temp = towersOnField.get(i);

            //temp.gameTick(enemyUnitsOnField);

            temp.getSprite(temp.getxPos() * tileSize, temp.getyPos() * tileSize + toolbarHieght).draw(batch);
            //batch.draw(temp.getSprite(), temp.getxPos(), temp.getyPos()+toolbarHieght,30,30);
            ArrayList<Projectile> tempBullets = towersOnField.get(i).gameTick(enemyUnitsOnField);   //Get all the bullets of the tower and render its bullets
            for(Projectile p: tempBullets){
                p.getSprite().draw(batch);
            }
        }

        if (leftMouseDown) {
            int x = Gdx.input.getX() - tempTower.getSpriteWidth() / 2;
            int y = Gdx.graphics.getHeight() - Gdx.input.getY() - tempTower.getSpriteWidth() / 2;
            //radius
            towerRangeRaius.setScale(tempTower.getRadius() / 15);
            towerRangeRaius.setPosition(Gdx.input.getX() - (towerRangeRaius.getWidth() / 2), Gdx.graphics.getHeight() - Gdx.input.getY() - (towerRangeRaius.getHeight() / 2));
            towerRangeRaius.draw(batch);
            //towerRangeRaius.setScale(0.1f);
            //tower
            tempTower.setPosition(x, y);
            tempTower.getSprite(x, y).draw(batch);
        }

        //Finally teh HUD
        //Hud
        mainHUD.setSize(660, 350);
        mainHUD.draw(batch);
        //towers
        for (int i = 0; i < towerIngameDB.length; i++) {
            //batch.draw(basicTurretTexture,0,0,50,50);
            towerIngameDB[i].updateButton(batch, i * 50 + 50, 0);
        }
        font.draw(batch, "SP:" + steamPoints, 600, 20);

        if(!spawnMonsters) {
            readyButton.update((SpriteBatch) batch);
        }

        batch.end();
        //END OF RENDER****************************************************************************************************************************

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  {
            for (int i = 0; i < towersOnField.size(); i++) {
                towersOnField.get(i).rotateTower(10);
            }
        }


        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            //runs though buttons
            for (int i = 0; i < towerCount; i++) {  //Steps through all the buttons
                if (towerIngameDB[i].checkIfClicked(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {

                    if (buildTowerSelected != i) {
                        buildTowerSelected = i;
                        try {
                            if (!leftMouseDown) {    //To avoid that if you move the selction over the other build options that it changes
                                tempTower = (TowerObject) towerIngameDB[i].clone();
                            }
                        } catch (Exception e) {

                        }
                    }
                    leftMouseDown = true;
                } else {

                }
            }
            //Upgrade dem tower
            if (!leftMouseDown) {    //This just makes sure that it doesn't try to upgrade the tower as the mouse moves over another tower (while trying to place something else)
                upgradeTower();
            }
            //Ready up
            //Check if player ready (to start next wave of enemies
            if (!leftMouseDown) {
                if ((readyButton.checkIfClicked(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) && !spawnMonsters) {
                    spawnMonsters = true;
                    waveNum++;
                    if (waveNum < enemyWaveCount) {
                        enemyWaveCount = waveNum * waveGrowthNum;
                    }
                }
            }
        } else {
            if (leftMouseDown) {
                addTurretToPlayingField();
                leftMouseDown = false;
            }
        }


        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            removeTurretToPlayingField();
        }

        //have the turrets do their things

    }

    private boolean yesNoDialogMessage(String header,String message){
        int output = JOptionPane.showConfirmDialog(null, message, header, JOptionPane.YES_NO_OPTION);
        if (output != 0) {
            return false;
        } else {
            return true;
        }
    }

    private void upgradeTower() {
        int x = Gdx.input.getX();
        int y = Gdx.graphics.getHeight() - Gdx.input.getY();
        //int x = Gdx.input.getX();
        //int y = Gdx.graphics.getHeight() - Gdx.input.getY();
        int dx = x / tileSize;
        int dy = (int) (y / tileSize + .25) - 2;

        //Valid spot
        if (((x < mapWidth * tileSize) && (y < mapHeight * tileSize)) && (dx > -1) && (dy > -1)) { //inside playing field
            int temp = map.playingField[dx][dy];  //get whats currently on that tile
            if (temp == 2) { //placed turret block procede to place
                //batch.draw(enemySpawnTexture, x * tileSize, y * tileSize + toolbarHieght);
                try {
                    for (int i = 0; i < towersOnField.size(); i++) {
                        int[] comparintTurret = towersOnField.get(i).getPosition();
                        if ((comparintTurret[0] == dx) && (comparintTurret[1] == dy)) {
                            //check for upgrades
                            if (yesNoDialogMessage("Upgrade turret?", "Do you wish to upgrade this turret?\nCost " + ((int) (tempTower.getCost() * +0.5)) + "SP\nDamage:\t# -> #\nRadius:\t# -> #\nRoF:\t# -> #")) {
                                //do upgrade things
                                steamPoints -= (int) (tempTower.getCost() * +0.5);
                            }
                        }
                    }
                }catch (Exception e){

                }
            }
        }
    }

    private void removeTurretToPlayingField(){
        int x = Gdx.input.getX();
        int y = Gdx.graphics.getHeight() - Gdx.input.getY();
        //int x = Gdx.input.getX();
        //int y = Gdx.graphics.getHeight() - Gdx.input.getY();
        int dx = x/tileSize;
        int dy = (int)(y/tileSize+.25)-2;

        //Removing of turret
        //Valid spot
        if(((x<mapWidth*tileSize)&&(y<mapHeight*tileSize))&&(dx>-1)&&(dy>-1)){ //inside playing field
            int temp = map.playingField[dx][dy];  //get whats currently on that tile
            if(temp == 2) { //placed turret block procede to place
                //batch.draw(enemySpawnTexture, x * tileSize, y * tileSize + toolbarHieght);
                try {
                    for (int i = 0; i < towersOnField.size();i++) {
                        int[] comparintTurret =towersOnField.get(i).getPosition();
                        if((comparintTurret[0]==dx)&&(comparintTurret[1]==dy)) {

                            //Are you sure?
                            if(yesNoDialogMessage("Remove turret?","Are you sure you wish to remove this turret?\nRecieve " + ((int)(towersOnField.get(i).getCost()/2))+"SP")) {
                                steamPoints+=(int)(towersOnField.get(i).getCost()/2);
                                towersOnField.remove(i);
                                map.playingField[dx][dy] = 1;
                                System.out.println("Remove turret to heap x:" + dx + "  y:" + dy);
                            }

                            //refund 50%
                        }
                    }
                }catch(Exception e){

                }
            }
        }
    }

    public void addTurretToPlayingField(){
        int x = Gdx.input.getX();
        int y = Gdx.graphics.getHeight() - Gdx.input.getY();
        //int x = Gdx.input.getX();
        //int y = Gdx.graphics.getHeight() - Gdx.input.getY();
        int dx = x/tileSize;
        int dy = (int)(y/tileSize+.25)-2;

        //Placing of turret
        //Valid spot
        if(((x<mapWidth*tileSize)&&(y<mapHeight*tileSize))&&(dx>-1)&&(dy>-1)){ //inside playing field
            int temp = map.playingField[dx][dy];  //get whats currently on that tile
            if(temp == 1) { //empty block procede to place
                if(steamPoints>=tempTower.getCost()){
                    //enough moneyz
                    //Are you sure?
                    if(yesNoDialogMessage("Place turret?","Are you sure you wish to contruct this turret?\nCost " + ((int)(tempTower.getCost()))+"SP")) {
                        steamPoints -= (int) (tempTower.getCost());
                        try {
                            tempTower.setPosition(dx, dy);
                            //tempTower.placeTower();
                            towersOnField.add(new TowerObject(tempTower.number, tempTower.getWidth(), tempTower.getHeight(), dx, dy,true));
                            //towersOnField.add((TowerObject)tempTower.clone());
                            map.playingField[dx][dy]=2;
                            System.out.println("Added turret to heap x:" + dx + "  y:" + dy);
                        }catch(Exception e){

                        }
                    }
                }else {
                    //notify player not enough mula
                }
            }else{

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
