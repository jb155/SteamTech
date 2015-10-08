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
    private float angle;
    private int damage;
    private int[]pos = new int[2];
    private int reward = 1;
    private Route route;
    private int flagCount = 0;

    public Collidable collidable;

    private long timeLastMoved = 0;

    public EnemyUnit(int num, int width, int height, int[] sp, Route rt){
        try {
            pos[0] = sp[0]*30;
            pos[1] = sp[1]*30+50+5;
            Scanner sc = new Scanner(new FileReader("EnemyUnits/unit"+num+".txt"));
            //Sprite
            String readLine = sc.nextLine();
            collidable = new Collidable(readLine);
            collidable.sprite.setSize(width, height);
            collidable.sprite.setCenter(width / 2, height / 2);
            collidable.sprite.setPosition(pos[0]*30, pos[1]*30+50);

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

            route = rt;

        }catch (IOException e){
            System.out.println(e);
        }
    }

    public void rotate(){
        collidable.rotate(angle);
    }

    public boolean tick() {  //Moves unit into direction it is pointed
        int rx = route.getX(flagCount)*30+5;
        int ry = (route.getY(flagCount)*30+50+5);

        int x = pos[0] - rx;
        int y = pos[1] - ry;

        angle = (float) (Math.atan2(x, y) * 360 / (2 * Math.PI));

        pos[1]-= (int) (Math.round(speed/2 * Math.cos(angle / 360 * (2 * Math.PI))));
        pos[0]+= (int) (Math.round(-speed/2 * Math.sin(angle/360 * (2 * Math.PI))));


        collidable.sprite.setPosition(pos[0], pos[1]);
        collidable.bounding.setPosition(pos[0], pos[1]);

        if (((pos[0]<=rx+1)&&(pos[0]>=rx-1))&&((pos[1]<=ry+1)&&(pos[1]>=ry-1))) {
            flagCount++;
        }

        //System.out.println ("En\tx:" + pos[0] + "\ty:" + pos[1] + "\t angle:" + angle);

        if ((HP <= 0) || (pos[0] > 660) || (collidable.colliding)) {      //dead or outside screen
            return false;
        } else {
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
