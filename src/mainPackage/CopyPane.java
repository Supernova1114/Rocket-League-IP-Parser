package mainPackage;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.OverrunStyle;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class CopyPane extends Hyperlink {

    public CopyPane(String text){
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

                setText("Copied!");

                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5f), new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        setText(text);
                    }

                }));
                timeline.play();
            }
        }); //setOnMouseClicked()

    }

}
