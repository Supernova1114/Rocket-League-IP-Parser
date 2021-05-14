package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.OverrunStyle;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class CustomPane extends Hyperlink {

    public CustomPane(String text){
        setTextOverrun(OverrunStyle.CLIP);
        setWrapText(true);
        setText(text);
        //setFont(Font.font(16));
        setStyle("-fx-text-fill:black;");

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                StringSelection stringSelection = new StringSelection(text);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);

                SwingWorker worker = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                setText("Copied!");
                            }
                        });

                        Thread.sleep(500);
                        return null;
                    }

                    @Override
                    protected void done() {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                setText(text);
                            }
                        });
                    }
                };
                worker.execute();






            }
        });
    }





}
