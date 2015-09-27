package uct.BTHJAC013.CSC2003S.steamtech;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Jacques on 9/27/2015.
 */
public class ExplosionEntity implements ApplicationListener {
    //Animation
    private int x;
    private int y;
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


    @Override
    public void create() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        //sprite.draw(batch);
        elapsedTime += Gdx.graphics.getDeltaTime();
        batch.draw(animation.getKeyFrame(elapsedTime, true), x, y);
        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
