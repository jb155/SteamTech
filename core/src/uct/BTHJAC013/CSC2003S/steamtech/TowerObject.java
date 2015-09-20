package uct.BTHJAC013.CSC2003S.steamtech;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Jacques on 9/18/2015.
 */
public class TowerObject implements Cloneable{
    private float rof = 1;
    private float radius = 3;
    private Texture towerTex;
    private Sprite sprite;
    private int level = 1;
    private int maxLevel = 3;

    private int xPos;
    private int yPos;
    private int angle = 0;
    private int cost = 1;

    private SimpleButton spawnButton;

    private boolean build = false;

    public TowerObject(int num, int width, int height){
        try {
            Scanner sc = new Scanner(new FileReader("tower/tower"+num+".txt"));
            //Texture
            String readLine = sc.nextLine();
            towerTex = new Texture(readLine);
            sprite = new Sprite(towerTex);
            sprite.setSize(width, height);
            //Setup spawn Button
            readLine = sc.nextLine();
            spawnButton = new SimpleButton(new Texture(readLine),0,0,50,50);
            //Radius
            readLine = sc.nextLine();
            radius = Integer.parseInt(readLine);
            //RoF
            readLine = sc.nextLine();
            rof = Float.parseFloat(readLine);
            readLine = sc.nextLine();
            cost = Integer.parseInt(readLine);

        }catch (IOException e){
            System.out.println(e);
        }
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
        return spawnButton.checkIfClicked(x,y);
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

    public void toggleColour(){
        build = !build;
        if(build){

        }else{

        }
    }

    public float getRadius(){
        return radius;
    }

    public int getCost(){
        return cost;
    }

    public void gameTick(){
        //check 
    }
}
