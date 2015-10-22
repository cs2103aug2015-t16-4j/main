package listItUI;

import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

public class FeedbackPane extends GridPane{
	
	private static TextArea feedbackScreen;
	private Text label;
	
	public FeedbackPane() {
		setPadding(new Insets(10, 10, 10, 10));
		
		label = new Text("Feedback:");
		label.setFont(Font.font("Verdana", FontPosture.ITALIC, 14));
		label.setStyle("-fx-fill: linear-gradient(#0033CC 30%, #0029A3 60%, #001A66 90%);");
		
		feedbackScreen = new TextArea();
		feedbackScreen.setPrefSize(500, 100);
		feedbackScreen.setEditable(false);
		
		setConstraints(label, 0, 0);
		setConstraints(feedbackScreen, 0, 1);
		
		getChildren().add(label);
		getChildren().add(feedbackScreen);
	}

	public static void displayInvalidInput() {
		feedbackScreen.appendText("Invalid input!\n");
	}

	public static void displayInvalidRedo() {
		feedbackScreen.appendText("No action can be redo!\n");
	}

	public static void displayNoTitle() {
		feedbackScreen.appendText("No event title found!\n");
	}

	public static void displayInvalidDate() {
		feedbackScreen.appendText("Invalid date is inputed\n");
	}

	public static void displayInvalidUndo() {
		feedbackScreen.appendText("No action can be undo!\n");
		
	}
}
