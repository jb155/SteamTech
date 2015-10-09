package uct.BTHJAC013.CSC2003S.steamtech;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Jacques on 10/8/2015.
 */

public class PathFinder {
    private ArrayList closed = new ArrayList();
    private SortedList open = new SortedList();

    private Map map;
    private Node[][] nodes;

    public PathFinder(Map map) {
        this.map = map;

        nodes = new Node[map.getFieldWidth()][map.getFieldHeight()];
        for (int x=0;x<map.getFieldWidth();x++) {
            for (int y=0;y<map.getFieldHeight();y++) {
                nodes[x][y] = new Node(x,y);
            }
        }
    }

    public Route findPath(int sx, int sy, int tx, int ty) {
        // initial state for A*. The closed group is empty. Only the starting
        nodes[sx][sy].cost = 0;
        nodes[sx][sy].depth = 0;
        closed.clear();
        open.clear();
        open.add(nodes[sx][sy]);

        nodes[tx][ty].parent = null;

        int maxDepth = 0;
        while ((open.size() != 0)) {
            //Pull first from open (probably the next thanks to heuristic)
            Node current = getFirstInOpen();
            if (current == nodes[tx][ty]) {
                break;
            }

            removeFromOpen(current);
            addToClosed(current);

            //Goes through all the neighbours of the tile and selects best cost one
            for (int x=-1;x<2;x++) {
                for (int y=-1;y<2;y++) {
                    //Checks if not current
                    if ((x == 0) && (y == 0)) {
                        continue;
                    }

                    //Not allowing for diag movement
                    if ((x != 0) && (y != 0)) {
                        continue;
                    }

                    //Determine the neighbour
                    int xp = x + current.x;
                    int yp = y + current.y;

                    if (isValidLocation(sx,sy,xp,yp)) {
                       float nextStepCost = current.cost + getMovementCost(current.x, current.y, xp, yp);
                        Node neighbour = nodes[xp][yp];

                        //If this node is cheaper than a prev, then un check the prev, sothat it can be re eval later if neded

                        if (nextStepCost < neighbour.cost) {
                            if (inOpenList(neighbour)) {
                                removeFromOpen(neighbour);
                            }
                            if (inClosedList(neighbour)) {
                                removeFromClosed(neighbour);
                            }
                        }

                        //Step to the open list
                        if (!inOpenList(neighbour) && !(inClosedList(neighbour))) {
                            neighbour.cost = nextStepCost;
                            neighbour.heuristic = getHeuristicCost(xp, yp, tx, ty);
                            maxDepth = Math.max(maxDepth, neighbour.setParent(current));
                            addToOpen(neighbour);
                        }
                    }
                }
            }
        }

        //If we have run throgh aall of that and still havent got a parent...then there is no path...bail
        if (nodes[tx][ty].parent == null) {
            return null;
        }


        //If we hit this point we have found a route...so make one then
        Route route = new Route();
        Node target = nodes[tx][ty];
        while (target != nodes[sx][sy]) {
            route.prependStep(target.x, target.y);
            target = target.parent;
        }
        route.prependStep(sx,sy);

        // thats it, we have our path

        return route;
    }

    protected Node getFirstInOpen() {
        return (Node) open.first();
    }

    protected void addToOpen(Node node) {
        open.add(node);
    }

    protected boolean inOpenList(Node node) {
        return open.contains(node);
    }

    protected void removeFromOpen(Node node) {
        open.remove(node);
    }

    protected void addToClosed(Node node) {
        closed.add(node);
    }

    protected boolean inClosedList(Node node) {
        return closed.contains(node);
    }

    protected void removeFromClosed(Node node) {
        closed.remove(node);
    }

    protected boolean isValidLocation(int sx, int sy, int x, int y) {
        boolean invalid = (x < 0) || (y < 0) || (x >= map.getFieldWidth()) || (y >= map.getFieldHeight());

        if ((!invalid) && ((sx != x) || (sy != y))) {
            invalid = map.blocked(x, y);
        }

        return !invalid;
    }

    public float getMovementCost(int sx, int sy, int tx, int ty) {
        return map.getCost(sx, sy, tx, ty);
    }

    public float getHeuristicCost(int x, int y, int tx, int ty) {
        float dx = tx - x;
        float dy = ty - y;

        float result = (float) (Math.sqrt((dx*dx)+(dy*dy)));
        return result;
    }

    private class SortedList {
        private ArrayList list = new ArrayList();

        public Object first() {
            return list.get(0);
        }

        public void clear() {
            list.clear();
        }

        public void add(Object o) {
            list.add(o);
            Collections.sort(list);
        }

        public void remove(Object o) {
            list.remove(o);
        }

        public int size() {
            return list.size();
        }

        public boolean contains(Object o) {
            return list.contains(o);
        }
    }

    private class Node implements Comparable {
        private int x;
        private int y;
        private float cost;
        private Node parent;
        private float heuristic;
        private int depth;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int setParent(Node parent) {
            depth = parent.depth + 1;
            this.parent = parent;

            return depth;
        }


        public int compareTo(Object other) {
            Node o = (Node) other;

            float f = heuristic + cost;
            float of = o.heuristic + o.cost;

            if (f < of) {
                return -1;
            } else if (f > of) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}