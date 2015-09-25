package uct.BTHJAC013.CSC2003S.steamtech;

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

    private int rof = 1;
    private float radius = 3;
    private Texture towerTex;
    private Sprite sprite;
    private int level = 1;
    private int maxLevel = 3;

    private int xPos;
    private int yPos;
    private float targetAngle;
    private float angle;
    private int cost = 1;

    private long lastFired;
    private boolean placed = false;

    private SimpleButton spawnButton;
    private ArrayList<Projectile>projectiles;
    private boolean targetLocked = false;
    private EnemyUnit target;

    private boolean build = false;

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
            towerTex = new Texture(readLine);
            sprite = new Sprite(towerTex);
            sprite.setSize(width, height);
            sprite.setOrigin(width/2, height/2);
            //Setup spawn Button
            readLine = sc.nextLine();
            spawnButton = new SimpleButton(new Texture(readLine),0,0,50,50);
            //Radius
            readLine = sc.nextLine();
            radius = Float.parseFloat(readLine);
            //RoF
            readLine = sc.nextLine();
            rof = Integer.parseInt(readLine);
            //Cost
            readLine = sc.nextLine();
            cost = Integer.parseInt(readLine);

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

    public float getButtonSize(){
        return (int) spawnButton.spriteSize();
    }


    public float getRadius(){
        return radius;
    }

    public int getCost(){
        return cost;
    }

    public ArrayList<Projectile> gameTick(ArrayList<EnemyUnit>enemies){
        //If a enemy is in range shoot at it (closest)
        if((target==null)||(target.getHP()<=0)) {
            for (int i = 0; i < enemies.size(); i++) {
                EnemyUnit a = enemies.get(i);
                int[] temp = a.getPos();
                int x = (xPos) - (temp[0] / 30);
                int y = (temp[1] / 30) - (yPos);
                if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) <= radius * 2) {
                    targetAngle = (int) (Math.atan2(x * 30, y * 30) * 360 / (2 * Math.PI));
                    //System.out.println("Enemy in range");
                    targetLocked = true;
                    target = a;
                    i = enemies.size();
                }else{
                    target = null;
                    targetLocked = false;
                }
            }
        }else{ //Has target
            int x = (xPos) - (target.getPos()[0] / 30);
            int y = (target.getPos()[1] / 30) - (yPos);
            if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) <= radius * 2) {
                targetAngle = (float) (Math.atan2(x * 30, y * 30) * 360 / (2 * Math.PI));
                //System.out.println("Enemy in range");
                targetLocked = true;
            }else{
                target = null;
                targetLocked = true;
            }
        }


        //else "seek"
        if(!targetLocked){
            targetAngle=0;
        }else{
            if(System.currentTimeMillis()>lastFired+rof){
                projectiles.add(new Projectile(xPos*30,yPos*30, 1, 1, "bullet.png",angle, 500));
                lastFired = System.currentTimeMillis();
            }
        }

        //update all the projectiles
        for(int i = 0; i < projectiles.size(); i++){
            if(!projectiles.get(i).tick()){         //the projectile tick function not only move the projectile around but also checks if its still alive, if its not...remove from arrayList
                projectiles.remove(i);
            }
        }

        //ensure turret is pointing correct angle
        if(angle!=targetAngle){
            if(angle<targetAngle){
                rotateTower(10);
            }else{
                rotateTower(-10);
            }
        }

        return projectiles;
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
}
