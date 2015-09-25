package uct.BTHJAC013.CSC2003S.steamtech;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Jacques on 9/18/2015.
 */
public class SimpleButton {
    private Sprite skin;

    public SimpleButton(Texture texture, float x, float y, float width, float height) {
        skin = new Sprite(texture); // your image
        skin.setPosition(x, y);
        skin.setSize(width, height);
    }

    public void update (SpriteBatch batch, float x, float y) {
        skin.setPosition(x, y);
        skin.draw(batch); // draw the button
    }

    public void update(SpriteBatch batch){
        skin.draw(batch); // draw the button
    }

    public boolean checkIfClicked (float ix, float iy) {
        if (ix > skin.getX() && ix < skin.getX() + skin.getWidth()) {
            if (iy > skin.getY() && iy < skin.getY() + skin.getHeight()) {
                // the button was clicked, perform an action
                return true;
            }
        }
        return false;
    }

    public float spriteSize(){
        return skin.getWidth();
    }
}
