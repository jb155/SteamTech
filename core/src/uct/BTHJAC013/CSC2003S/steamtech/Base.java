package uct.BTHJAC013.CSC2003S.steamtech;

/**
 * Created by Jacques on 9/26/2015.
 */
public class Base {
    private int[] pos;
    private int baseHealth;
    public Collidable collidable;

    public Base(int[] position, int hp, String name){
        pos=position;
        baseHealth = hp;
        collidable=new Collidable(name);
        collidable.sprite.setSize(30, 60);
        collidable.sprite.setCenter(15, 30);
        collidable.sprite.setPosition(pos[0], pos[1]-15);
    }

    public int[] getPos(){
        return pos;
    }

    public int getHealth(){
        return baseHealth;
    }

    public boolean doDamage(float dmg){
        System.out.println ("An allieds' town is under siege");
        baseHealth+=(int)dmg;
        if(baseHealth<1){
            return true;
        }else{
            return false;
        }
    }
}
