package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;



public class Controller {

    String [][] serverArray2D;

    String logFileFolder = "";


    @FXML
    Hyperlink logFolderLink;

    @FXML
    Hyperlink githubLink;

    @FXML
    GridPane gridPane;


    @FXML
    void initialize(){
        logFolderLink.setDisable(true);
        logFolderLink.setText("");
    }

    @FXML
    public void openLogFolder() throws IOException {
        logFolderLink.setVisited(false);

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(new File(logFileFolder));
        }
    }

    @FXML
    public void openGitHubLink() {
        githubLink.setVisited(false);
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(URI.create("https://github.com/Supernova1114/Rocket-League-IP-Parser"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    public void openFile() throws IOException {

        File logFile = null;

        try {
            //Try fileChooser with default log directory
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Log File");

            String userprofile = System.getenv("USERPROFILE");
            fileChooser.setInitialDirectory(new File(userprofile + "\\Documents\\My Games\\Rocket League\\TAGame\\Logs"));

            logFile = fileChooser.showOpenDialog(Main.primaryStage);

        }
        catch (Exception e){
            //Open default fileChooser
            System.out.println("Could Not Find: Default Rocket League Log Directory!; \\Documents\\My Games\\Rocket League\\TAGame\\Logs");

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Log File");

            logFile = fileChooser.showOpenDialog(Main.primaryStage);
        }
        finally {
            if (logFile != null && logFile.getName().contains(".log"))
            beginParse(logFile);
        }
    }


    //Parse log file
    public void beginParse(File file) throws IOException {

        logFolderLink.setDisable(true);

        System.out.println(file.getName());
        //remove children
        gridPane.getChildren().clear();


        try {

            //Read File
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            ArrayList<String> tempList = new ArrayList<>();

            String n;
            while ((n = bufferedReader.readLine()) != null){
                if (n.contains("GameURL")){
                    tempList.add(n);
                }
            }

            bufferedReader.close();



            //Debug
            System.out.println();
            for (String temp: tempList){
                System.out.println(temp);
            }

            serverArray2D = new String[tempList.size()][2];

            //Parse Server IP and Server Name from line
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

            //Debug
            for (int i=0; i<serverArray2D.length; i++){
                System.out.println(serverArray2D[i][0] + " | " + serverArray2D[i][1]);
            }

            //Add to gridPane
            for (int i=0; i<serverArray2D.length; i++){
                gridPane.add(new CustomPane(serverArray2D[i][0]), 0, i);
                gridPane.add(new CustomPane(serverArray2D[i][1]), 1, i);
            }

            logFolderLink.setText(file.getName());
            logFileFolder = file.getParent();

        }catch (Exception e){
            System.out.println("Err: File Read or Parse Failed!");
            e.printStackTrace();
        }

        logFolderLink.setDisable(false);

    }//beginParse()




}
