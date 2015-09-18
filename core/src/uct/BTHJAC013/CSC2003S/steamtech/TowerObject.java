package uct.BTHJAC013.CSC2003S.steamtech;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Jacques on 9/18/2015.
 */
public class TowerObject {
    private float rof = 1;
    private float radius = 3;
    private Texture towerTex;

    private SimpleButton spawnButton;

    public TowerObject(int num){
        try {
            Scanner sc = new Scanner(new FileReader("tower/tower"+num+".txt"));
            //Texture
            String readLine = sc.nextLine();
            towerTex = new Texture(readLine);
            //Radius
            readLine = sc.nextLine();
            radius = Float.parseFloat(readLine);
            //RoF
            readLine = sc.nextLine();
            rof = Float.parseFloat(readLine);

            //Setup spawn Button
            spawnButton = new SimpleButton(towerTex,0,0,50,50);
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public Texture getTexture(){
        return towerTex;
    }

    public void updateButton(Batch batch,float x, float y){
        spawnButton.update((SpriteBatch) batch,x,y);
    }
}
