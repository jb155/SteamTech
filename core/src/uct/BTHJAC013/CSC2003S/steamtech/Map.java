package uct.BTHJAC013.CSC2003S.steamtech;

import java.util.ArrayList;

/**
 * Created by Jacques on 9/14/2015.
 */
public class Map {
    private ArrayList<MapModule> mapModules;
    private int moduleWidth=5;
    private int moduleHeight = 10;

    private int fieldWidth;
    private int fieldHeight;

    private int moduleDatabaseAmount = 6;

    private int mapModuleCount = 4;

    public int[][] playingField;
    public int[] spawnPoint;
    public int[] basePoint;

    private boolean[][] visited = new boolean[fieldWidth][fieldHeight];

    public Map(int width, int height){
        mapModules = new ArrayList<MapModule>();
        GenerateMapParts();

        playingField = new int[width][height];
        fieldWidth = width;
        fieldHeight = height;
        GenerateMap();
        printToScreen();
    }

    public void GenerateMapParts(){
        for (int i = 1; i < moduleDatabaseAmount+1; i++){
            MapModule temp = new MapModule(moduleWidth,moduleHeight,i);
            temp.setup();
            mapModules.add(temp);
        }
    }

    private void addToMap(int startX, MapModule temp){
        startX = startX*moduleWidth+1;          //First and last lines are left blank as the spawn and base needs to be added
        for (int y = 0; y < moduleHeight; y++) {
            int[] line = temp.getRow(y);
            int count = 0;
            for (int x = startX; x < startX + moduleWidth; x++) {
                playingField[x][y]=line[count];
                count++;
            }
        }
    }

    private void printToScreen(){
        for (int i = 0; i < fieldHeight; i++){
            for (int j = 0; j < fieldWidth; j++){
                System.out.print(playingField[j][i]);
            }
            System.out.println();
        }
    }

    public void GenerateMap(){
        MapModule temp;
        int mapUnitCount=0;
        //Select random start
        int randomNumber = (int )(Math.random() * mapModules.size());   //Rndom startpoint
        temp = mapModules.get(randomNumber);

        addToMap(mapUnitCount, temp);
        mapUnitCount++;


        for(int i = 0; i < mapModuleCount-1; i++){
            int[] tempNums = temp.getOut();
            randomNumber = (int )(Math.random() * tempNums.length);
            temp = mapModules.get(tempNums[randomNumber]-1);
            addToMap(mapUnitCount,temp);
            mapUnitCount++;
        }

        //Set spawn
        for(int y = 0; y < moduleHeight; y++){
            if(playingField[1][y]==0){
                playingField[0][y]=-1;
                spawnPoint = new int[]{0,y};
            }else{
                playingField[0][y]=1;
            }
        }


        //Set Base
        for(int y = 0; y < moduleHeight; y++){
            if(playingField[fieldWidth-2][y]==0){
                playingField[fieldWidth-1][y]=-2;
                basePoint = new int[]{fieldWidth-1,y};
            }else{
                playingField[fieldWidth-1][y]=1;
            }
        }
    }
    public int[] getSpawnPoint(){
        return spawnPoint;
    }

    public int[] getBasePoint() {return basePoint;}

    public int getFieldWidth(){
        return fieldWidth;
    }
    public int getFieldHeight(){
        return fieldHeight;
    }

    //*****************************************************************
    //code for AI path finding
    public void clearVisited() {
        for (int x=0;x<getFieldWidth();x++) {
            for (int y=0;y<getFieldHeight();y++) {
                visited[x][y] = false;
            }
        }
    }
    public boolean visited(int x, int y) {
        return visited[x][y];
    }
    public boolean blocked(int x, int y) {
        // if theres a unit at the location, then it's blocked

        if (playingField[x][y] == 1) {
            return true;
        }
        return false;
    }

    public float getCost(int sx, int sy, int tx, int ty) {
        return 1;
    }

}
