package koko;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box in the Koko user interface.
 */
public class DialogBox extends HBox {

    private DialogBox(String text, String style) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMaxWidth(350);
        label.setStyle(style);

        this.getChildren().add(label);
        this.setSpacing(4);
    }

    public static DialogBox getUserDialog(String text) {
        DialogBox dialogBox = new DialogBox(
                text,
                "-fx-background-color: #dcf8c6;"
                        + "-fx-padding: 10;"
                        + "-fx-background-radius: 10;");

        dialogBox.setAlignment(Pos.CENTER_RIGHT);
        return dialogBox;
    }

    public static DialogBox getKokoDialog(String text) {
        DialogBox dialogBox = new DialogBox(
                text,
                "-fx-background-color: #eeeeee;"
                        + "-fx-padding: 10;"
                        + "-fx-background-radius: 10;");

        dialogBox.setAlignment(Pos.CENTER_LEFT);
        return dialogBox;
    }
}