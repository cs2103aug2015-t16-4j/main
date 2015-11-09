package listItUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import listItLogic.CommandParser;

/**
 * This class helps constructs the input text field of the program. 
 * It creates the field to receive your command, and 
 * passes the command to the command parser in the Logic component.
 * @version 0.5
 */

public class InputTextPane extends GridPane implements EventHandler<ActionEvent> {

	Text inputLabel;
	TextField inputField;
	Clipboard clipboard = Clipboard.getSystemClipboard();

	public InputTextPane() {
		setPadding(new Insets(10, 10, 10, 10));

		setupLabel();

		setupTextField();
		
		setupInputAction();

		setConstraints(inputLabel, 0, 0);
		setConstraints(inputField, 1, 0);

		getChildren().addAll(inputLabel, inputField);
	}

	private void setupTextField() {
		inputField = new TextField();
		inputField.setOnAction(this);
		inputField.setPrefSize(540, 20);
	}

	private void setupInputAction() {
		inputField.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.COPY) {
					inputField.setText(clipboard.getString());
				}
			}

		});
	}

	private void setupLabel() {
		inputLabel = new Text("command:  ");
		inputLabel.setStyle("-fx-fill: linear-gradient(#0033CC 30%, #0029A3 60%, #001A66 90%);");
	}

	public void handle(ActionEvent event) {
		if (event.getSource() == inputField) {
			String command = inputField.getText();
			CommandParser.processCommand(command);
			inputField.setText("");
		}
	}

	public TextField getTextField() {
		return inputField;
	}

}
