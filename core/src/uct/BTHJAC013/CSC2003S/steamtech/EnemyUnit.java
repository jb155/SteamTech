package uct.BTHJAC013.CSC2003S.steamtech;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Jacques on 9/24/2015.
 */
public class EnemyUnit{
    private float HP;
    private int speed;
    private int angle;
    private int damage;
    private int[]pos = new int[2];
    private int reward = 1;

    public Collidable collidable;

    public EnemyUnit(int num, int width, int height, int[] sp){
        try {
            pos[0] = sp[0]*30;
            pos[1] = sp[1]*30+50;
            Scanner sc = new Scanner(new FileReader("EnemyUnits/unit"+num+".txt"));
            //Sprite
            String readLine = sc.nextLine();
            collidable = new Collidable(readLine);
            collidable.sprite.setSize(width, height);
            collidable.sprite.setCenter(width / 2, height / 2);
            collidable.sprite.setPosition(pos[0], pos[1]);

            //Set HP
            readLine = sc.nextLine();
            HP = Integer.parseInt(readLine);
            //angle
            angle = 90;
            //damage
            readLine = sc.nextLine();
            damage = Integer.parseInt(readLine);
            collidable.damage = -damage;
            //Speed
            readLine = sc.nextLine();
            speed = Integer.parseInt(readLine);
            //Reward
            readLine = sc.nextLine();
            reward = Integer.parseInt(readLine);

        }catch (IOException e){
            System.out.println(e);
        }
    }

    public void rotate(){
        collidable.rotate(angle);
    }

    public boolean tick(){
        pos[0]+= (int) (speed * Math.sin(angle)+0.5);
        pos[1]+= (int) (speed * Math.cos(angle)+0.5);
        collidable.sprite.setPosition(pos[0],pos[1]);
        collidable.bounding.setPosition(pos[0],pos[1]);

        if((HP<=0)||(pos[0]>660)||(collidable.colliding)){      //dead or outside screen
            return false;
        }else{
            return true;
        }
    }

    public void collided(Collidable col){
        //collidable.colliding = true;
        HP -= col.damage;
    }

    public Sprite getSprite(){
        return collidable.sprite;
    }

    public int[] getPos(){
        return pos;
    }

    public float getHP(){
        return HP;
    }

    public int getReward(){
        return reward;
    }
}
