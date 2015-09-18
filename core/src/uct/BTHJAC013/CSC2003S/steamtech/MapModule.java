package uct.BTHJAC013.CSC2003S.steamtech;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Jacques on 9/14/2015.
 */
public class MapModule {
    public int moduleNum;
    private int[] in;
    private int[] out;

    private int moduleWidth;
    private int moduleHeight;

    private int[][] moduleLayout;

    public MapModule(int width, int height, int num){
        moduleLayout=new int[width][height];
        moduleNum=num;

        moduleWidth = width;
        moduleHeight = height;

        moduleNum = num;
    }

    public void setup(){
        try {
            Scanner sc = new Scanner(new FileReader("maps/"+moduleNum+".txt"));
            //Give the module its input module string
            String readLine = sc.nextLine();
            setIn(readLine);
            //Give the module its output module string
            readLine = sc.nextLine();
            setOut(readLine);
            //Give module its guts
            String theRest = "";
            while (sc.hasNext()){
                readLine=sc.nextLine();
                theRest+=readLine + "-";
            }
            setLayout(theRest);
        }catch (IOException e){
            System.out.println(e);
        }
    }

    //This will only be used for constructing the modules
    public void setIn(String a){
        String[] temp = a.split(",");
        in = new int[temp.length];
        for(int i = 0; i < temp.length; i++){
            in[i] = Integer.parseInt(temp[i]);
        }
    }

    public void setOut(String a){
        String[] temp = a.split(",");
        out = new int[temp.length];
        for(int i = 0; i < temp.length; i++){
            out[i] = Integer.parseInt(temp[i]);
        }
    }

    public int[] getIn(){
        return in;
    }
    public int[] getOut(){
        return out;
    }
    public int[] getRow(int a){
        int[] temp = new int[moduleWidth];
        for (int i = 0; i < moduleWidth; i++){
            temp[i] = moduleLayout[i][a];
        }
        return temp;
    }

    public void setLayout(String a){
        String[] temp = a.split("-");

        for (int y = 0; y < temp.length; y++) {
            if(temp[y].length()>0) { //Double checks its not the last bit (it's is null)
                String[] widthSplit = temp[y].split(",");
                for (int x = 0; x < widthSplit.length; x++) {
                    moduleLayout[x][y] = Integer.parseInt(widthSplit[x]);
                }
            }
        }
    }
}

