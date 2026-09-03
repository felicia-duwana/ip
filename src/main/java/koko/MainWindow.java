package koko;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Controls the main window of the Koko user interface.
 */
public class MainWindow {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private Koko koko;

    public void setKoko(Koko koko) {
        this.koko = koko;

        dialogContainer.getChildren().add(
                DialogBox.getKokoDialog(
                        "Hello! I'm Koko. What can I do for you?"
                )
        );

        userInput.requestFocus();
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();

        if (input == null || input.isBlank()) {
            return;
        }

        String response = koko.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input),
                DialogBox.getKokoDialog(response)
        );

        userInput.clear();
        scrollPane.setVvalue(1.0);
        userInput.requestFocus();
    }
}