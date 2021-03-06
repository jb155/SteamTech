package uct.BTHJAC013.CSC2003S.steamtech;

import java.util.ArrayList;

/**
 * Created by Jacques on 10/8/2015.
 */
public class Route {
    private ArrayList steps = new ArrayList();

    public Route() {

    }

    public Step getStep(int index) {
        return (Step) steps.get(index);
    }

    public int getX(int index) {
        return getStep(index).x;
    }

    public int getY(int index) {
        return getStep(index).y;
    }


    public void prependStep(int x, int y) {
        steps.add(0, new Step(x, y));
    }

    public boolean contains(int x, int y) {
        return steps.contains(new Step(x,y));
    }


    public class Step {

        private int x;

        private int y;

        public Step(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int hashCode() {
            return x*y;
        }

        public boolean equals(Object other) {
            if (other instanceof Step) {
                Step o = (Step) other;

                return (o.x == x) && (o.y == y);
            }

            return false;
        }
    }
}
