package uct.BTHJAC013.CSC2003S.steamtech;

import com.badlogic.gdx.math.Rectangle;

/**
 * A simple class to check collisions given the bounding boxes and the masks
 * Created by calvin on 2014/09/05. Modified by Jacques Botha 27/09/2015
 */

public class CollisionChecker {

    public static boolean collide(Rectangle r1, Rectangle r2, long[] one, long[] two) {
        // Get shifting amounts
        int upDownShift = (int) (r1.y - r2.y);
        int leftRightShift = (int) (r1.x - r2.x);

        // Loop through making sure to stay within bounds
        for (int i = 0; i < Math.min(r1.height, r2.height - upDownShift); i++) {
            // Check bounds (we need to do this as initial values may be out of bounds, but we want to check later values)
            if (i + upDownShift > 0 && (i + upDownShift) < Math.min( two.length, one.length )) {
                // Bitshift and compare to correct row of other mask
                if (leftRightShift > 0) {
                    if (((one[i] >>> leftRightShift) & two[(i + upDownShift)]) != 0) {
                        System.out.println("Collision");
                        return true;
                    }
                } else {
                    if (((one[i] << Math.abs(leftRightShift)) & two[((i + upDownShift))]) != 0) {
                        System.out.println("Collision");
                        return true;
                    }
                }
            }
        }
        // No collisions found
        return false;
    }
}
