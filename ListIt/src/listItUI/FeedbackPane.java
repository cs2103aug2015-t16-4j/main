// @@author Shi Hao A0129916W
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
		
		setupLabel();
		
		setupFeedbackScreen();
		
		setConstraints(label, 0, 0);
		setConstraints(feedbackScreen, 0, 1);
		
		getChildren().add(label);
		getChildren().add(feedbackScreen);
	}

	private void setupFeedbackScreen() {
		feedbackScreen = new TextArea();
		feedbackScreen.setPrefSize(620, 80);
		feedbackScreen.setEditable(false);
	}

	private void setupLabel() {
		label = new Text("Feedback:");
		label.setFont(Font.font("Verdana", FontPosture.ITALIC, 20));
		label.setStyle("-fx-fill: linear-gradient(#0033CC 30%, #0029A3 60%, #001A66 90%);");
	}

	public static void displayInvalidInput() {
		feedbackScreen.appendText("Invalid input!\n");
	}
	
	public static void displayInvalidIndexToDelete() {
		feedbackScreen.appendText("Index is out of bounds!\n");
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
		feedbackScreen.appendText("Undo not available\n");
	}

	public static void displayInvalidTitle() {
		feedbackScreen.appendText("Invalid title\n");
	}

	public static void displayInvalidIndexComplete() {
		feedbackScreen.appendText("Invalid input , index is out of bounds!\n"); 	
	}

	public static void displayInvalidAdd() {
		feedbackScreen.appendText("Cannot add when viewing complete List!\n");
	}

	public static void displayInvalidEdit() {
		feedbackScreen.appendText("Cannot edit when viewing complete List!\n");
	}

	public static void displayInvalidComplete() {
		feedbackScreen.appendText("Cannot complete event when viewing complete List!\n");
	}

	public static void displayValidFileMove() {
		feedbackScreen.appendText("File move complete!\n");
	}

	public static void displayInvalidFileMove() {
		feedbackScreen.appendText("File move failed, please check if datafiles are safe.\n");
	}

	public static void displayInvalidIndexImptLevel() {
		feedbackScreen.appendText("Invalid Importance level,there are only 3 types: 1 , 2 or 3.\n");
	}

	public static void displayInvalidAddBlocked() {
		feedbackScreen.appendText("The current event timeline is blocked, cannot be added\n");
	}

	public static void displayMessage(String message) {
		feedbackScreen.appendText(message + "\n");
		
	}

	public static void displayValidEdit() {
		feedbackScreen.appendText("Sucessful edit!\n");
	}

	public static void displayValidAdd() {
		feedbackScreen.appendText("Sucessful add!\n");
	}
}
