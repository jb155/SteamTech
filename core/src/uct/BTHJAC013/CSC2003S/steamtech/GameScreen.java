package uct.BTHJAC013.CSC2003S.steamtech;

/**
 * Created by Jacques on 9/13/2015.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Intersector;

import javax.swing.*;
import java.util.ArrayList;


public class GameScreen implements Screen {

    private Batch batch;
    private int mapWidth = 22;
    private int mapHeight = 10;
    private int tileSize = 30;
    private int toolbarHieght = 50;
    private MyGame game;

    private boolean gameOver = false;
    private Texture gameOverTex;
    private boolean victory = false;
    private Texture victoryTex;
    private SimpleButton backToLobby;

    private Texture wallTexture;
    private Texture enemySpawnTexture;
    private Sprite floorSprite;

    private Sprite mainHUD;


    private Sprite turretMountSprite;

    private Sprite towerRangeRaius;

    //Classes
    private Map map;
    private Base base;


    private int towerDBCount = 4;
    private int towerCount = 3;
    private TowerObject[] towerDB;
    private TowerObject[] towerIngameDB;
    private ArrayList<TowerObject> towersOnField;

    private ArrayList<EnemyUnit> enemyUnitsOnField;
    private int totalEnemyWaves = 7;
    private int enemyWaveCount = 0;
    private int waveGrowthNum = 2;
    private int currentWaveNumber = 0;
    private int waveNum = 0;
    private int spawnRate = 1000;
    private long mosnsterLastSpawned = 0;
    private boolean spawnMonsters = false;
    private int enemyTypes = 4;

    //controls
    private boolean leftMouseDown = false;
    private int buildTowerSelected = -1;
    private TowerObject tempTower;
    private SimpleButton readyButton;

    //Game values
    private int steamPoints = 5;

    //HUD
    private BitmapFont font;

    //Collision
    Grid grid = new Grid(350,660);

    //Sounds and music
    private Music victoryFanfare;
    private Sound construction;
    private Sound tollBell;
    private Sound failed;
    private Sound SmallExplosion;

    private ArrayList <ExplosionEntity> explosions;

    // constructor to keep a reference to the main Game class
    public GameScreen(MyGame game){
        this.game = game;
        batch = new SpriteBatch();
        wallTexture = new Texture("wallBasic.png");
        enemySpawnTexture = new Texture("enemySpawnClosed.png");
        floorSprite = new Sprite(new Texture("floor.png"));
        gameOverTex = new Texture("MainMenu/YouFailedScreen.png");
        victoryTex = new Texture("MainMenu/Victory.png");
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
        font = new BitmapFont(Gdx.files.internal("courier.fnt"), false);
        font.setColor(Color.BLACK);

        //Ready button
        readyButton = new SimpleButton(new Texture("ReadyButton.png"), 280, 0, 100, 50);

        //Enemies
        enemyUnitsOnField = new ArrayList<EnemyUnit>();

        base = new Base(map.getBasePoint(), 9, "base.png");
        base.collidable.sprite.setPosition(base.getPos()[0] * 30, base.getPos()[1] * 30 + 35);
        base.collidable.bounding.setPosition(base.getPos()[0] * 30, base.getPos()[1] * 30 + 35);
        //base.collidable.sprite.setPosition(base.getPos()[0] * tileSize, base.getPos()[1] * tileSize + toolbarHieght);


        setupTowers();
        System.out.println("Towers setup completed");

        //Load sounds and music
        victoryFanfare = Gdx.audio.newMusic(Gdx.files.internal("sounds/victory_Fanfare.mp3"));
        construction = Gdx.audio.newSound(Gdx.files.internal("sounds/Construct.wav"));
        tollBell = Gdx.audio.newSound(Gdx.files.internal("sounds/tollBell.wav"));
        failed = Gdx.audio.newSound(Gdx.files.internal("sounds/failed.wav"));
        SmallExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/SmallExplosion.wav"));

        explosions = new ArrayList<ExplosionEntity>();

        batch = new SpriteBatch();
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(gameOver){   //did they lose?
            if(!victory) {      //A very dirty fix
                failed.play(1f);
                victory = true;
            }
            batch.begin();
                batch.draw(gameOverTex, 0, 0, 660, 350);

                backToLobby = new SimpleButton(new Texture("BackToLobbyButton.png"),520, 10 ,100, 40);
                backToLobby.update((SpriteBatch)batch);
            batch.end();
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if (backToLobby.checkIfClicked(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    game.setScreen(game.mainMenuScreen);
                }
            }
            System.out.println("You failed");
        }else  if(victory){ //win mebe?
            victoryFanfare.play();
            batch.begin();
                batch.draw(victoryTex, 0, 0, 660, 350);

                backToLobby = new SimpleButton(new Texture("BackToLobbyButton.png"),550, 10,100, 40);
                backToLobby.update((SpriteBatch)batch);
            batch.end();
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                if (backToLobby.checkIfClicked(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY())) {
                    game.setScreen(game.mainMenuScreen);
                }
            }
            //game.setScreen(game.mainMenuScreen);
            System.out.println ("Victory");
        }else {     //No? Dammit...let them eat cake
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
                }
            }

            //enemySpawn
            batch.draw(enemySpawnTexture, map.getSpawnPoint()[0] * tileSize, map.getSpawnPoint()[1] * tileSize + toolbarHieght,30,30);

            //base
            base.collidable.sprite.draw(batch);

            //Enemies
            if ((spawnMonsters) && (currentWaveNumber < waveGrowthNum * waveNum)) {
                //spawn monsters;
                if (System.currentTimeMillis() > mosnsterLastSpawned + spawnRate) {
                    mosnsterLastSpawned = System.currentTimeMillis();
                    double randomNumber = Math.random() * (double)(((double)waveNum/(double)totalEnemyWaves)*(double)(enemyTypes));
                    enemyUnitsOnField.add(new EnemyUnit((int)randomNumber, 20, 20, map.getSpawnPoint()));
                    currentWaveNumber++;
                    System.out.println("Spawn enemy\t" + randomNumber);
                }
            } else {
                if (waveNum >= totalEnemyWaves) {
                    victory = true;
                }
            }

            for (int i = 0; i < explosions.size(); i++){
                if(explosions.get(i).elapsedTime>=explosions.get(i).explosionLifeTime){
                    explosions.remove(i);
                }
            }

            //go through enemies and check if dead (if they are...remove them and also render them
            for (int i = 0; i < enemyUnitsOnField.size(); i++) {
                if (!enemyUnitsOnField.get(i).tick()) {
                    explosions.add(new ExplosionEntity(0.3f,enemyUnitsOnField.get(i).getPos()[0],enemyUnitsOnField.get(i).getPos()[1]));
                    enemyUnitsOnField.remove(i);
                }else {
                    enemyUnitsOnField.get(i).getSprite().draw(batch);
                }
            }



            //if all are dead, give ready button again
            if ((spawnMonsters) && (enemyUnitsOnField.size() == 0) && (System.currentTimeMillis() >= mosnsterLastSpawned + (spawnRate * waveGrowthNum))) {
                enemySpawnTexture = new Texture("enemySpawnClosed.png");
                spawnMonsters = false;
                steamPoints+=(int)(waveNum*waveGrowthNum*0.25);
                currentWaveNumber = 0;
            }

            //Towers
            for (int i = 0; i < towersOnField.size(); i++) {
                TowerObject temp = towersOnField.get(i);

                //temp.gameTick(enemyUnitsOnField);

                temp.getSprite(temp.getxPos() * tileSize, temp.getyPos() * tileSize + toolbarHieght).draw(batch);
                //batch.draw(temp.getSprite(), temp.getxPos(), temp.getyPos()+toolbarHieght,30,30);
                ArrayList<Projectile> tempBullets = towersOnField.get(i).gameTick(enemyUnitsOnField);   //Get all the bullets of the tower and render its bullets
                //update all the projectiles
                for(int j = 0; j < tempBullets.size(); j++){
                    if(!tempBullets.get(j).tick()){         //the projectile tick function not only move the projectile around but also checks if its still alive, if its not...remove from arrayList
                        tempBullets.remove(j);
                    }else{
                        tempBullets.get(j).getSprite().draw(batch);
                    }
                }
            }

            if (leftMouseDown) {
                int x = Gdx.input.getX() - tempTower.getSpriteWidth() / 2;
                int y = Gdx.graphics.getHeight() - Gdx.input.getY() - tempTower.getSpriteWidth() / 2;
                //radius
                towerRangeRaius.setScale(tempTower.getRadius() / 20);
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
            font.draw(batch, "" + steamPoints, 620, 40);
            font.draw(batch, "" + base.getHealth(), 540, 38);
            font.draw(batch, "" + waveNum + "/" + totalEnemyWaves, 615, 335);

            if (!spawnMonsters) {
                readyButton.update((SpriteBatch) batch);
            }

            batch.end();
            //END OF RENDER****************************************************************************************************************************

            // Adding everything to the grid
            grid.clear();
            //enemies
            for (EnemyUnit e : enemyUnitsOnField) {
                grid.add(e.collidable);
            }
            //bullets
            for (TowerObject t : towersOnField) {
                ArrayList<Projectile> temp = t.getProjectiles();
                for (Projectile p : temp) {
                    grid.add(p.collidable);
                }
            }
            //Base
            grid.add(base.collidable);


            // Checking each grid cell for more than one entity
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {

                    ArrayList<Collidable> collidables = grid.get(i, j);

                    if (collidables.size() > 1) {
                        // Loop through the collidables and check them against all others
                        for (int indexOfFirst = 0; indexOfFirst < collidables.size(); indexOfFirst++) {
                            // Start second index at index of first + 1 to avoid double checking
                            for (int indexOfSecond = indexOfFirst + 1; indexOfSecond < collidables.size(); indexOfSecond++) {
                                Collidable first = collidables.get(indexOfFirst);
                                Collidable second = collidables.get(indexOfSecond);

                                if (Intersector.overlapConvexPolygons(first.bounding, second.bounding)) {
                                    // Check bounding polygons first
                                    if (CollisionChecker.collide(first.sprite.getBoundingRectangle(), second.sprite.getBoundingRectangle(), first.mask, second.mask)) {
                                        for (int en = 0; en < enemyUnitsOnField.size(); en++) {
                                            //check if the collision is between base and enemy unit
                                            if (((first == enemyUnitsOnField.get(en).collidable) && (second == base.collidable)) || ((second == enemyUnitsOnField.get(en).collidable) && (first == base.collidable))) {
                                                gameOver = base.doDamage(enemyUnitsOnField.get(en).collidable.damage);
                                                SmallExplosion.play();
                                                first.colliding = true;
                                                second.colliding = true;
                                                //else enemy and bullet
                                            } else if (((first == enemyUnitsOnField.get(en).collidable)&&(second.damage>0))||
                                                    ((second == enemyUnitsOnField.get(en).collidable)&&(first.damage>0))){ //Enemy units do - damage and bullets do + damage. This way one enemy doesnt take another out
                                                enemyUnitsOnField.get(en).collided(second);
                                                if (enemyUnitsOnField.get(en).getHP() < 1) {
                                                    steamPoints += enemyUnitsOnField.get(en).getReward();
                                                }
                                                second.colliding = true;
                                                first.colliding = true;
                                            }
                                            System.out.println (first.fileName + ":" + second.fileName);
                                        }
                                    }
                                } else {
                                    //second.colliding = false;
                                    //first.colliding = false;
                                }
                            }
                        }
                    }
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
                        tollBell.play(0.5f);
                        spawnMonsters = true;
                        enemySpawnTexture = new Texture("enemySpawnOpen.png");
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

            //removeTurret
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                removeTurretToPlayingField();
            }


            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                if (yesNoDialogMessage("End level?", "Are you sure you wish to return to main lobby?\nYou will abandon this brewery to those fiends")) {
                    game.setScreen(game.mainMenuScreen);
                }
            }

            //have the turrets do their things
        }
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
                            if(steamPoints>(int) (towersOnField.get(i).getCost()  * +0.5)) {
                                if (towersOnField.get(i).level < towersOnField.get(i).maxLevel) {
                                    if (yesNoDialogMessage("Upgrade turret?", "Do you wish to upgrade this turret to lvl. " + (towersOnField.get(i).level+1) + "?\nCost " + ((int) (towersOnField.get(i).getCost() + 0.5)) +
                                            "SP\nDamage:\t" + towersOnField.get(i).damage + " -> " + (towersOnField.get(i).damage + (int) (towersOnField.get(i).level * 0.75)) + "\nRadius:\t" + towersOnField.get(i).radius
                                            + " -> " + (towersOnField.get(i).radius + 0.2f) +
                                            "\nRoF:\t" + towersOnField.get(i).rof + " -> " + (towersOnField.get(i).rof - 50))) {
                                        //do upgrade things
                                        towersOnField.get(i).upgrade();
                                        steamPoints -= (int) (towersOnField.get(i).getCost() * +0.5);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(null, "Turret already max level.");
                                }
                            }else{
                                JOptionPane.showMessageDialog(null, "You are not in possession of enough SteamPoints to upgrade this here turret");
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
        if(((dx<mapWidth)&&(dy<mapHeight))&&(dx>-1)&&(dy>-1)){ //inside playing field
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
                            construction.play(0.4f);

                        }catch(Exception e){

                        }
                    }
                }else {
                    //notify player not enough mula
                    JOptionPane.showMessageDialog(null, "You do not have enough Steam Points to construct this towers");
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
        batch.dispose();
    }
}
