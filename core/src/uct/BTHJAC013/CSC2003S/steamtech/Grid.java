package uct.BTHJAC013.CSC2003S.steamtech;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * A grid for more efficient collision detection
 * Created by calvin on 2014/09/07. Modified by Jacques Botha 27/09/2015
 */
public class Grid {
    private ArrayList<Collidable>[][] grid = new ArrayList[3][3];
    private int height;
    private int width;

    public Grid(int height, int width) {
        // A simple constructor
        this.height = height;
        this.width = width;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[i][j]= new ArrayList<Collidable>();
            }
        }
    }

    public void clear() {
        // Clear all objects
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[i][j].clear();
            }
        }
    }

    public void add(Collidable entity) {
        // Need to find all grid positions it touches
        // Because our sprites < size of a grid cell, we can just check positions of vertices
        Rectangle bounds = entity.sprite.getBoundingRectangle();

        int[] cells = cell(bounds.x, bounds.y);
        safeAdd(cells, entity);
        cells = cell(bounds.x + bounds.width, bounds.y);
        safeAdd(cells, entity);
        cells = cell(bounds.x + bounds.width, bounds.y + bounds.height);
        safeAdd(cells,entity);
        cells = cell(bounds.x, bounds.y + bounds.height);
        safeAdd(cells,entity);
    }

    private void safeAdd(int[] cells, Collidable entity) {
        // Add only if not present
        if (!grid[cells[0]][cells[1]].contains(entity)) {
            grid[cells[0]][cells[1]].add(entity);
        }
    }

    public int[] cell(float x, float y) {
        // Checks the correct location in most naive way ever
        int[] cells = new int[2];
        if (x < height / 3) {
            cells[0] = 0;
        } else if (x < height * 2 / 3) {
            cells[0] = 1;
        } else {
            cells[0] = 2;
        }

        if (y < width / 3) {
            cells[1] = 0;
        } else if (x < width * 2 / 3) {
            cells[1] = 1;
        } else {
            cells[1] = 2;
        }

        return cells;
    }

    public ArrayList<Collidable> get(int x, int y){
        // Returns all elements within a given grid
        return grid[x][y];
    }

}
