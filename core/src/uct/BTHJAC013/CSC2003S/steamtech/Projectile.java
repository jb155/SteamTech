package uct.BTHJAC013.CSC2003S.steamtech;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Jacques on 9/23/2015.
 */
public class Projectile {

    private int[]pos;
    private int damage;
    private float speed;
    private float angle;
    private long spawnTime;
    private int life;
    public Collidable collidable;

    public Projectile(int x, int y, int dmg, float spd, String tex, float agl,int lf){
        pos = new int[] {x,y+50};
        damage = dmg;
        speed = spd;

        collidable = new Collidable(tex);

        collidable.sprite = new Sprite (new Texture(tex));
        collidable.sprite.setSize(10,25);
        collidable.sprite.setOrigin(5,12.5f);
        collidable.sprite.setRotation(agl);

        collidable.damage = damage;

        angle = agl;

        life = lf;
        spawnTime = System.currentTimeMillis();
        tick();
    }

    public boolean tick(){
        pos[1]-= (int) (Math.round(-speed * Math.cos(angle/360 * (2 * Math.PI))));
        pos[0]+= (int) (Math.round(-speed * Math.sin(angle/360 * (2 * Math.PI))));

        collidable.sprite.setPosition(pos[0],pos[1]);
        collidable.bounding.setPosition(pos[0],pos[1]);

        //projectile hit something...get rid of it
        if(collidable.colliding) {
            return false;
        }

        if(System.currentTimeMillis()>spawnTime+life){  //checks if bullet should be dead, if so let turret class know to remove bullets
            return false;
        }else{
            return true;
        }
    }

    public Sprite getSprite(){
        return collidable.sprite;
    }

}
