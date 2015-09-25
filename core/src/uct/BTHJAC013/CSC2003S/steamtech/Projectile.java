package uct.BTHJAC013.CSC2003S.steamtech;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Jacques on 9/23/2015.
 */
public class Projectile {

    private int[]pos;
    private int damage;
    private int speed;
    private Sprite sprite;
    private float angle;
    private long spawnTime;
    private int life;

    public Projectile(int x, int y, int dmg, int spd, String tex, float agl,int lf){
        pos = new int[] {x,y+50};
        damage = dmg;
        speed = spd;
        sprite = new Sprite (new Texture(tex));
        //sprite.setSize(45,203);
        //sprite.setOrigin(x - sprite.getWidth()/2, y-sprite.getHeight()/2);
        //sprite.setOrigin(50, 125);
        sprite.rotate(angle);
        //sprite.setOrigin(x,y);
        sprite.setPosition(x,y);
        sprite.scale(0.1f);
        sprite.setOrigin(sprite.getWidth() / 2,sprite.getHeight()/2);

        angle = agl;

        life = lf;
        spawnTime = System.currentTimeMillis();
        //System.out.println("New bullet:" + spawnTime);
        //System.out.println ("wheeeee " + angle);

        tick();
    }

    public boolean tick(){
        pos[0]+= (int) (speed * Math.sin(angle));
        pos[1]+= (int) (speed * Math.cos(angle));
        sprite.setPosition(pos[0], pos[1]);


        if(System.currentTimeMillis()>spawnTime+life){  //checks if bullet should be dead, if so let turret class know to remove bullets
            return false;
        }else{
            return true;
        }
    }

    public Sprite getSprite(){
        return sprite;
    }

}
