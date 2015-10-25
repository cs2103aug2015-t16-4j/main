package listItUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import listItLogic.CommandParser;

public class InputTextPane extends GridPane implements EventHandler<ActionEvent> {
	
	Text inputLabel;
	TextField inputField;
	
	public InputTextPane() {
		setPadding(new Insets(10, 10, 10 ,10));
		
		inputLabel = new Text("command:  ");
		inputField = new TextField();
		
		inputField.setPrefSize(500, 20);
		inputLabel.setStyle("-fx-fill: linear-gradient(#0033CC 30%, #0029A3 60%, #001A66 90%);");
		
		inputField.setOnAction(this);
		
		setConstraints(inputLabel, 0, 0);
		setConstraints(inputField, 1, 0);
		
		getChildren().addAll(inputLabel, inputField);
	}
	
	public void handle(ActionEvent event) {
		if(event.getSource() == inputField) {
			String command = inputField.getText();
			CommandParser.processCommand(command);
			inputField.setText("");
		}
	}
	
	
}
