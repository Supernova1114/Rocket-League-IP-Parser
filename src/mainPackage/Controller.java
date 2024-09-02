package mainPackage;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;



public class Controller {

    String [][] dataArray2D;

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
    public void closeApp(){
        Main.primaryStage.close();
        System.exit(0);
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
            ArrayList<String> resultList = new ArrayList<>();

            String lineQuery = "GameURL";

            String n;
            while ((n = bufferedReader.readLine()) != null){
                if (n.contains(lineQuery)){
                    resultList.add(n);
                }
            }

            bufferedReader.close();

            //Debug
            System.out.println();
            for (String temp: resultList){
                System.out.println(temp);
            }

            dataArray2D = new String[resultList.size()][4];

            //Parse server name, address, game port, and ping port.
            for (int i=0; i<resultList.size(); i++){

                int serverNameIndex = resultList.get(i).indexOf("ServerName=\"") + 12;
                int serverPingURLIndex = resultList.get(i).indexOf("PingURL=\"") + 9;
                int serverGameURLIndex = resultList.get(i).indexOf("GameURL=\"") + 9;

                String serverNameSub = resultList.get(i).substring(serverNameIndex);
                String serverGameURLSub = resultList.get(i).substring(serverGameURLIndex);
                String serverPingURLSub = resultList.get(i).substring(serverPingURLIndex);
                String serverPingPortSub = "";
                String serverGamePortSub = "";

                serverNameSub = serverNameSub.substring(0, serverNameSub.indexOf("\""));
                serverPingPortSub = serverPingURLSub.substring(serverPingURLSub.indexOf(":")+1, serverPingURLSub.indexOf("\""));
                serverGamePortSub = serverGameURLSub.substring(serverGameURLSub.indexOf(":")+1, serverGameURLSub.indexOf("\""));
                serverGameURLSub = serverGameURLSub.substring(0, serverGameURLSub.indexOf(":"));

                dataArray2D[i][0] = serverNameSub;
                dataArray2D[i][1] = serverGameURLSub;
                dataArray2D[i][2] = serverGamePortSub;
                dataArray2D[i][3] = serverPingPortSub;
            }

            //Debug
            for (int i = 0; i< dataArray2D.length; i++){
                System.out.println(dataArray2D[i][0] + " | " + dataArray2D[i][1] + " | " + dataArray2D[i][2] + " | " + dataArray2D[i][3]);
            }

            String[] columnTitles = new String[]{
                    "Server Name:",
                    "Server Address:",
                    "Game Port:",
                    "Ping Port:"
            };

            int columnCount = columnTitles.length;

            // Add titles to first row.
            for (int i = 0; i < columnCount; i++)
            {
                Label label = new Label(columnTitles[i]);
                label.setUnderline(true);

                gridPane.add(label, i, 0); // Col, Row
            }

            //Add data to gridPane
            for (int i = 0; i < dataArray2D.length; i++) {
                for (int col = 0; col < columnCount; col++) {
                    gridPane.add(new CopyPane(dataArray2D[i][col]), col, i + 1);
                }
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
