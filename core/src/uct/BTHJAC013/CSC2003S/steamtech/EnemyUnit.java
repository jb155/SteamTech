package uct.BTHJAC013.CSC2003S.steamtech;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Jacques on 9/24/2015.
 */
public class EnemyUnit {
    private int HP;
    private int speed;
    private int angle;
    private int damage;
    private Sprite sprite;
    private int[]pos = new int[2];

    public EnemyUnit(int num, int width, int height, int[] sp){
        try {
            pos[0] = sp[0]*30;
            pos[1] = sp[1]*30+50;
            Scanner sc = new Scanner(new FileReader("EnemyUnits/unit"+num+".txt"));
            //Sprite
            String readLine = sc.nextLine();
            sprite = new Sprite(new Texture(readLine));
            sprite.setSize(width, height);
            sprite.setCenter(width/2,height/2);
            sprite.setPosition(pos[0],pos[1]);
            //Set HP
            readLine = sc.nextLine();
            HP = Integer.parseInt(readLine);
            //angle
            angle = 90;
            //damage
            readLine = sc.nextLine();
            damage = Integer.parseInt(readLine);
            //Speed
            readLine = sc.nextLine();
            speed = Integer.parseInt(readLine);

        }catch (IOException e){
            System.out.println(e);
        }
    }

    public boolean tick(){
        pos[0]+= (int) (speed * Math.sin(angle));
        pos[1]+= (int) (speed * Math.cos(angle));
        sprite.setPosition(pos[0],pos[1]);

        //Do collision things here

        if(HP<=0){
            return false;
        }else{
            return true;
        }
    }

    public Sprite getSprite(){
        return sprite;
    }

    public int[] getPos(){
        return pos;
    }

    public int getHP(){
        return HP;
    }
}
