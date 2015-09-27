package uct.BTHJAC013.CSC2003S.steamtech;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Jacques on 9/18/2015.
 */
public class TowerObject implements Cloneable{
    public int number;

    public int rof = 1;
    public float radius = 3;
    private Texture towerTex;
    private Sprite sprite;
    public int level = 1;
    public int maxLevel = 3;

    private int xPos;
    private int yPos;
    private float targetAngle;
    private float angle;
    private int cost = 1;

    public float damage;
    private int bulletLife;
    private float speed;

    private long lastFired;
    private boolean placed = false;

    private SimpleButton spawnButton;
    private ArrayList<Projectile>projectiles;
    private boolean targetLocked = false;
    private EnemyUnit target;

    private String imageName;
    private int spriteWidth;
    private int spriteHeight;

    private String bulletTex;

    private boolean build = false;

    private Sound fireSound;

    public TowerObject(int num, int width, int height){
        setup(num,width,height);
    }

    public TowerObject(int num, int width, int height, int x, int y, boolean place){
        setup(num,width,height);
        xPos = x;
        yPos = y;
        placed = place;
    }

    private void setup(int num, int width, int height){
        number = num;
        try {
            Scanner sc = new Scanner(new FileReader("tower/tower"+num+".txt"));
            //Texture
            String readLine = sc.nextLine();
            imageName=readLine;
            towerTex = new Texture(readLine + level + ".png");
            sprite = new Sprite(towerTex);
            sprite.setSize(width, height);
            sprite.setOrigin(width / 2, height / 2);
            spriteWidth=width;
            spriteHeight=height;
            //Setup spawn Button
            readLine = sc.nextLine();
            spawnButton = new SimpleButton(new Texture(readLine),0,0,50,50);
            //bullet
            readLine = sc.nextLine();
            bulletTex = readLine;
            //Sound
            readLine = sc.nextLine();
            fireSound = Gdx.audio.newSound(Gdx.files.internal(readLine));
            //Radius
            readLine = sc.nextLine();
            radius = Float.parseFloat(readLine);
            //RoF
            readLine = sc.nextLine();
            rof = Integer.parseInt(readLine);
            //Cost
            readLine = sc.nextLine();
            cost = Integer.parseInt(readLine);
            //Damage
            readLine = sc.nextLine();
            damage = Float.parseFloat(readLine);
            //BulletLife
            readLine = sc.nextLine();
            bulletLife = Integer.parseInt(readLine);
            //bullet Speed
            readLine = sc.nextLine();
            speed = Float.parseFloat(readLine);

            angle = 0;

            lastFired = System.currentTimeMillis();
        }catch (IOException e){
            System.out.println(e);
        }
        projectiles = new ArrayList<Projectile>();
    }

    public int getxPos(){
        return xPos;
    }
    public int getyPos(){
        return yPos;
    }

    public void updateButton(Batch batch,float x, float y){
        spawnButton.update((SpriteBatch) batch,x,y);
    }

    public boolean checkIfClicked(float x, float y){
        return spawnButton.checkIfClicked(x, y);
    }


    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getSpriteWidth(){
        return (int) sprite.getWidth();
    }

    public Sprite getSprite(int x, int y){
        sprite.setPosition(x,y);
        return sprite;
    }

    public void setPosition(int x, int y){
        xPos = x;
        yPos = y;
    }

    public int[] getPosition(){
        int[] temp = new int[]{xPos,yPos};
        return temp;
    }
    public float getRadius(){
        return radius;
    }

    public int getCost(){
        return cost;
    }

    public ArrayList<Projectile> gameTick(ArrayList<EnemyUnit>enemies){
        //If a enemy is in range shoot at it (closest)
        if((target==null)||(target.getHP()<1)||(target.collidable.colliding)) {
            target=null;       //this is just if HP = 0, reset target
            targetLocked = false;
            for (int i = 0; i < enemies.size(); i++) {
                EnemyUnit a = enemies.get(i);
                int[] temp = a.getPos();
                int x = (xPos*30) - (temp[0]);
                int y = (temp[1]) - (yPos*30);
                //int x = ((xPos*30-(330))/30) - ((temp[0]-330) / 30);
                //int y = ((temp[1]-330) / 30) - ((yPos*30-(330))/30);
                if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) <= radius*45) {
                    //System.out.println("Enemy in range");
                    targetLocked = true;
                    target = a;
                    i = enemies.size();
                    lastFired = System.currentTimeMillis()-(rof/2);
                }else{
                    target = null;
                    targetLocked = false;
                }
            }
        }else{ //Has target
            int x = (xPos*30) - (target.getPos()[0]);
            int y = (target.getPos()[1]) - (yPos*30)-45;
            if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) <= radius*45) {
                targetAngle = (float) (Math.atan2(x, y) * 360 / (2 * Math.PI));
                //System.out.println("Enemy in range");
                targetLocked = true;
            }else{
                target = null;
                targetLocked = true;
            }
        }

        //System.out.println(targetAngle);

        //else "seek"


        //fire
        if(!targetLocked){
            //targetAngle=0;
        }else{
            if(System.currentTimeMillis()>lastFired+rof){
                projectiles.add(new Projectile(xPos*30,yPos*30, damage, speed, bulletTex ,angle, bulletLife));
                fireSound.play();
                lastFired = System.currentTimeMillis();
            }
        }

        //ensure turret is pointing correct angle
        if(angle!=targetAngle){
            if(angle<targetAngle){
                rotateTower(5);
            }else{
                rotateTower(-5);
            }
        }

        return projectiles;
    }

    public ArrayList<Projectile> getProjectiles(){
        return  projectiles;
    }

    public void placeTower(){
        placed = true;
    }

    public void rotateTower(int a){
        angle += a;
        if(angle > 360){
            angle-=360;
        }
        sprite.setRotation(angle);
    }

    private float getAngle(){
        return angle;
    }

    public int getWidth(){
        return (int)sprite.getWidth();
    }

    public int getHeight(){
        return (int)sprite.getHeight();
    }



    public void upgrade(){
        if(level<maxLevel) {
            rof -= 50;
            radius += 0.2f;
            cost += 1;
            damage += (int)level*0.75;
            level++;

            //new image
            towerTex = new Texture(imageName + level + ".png");
            sprite=null;
            sprite = new Sprite(towerTex);
            sprite.setSize(spriteWidth, spriteHeight);
            sprite.setOrigin(spriteWidth/2, spriteHeight/2);
        }
    }
}
