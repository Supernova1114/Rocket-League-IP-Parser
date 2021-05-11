package sample;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.io.*;
import java.util.ArrayList;



public class Controller {

    String [][] serverArray2D;

    //String parsedString = "";
    ArrayList<String> gameURLList = new ArrayList<>();

    @FXML
    GridPane gridPane;

    @FXML
    void initialize(){

    }

    @FXML
    public void beginParse() throws IOException {

        //parsedString = "";


        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("C:\\Users\\Fart\\Documents\\My Games\\Rocket League\\TAGame\\Logs\\Launch.log")));

        ArrayList<String> tempList = new ArrayList<>();
        /*int n;
        while ( (n = bufferedReader.read()) != -1){
            parsedString += (char)n;
            //System.out.print((char)n);
        }*/

        String n;
        while ((n = bufferedReader.readLine()) != null){
            if (n.contains("GameURL")){
                tempList.add(n);
                //System.out.println(n);
            }
        }

        bufferedReader.close();

        System.out.println();

        for (String temp: tempList){
            System.out.println(temp);
        }

        serverArray2D = new String[tempList.size()][2];

        for (int i=0; i<tempList.size(); i++){

            int serverNameIndex = tempList.get(i).indexOf("ServerName=\"") + 12;
            int serverGameURLIndex = tempList.get(i).indexOf("GameURL=\"") + 9;

            String serverNameSub = tempList.get(i).substring(serverNameIndex);
            String serverGameURLSub = tempList.get(i).substring(serverGameURLIndex);

            serverNameSub = serverNameSub.substring(0, serverNameSub.indexOf("\""));
            serverGameURLSub = serverGameURLSub.substring(0, serverGameURLSub.indexOf(":"));

            serverArray2D[i][0] = serverNameSub;
            serverArray2D[i][1] = serverGameURLSub;

        }

        for (int i=0; i<serverArray2D.length; i++){
            System.out.println(serverArray2D[i][0] + " | " + serverArray2D[i][1]);
        }

        for (int i=0; i<serverArray2D.length; i++){
            gridPane.add(new CustomPane(serverArray2D[i][0]), 0, i);
            gridPane.add(new CustomPane(serverArray2D[i][1]), 1, i);
        }




    }




}
