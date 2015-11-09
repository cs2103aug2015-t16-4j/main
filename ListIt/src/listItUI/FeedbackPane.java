// @@author Shi Hao A0129916W
package listItUI;

import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

/**
 * This class constructs the feedback text area of the program 
 * which is used to present the feedback from our program. 
 * it displays the different messages required when a user
 * has inputed either an valid or an invalid command. 
 * 
 * @version 0.5
 */

public class FeedbackPane extends GridPane{
	
	private static final String MESSAGE_EDIT_TIMELINE_BLOCKED = "Task timeline cannot be edited as specific timeline is blocked\n";
	private static final String MESSAGE_SUCCESSFUL_ADD = "Sucessful add!\n";
	private static final String MESSAGE_SUCESSFUL_EDIT = "Sucessful edit!\n";
	private static final String MESSAGE_TIMELINE_BLOCKED = "The current event timeline is blocked, cannot be added\n";
	private static final String MESSAGE_INVALID_RANK_LEVEL = "Invalid Importance level,there are only 3 types: 1 , 2 or 3.\n";
	private static final String MESSAGE_FILE_MOVE_FAIL = "File move failed, please check if datafiles are safe.\n";
	private static final String MESSAGE_FILE_MOVE_SUCCESS = "File move complete!\n";
	private static final String MESSAGE_COMPLETE_NOT_ALLOW = "Cannot complete event when viewing complete List!\n";
	private static final String MESSAGE_EDIT_NOT_ALLOW = "Cannot edit when viewing complete List!\n";
	private static final String MESSAGE_ADD_NOT_ALLOW = "Cannot add when viewing complete List!\n";
	private static final String MESSAGE_INVALID_INDEX = "Invalid input , index is out of bounds!\n";
	private static final String MESSAGE_INVALID_TITLE = "Invalid title\n";
	private static final String MESSAGE_NO_UNDO = "Undo not available\n";
	private static final String MESSAGE_INVALID_DATE = "Invalid date is inputed\n";
	private static final String MESSAGE_NO_REDO = "No action can be redo!\n";
	private static final String MESSAGE_INDEX_OUT_OF_BOUNDS = "Index is out of bounds!\n";
	private static final String MESSAGE_INVALID_INPUT = "Invalid input!\n";
	
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
		feedbackScreen.appendText(MESSAGE_INVALID_INPUT);
	}
	
	public static void displayInvalidIndexToDelete() {
		feedbackScreen.appendText(MESSAGE_INDEX_OUT_OF_BOUNDS);
	}

	public static void displayInvalidRedo() {
		feedbackScreen.appendText(MESSAGE_NO_REDO);
	}

	public static void displayInvalidDate() {
		feedbackScreen.appendText(MESSAGE_INVALID_DATE);
	}

	public static void displayInvalidUndo() {
		feedbackScreen.appendText(MESSAGE_NO_UNDO);
	}

	public static void displayInvalidTitle() {
		feedbackScreen.appendText(MESSAGE_INVALID_TITLE);
	}

	public static void displayInvalidIndexComplete() {
		feedbackScreen.appendText(MESSAGE_INVALID_INDEX); 	
	}

	public static void displayInvalidAdd() {
		feedbackScreen.appendText(MESSAGE_ADD_NOT_ALLOW);
	}

	public static void displayInvalidEdit() {
		feedbackScreen.appendText(MESSAGE_EDIT_NOT_ALLOW);
	}

	public static void displayInvalidComplete() {
		feedbackScreen.appendText(MESSAGE_COMPLETE_NOT_ALLOW);
	}

	public static void displayValidFileMove() {
		feedbackScreen.appendText(MESSAGE_FILE_MOVE_SUCCESS);
	}

	public static void displayInvalidFileMove() {
		feedbackScreen.appendText(MESSAGE_FILE_MOVE_FAIL);
	}

	public static void displayInvalidIndexImptLevel() {
		feedbackScreen.appendText(MESSAGE_INVALID_RANK_LEVEL);
	}

	public static void displayInvalidAddBlocked() {
		feedbackScreen.appendText(MESSAGE_TIMELINE_BLOCKED);
	}

	public static void displayMessage(String message) {
		feedbackScreen.appendText(message + "\n");
		
	}

	public static void displayValidEdit() {
		feedbackScreen.appendText(MESSAGE_SUCESSFUL_EDIT);
	}

	public static void displayValidAdd() {
		feedbackScreen.appendText(MESSAGE_SUCCESSFUL_ADD);
	}

	public static void displayInvalidEditBlocked() {
		feedbackScreen.appendText(MESSAGE_EDIT_TIMELINE_BLOCKED);
	}
}
