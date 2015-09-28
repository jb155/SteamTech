package uct.BTHJAC013.CSC2003S.steamtech;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Jacques on 9/27/2015.
 */
public class ExplosionEntity{
    //Animation
    public int x;
    public int y;
    private TextureAtlas textureAtlas;
    private Animation animation;
    public float elapsedTime = 0;
    public float explosionLifeTime = 0;
    private SpriteBatch batch;

    public ExplosionEntity(float life, int xPos, int yPos){
        textureAtlas = new TextureAtlas(Gdx.files.internal("explosion/explosion.atlas"));
        animation = new Animation(1/15f, textureAtlas.getRegions());
        animation.setPlayMode(Animation.PlayMode.NORMAL);
        explosionLifeTime = life;
        x = xPos;
        y = yPos;
    }

    public TextureRegion update() {
        //sprite.draw(batch);
        elapsedTime += Gdx.graphics.getDeltaTime();
        //batch.draw(animation.getKeyFrame(elapsedTime, true), x, y);
        return animation.getKeyFrame(elapsedTime, true);
    }
}
