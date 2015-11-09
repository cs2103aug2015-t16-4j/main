# Shi Hao A0129916W
###### src\listItLogic\CompleteLogic.java
``` java
package listItLogic;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;

/**
 * This class contains methods to move tasks from the usual data file, 
 * into the completed data file, when the user enters "complete". 
 *
 */
public class CompleteLogic {
	
	private static FileModifier modifier = FileModifier.getInstance();
	private static String message = null;
	private static final String MESSAGE_OUT_OF_BOUNDS = "Index is out of bounds";
	private static final String COMPLETE_SUCCESSFUL = "Task is completed";

	/**
	 * This method gets the task by using the line index, and then moves the task
	 * over to the completed task list. 
	 * @param command string command input by the user with a "complete" keyword.
	 */
	public static void completeEvent(String command) {
		int taskIndexComplete = getCompleteIndex(command);
		int sizeOfFile = getSizeOfFile();
		if(isValidIndex(taskIndexComplete, sizeOfFile)) {
			modifier.completeTask(taskIndexComplete - 1);
			message = COMPLETE_SUCCESSFUL;
			LoggingLogic.logging(message);
			FeedbackPane.displayMessage(message);
			
		} else {
			FeedbackPane.displayInvalidIndexComplete();
			message = MESSAGE_OUT_OF_BOUNDS;
			LoggingLogic.logging(message);
		}
	}

	/**
	 * Checks if the index entered is valid (in the range of the task list)
	 * @param taskIndexComplete completed task list index
	 * @param sizeOfFile size of the task list to get the index range
	 * @return true if index is valid, else returns false
	 */
	private static boolean isValidIndex(int taskIndexComplete, int sizeOfFile) {
		return (taskIndexComplete-1) < sizeOfFile && taskIndexComplete-1 >= 0;
	}

	private static int getSizeOfFile() {
		return modifier.getContentList().size();
	}

	private static int getCompleteIndex(String command) {
		return Integer.parseInt(command.substring(9));
	}
	
	public static String getMessage() {
		return message;
	}
}
```
###### src\listItLogic\TaskCheckLogic.java
``` java
package listItLogic;

import java.util.ArrayList;
import java.util.Date;

import fileModifier.FileModifier;
import taskGenerator.Task;

/**
 * This class contains all the methods that check if the command date input
 * entered by the user is of a valid date input, and also compares the task date
 * to the actual calendar date.
 * @version 0.5
 */
public class TaskCheckLogic {
	static FileModifier modifier = FileModifier.getInstance();

	public TaskCheckLogic() {

	}

	/**
	 * Checks if the date variable of the task is over the actual calendar date.
	 * @param taskList selected task list
	 */
	public static void overDateCheck(ArrayList<Task> taskList) {
		Task tempTask = new Task();
		Date systemTime = new Date();
		for (int i = 0; i < taskList.size(); i++) {
			tempTask = taskList.get(i);
			if (!isEndDateNull(tempTask)) {
				if (isOverDate(tempTask, systemTime)) {
					tempTask.setOverDate();
					taskList.set(i, tempTask);
				} else {
					tempTask.setNotOverDate();
					taskList.set(i, tempTask);
				}
			} else {
				break;
			}
		}

		modifier.saveFile(taskList);
	}

	private static boolean isOverDate(Task tempTask, Date systemTime) {
		return systemTime.compareTo(tempTask.getEndDateInDateType()) > 0;
	}

	private static boolean isEndDateNull(Task tempTask) {
		return tempTask.getEndDate() == null;
	}

	/**
	 * Checks the block tasks in the list, to see if there is a time line overlap
	 * between the newTask and blockingTask
	 * 
	 * @param taskForCheck
	 *            task in a block input
	 * @return true if above holds, else returns false.
	 */
	public static boolean blockedDateCheck(Task taskForCheck) {
		boolean result = true;
		if (!isEndDateNull(taskForCheck) && !isStartDateNull(taskForCheck)) {
			ArrayList<Task> taskList = modifier.getContentList();
			Task tempTask = new Task();
			for (int i = 0; i < taskList.size(); i++) {
				tempTask = taskList.get(i);
				if (tempTask.isBlocking()) {
					if (isBlockDatesValid(taskForCheck, tempTask)) {
						result = false;
						break;
					}
				}
			}
		}
		return result;
	}

	private static boolean isBlockDatesValid(Task taskForCheck, Task tempTask) {
		if (taskForCheck.getStartDateInDateType().compareTo(tempTask.getStartDateInDateType()) == -1
				&& taskForCheck.getEndDateInDateType().compareTo(tempTask.getEndDateInDateType()) == 1) {
			return true;
		} else if (taskForCheck.getEndDateInDateType().compareTo(tempTask.getStartDateInDateType()) == 1
				&& taskForCheck.getEndDateInDateType().compareTo(tempTask.getEndDateInDateType()) == -1) {
			return true;
		} else if (taskForCheck.getStartDateInDateType().compareTo(tempTask.getStartDateInDateType()) == 1 
				&& taskForCheck.getStartDateInDateType().compareTo(tempTask.getEndDateInDateType()) == -1) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isStartDateNull(Task taskForCheck) {
		return taskForCheck.getStartDate() == null;
	}
}
```
###### src\listItLogic\UndoAndRedoLogic.java
``` java
package listItLogic;

import java.util.ArrayList;
import java.util.Stack;

import taskGenerator.Task;

/**
 * This class contains methods for our redo and undo logic; 2 temporary stacks
 * are created and the task lists are stored into these stacks. Therefore, when 
 * the appropriate command is called, the previous or next list can be recalled. 
 * The 2 stacks created are temporary as when ListIt is closed, all data lists 
 * in the stacks are deleted. Hence, the undo and redo function works only up to 
 * the current status of when ListIt was last open.
 * @version 0.5
 */
public class UndoAndRedoLogic {

	private static UndoAndRedoLogic storage;
	private static Stack<ArrayList<Task>> undo;
	private static Stack<ArrayList<Task>> redo;
	private static Stack<ArrayList<Task>> undoComplete;
	private static Stack<ArrayList<Task>> redoComplete;

	private UndoAndRedoLogic() {
		undo = new Stack<ArrayList<Task>>();
		redo = new Stack<ArrayList<Task>>();
		undoComplete = new Stack<ArrayList<Task>>();
		redoComplete = new Stack<ArrayList<Task>>();
	}

	public static UndoAndRedoLogic getInstance() {
		if (isStorageNull()) {
			storage = new UndoAndRedoLogic();
		}
		return storage;
	}

	private static boolean isStorageNull() {
		return storage == null;
	}

	public void storeListToUndo(ArrayList<Task> list) {
		undo.push(list);
	}

	public ArrayList<Task> getListFromUndo() {
		return undo.pop();
	}

	public void storeListToRedo(ArrayList<Task> list) {
		redo.push(list);
	}

	public ArrayList<Task> getListFromRedo() {
		return redo.pop();
	}

	public boolean isUndoEmpty() {
		return undo.isEmpty();
	}

	public boolean isRedoEmpty() {
		return redo.isEmpty();
	}

	public void clearRedo() {
		redo.clear();
	}
	
	public void clearUndo() {
		undo.clear();
	}
	
	public void storeListToUndoComplete(ArrayList<Task> completeList) {
		undoComplete.push(completeList);
	}
	
	public ArrayList<Task> getListFromUndoComplete() {
		return undoComplete.pop();
	}
	
	public void storeListToRedoComplete(ArrayList<Task> completeList) {
		redoComplete.push(completeList);
	}
	
	public ArrayList<Task> getListFromRedoComplete() {
		return redoComplete.pop();
	}
	
	public void clearRedoComplete() {
		redoComplete.clear();
	}
}
```
###### src\listItUI\FeedbackPane.java
``` java
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
```
###### src\listItUI\InputTextPane.java
``` java
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
```
###### src\listItUI\OutputScreenPane.java
``` java
package listItUI;

import java.util.ArrayList;

/**
 * This class helps constructs the output text field of the program.
 * 
 * @version 0.5
 */

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import taskGenerator.Task;

public class OutputScreenPane extends GridPane {

	private Text displayLabel;
	private static VBox taskList;
	private static ScrollPane Screen;

	public OutputScreenPane() {
		setPadding(new Insets(10, 10, 10, 10));

		setupDisplayLabel();

		setupListPane();

		setupScreen();

		setConstraints(displayLabel, 0, 0);
		setConstraints(Screen, 0, 1);

		getChildren().addAll(displayLabel, Screen);
	}
	
	private void setupScreen() {
		Screen = new ScrollPane();
		Screen.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
		Screen.setContent(taskList);
	}

	private void setupListPane() {
		taskList = new VBox();
		taskList.setStyle("-fx-background-color: #FFFFFF;");
		taskList.setPrefSize(600, 370);
	}

	private void setupDisplayLabel() {
		displayLabel = new Text("Display:");
		displayLabel.setFont(Font.font("Verdana", FontPosture.ITALIC, 20));
		displayLabel.setStyle("-fx-fill: linear-gradient(#0033CC 30%, #0029A3 60%, #001A66 90%);");
	}

	public static void displayList(ArrayList<Task> list) {
		Task tempTask = new Task();
		boolean isFloatingState = false;
		String currentHeader = null;
		
		assert list != null;

		taskList.getChildren().clear();

		if (list.isEmpty()) {
			displayEmpty();
			return;
		}

		if (list.get(0).getEndDate() != null) {
			currentHeader = generateFirstHeader(list);
		}

		for (int i = 0; i < list.size(); i++) {
			tempTask = list.get(i);
			if (tempTask.getEndDate() != null && isFloatingState == false) {
				if (!tempTask.getEndDateWithoutTime().equals(currentHeader)) {
					currentHeader = generateNewDateHeader(tempTask);
				}
			} else if (isDateNull(tempTask) && isFloatingState == false) {
				isFloatingState = generateFloatingTaskHeader();
			}

			GridPane taskDetail = new GridPane();

			taskDetail = generateTaskInfomation(tempTask, isFloatingState);
			taskList.getChildren().add(taskDetail);
		}
	}
	

	/**
	 * decides what kind of task display layout should be used. 
	 * @param tempTask 
	 * @param isFloatingState
	 * @return the correct task display layout as a GridPane
	 */
	private static GridPane generateTaskInfomation(Task tempTask, boolean isFloatingState) {
		GridPane taskDetail;
		if (isFloatingState) {
			taskDetail = createFloatingTaskDetail(tempTask);
		} else {
			taskDetail = createTaskDetail(tempTask);
		}
		return taskDetail;
	}

	private static boolean generateFloatingTaskHeader() {
		boolean isFloatingState;
		Text headerText;
		headerText = new Text("Floating");
		headerText.setFont(Font.font("Georgia", 20));
		HBox header = generateHearder(headerText);
		header.setStyle("-fx-background-color: linear-gradient(to right, #FFFF66 0%, #FFFFFF 80%);");
		taskList.getChildren().add(header);
		isFloatingState = true;
		return isFloatingState;
	}

	private static String generateNewDateHeader(Task tempTask) {
		String currentHeader;
		currentHeader = tempTask.getEndDateWithoutTime();
		generateCompleteHeader(currentHeader);
		return currentHeader;
	}

	private static String generateFirstHeader(ArrayList<Task> list) {
		String currentHeader;
		currentHeader = list.get(0).getEndDateWithoutTime();
		generateCompleteHeader(currentHeader);
		return currentHeader;
	}

	private static HBox generateHearder(Text headerText) {
		HBox header = new HBox();
		header.getChildren().add(headerText);

		return header;
	}

	private static GridPane createFloatingTaskDetail(Task tempTask) {
		GridPane taskDetail = new GridPane();
		Text index = new Text(tempTask.getIndex().toString() + ". ");
		Text eventTitle = new Text("Title: " + tempTask.getEventTitle());
		Text rank = new Text(getRankingText(tempTask.getImportance()));
		Text emptyLine = new Text("");
		
		index.setFont(Font.font(18));

		arrangeTextForFloatingTask(index, eventTitle, rank, emptyLine);

		taskDetail.getChildren().addAll(index, eventTitle, rank, emptyLine);

		taskDetail.setStyle("-fx-background-color: linear-gradient(to right, #FFCCFF 20%, #FFFFFF 80%);");

		return taskDetail;
	}

	private static void arrangeTextForFloatingTask(Text index, Text eventTitle, Text rank, Text emptyLine) {
		setConstraints(index, 0, 0);
		setConstraints(eventTitle, 1, 0);
		setConstraints(rank, 1, 1);
		setConstraints(emptyLine, 0, 2);
	}

	private static GridPane createTaskDetail(Task tempTask) {
		GridPane taskDetail = new GridPane();
		boolean showDates = false;
		boolean showRepeat = false;
		boolean showBlock = false;
		Text index = new Text(tempTask.getIndex().toString() + ". ");
		Text eventTitle = new Text("Title: " + tempTask.getEventTitle());
		Text emptyLine = new Text("");
		Text startDate = null;
		Text endDate = null;
		Text rank;
		Text repeatCycle = null;
		Text blocker = null;
		
		index.setFont(Font.font(18));

		if (tempTask.getStartDate() != null) {
			startDate = new Text("Start Date: " + tempTask.getStartDate());
			endDate = new Text("End Date: " + tempTask.getEndDate());
			showDates = true;
		} else {
			endDate = new Text("End Date: " + tempTask.getEndDate());
		}

		if (tempTask.isBlocking()) {
			blocker = new Text("     (Block Set)");
			showBlock = true;
		}

		rank = new Text(getRankingText(tempTask.getImportance()));

		if (tempTask.getRepeat()) {
			showRepeat = true;
			repeatCycle = new Text("Repeat for each: " + tempTask.getRepeatCycle() + " " + tempTask.getRepeatType());
		}

		setConstraints(index, 0, 0);
		setConstraints(eventTitle, 1, 0);
		if (showDates && showRepeat) {
			arrangeTextForRecurringTimelineTask(taskDetail, index, eventTitle, emptyLine, startDate, endDate, rank,
					repeatCycle);
		} else if (showDates && showBlock) {
			arrangeTextForBlockingTask(taskDetail, index, eventTitle, emptyLine, startDate, endDate, rank, blocker);
		} else if (showDates) {
			arrangeTextForTimelineTask(taskDetail, index, eventTitle, emptyLine, startDate, endDate, rank);
		} else if (showRepeat) {
			arrangeTextForRecurringTask(taskDetail, index, eventTitle, emptyLine, endDate, rank, repeatCycle);
		} else {
			arrangeTextForDeadlineTask(taskDetail, index, eventTitle, emptyLine, endDate, rank);
		}

		setTaskDetailBackgroundColor(tempTask, taskDetail);

		return taskDetail;
	}

	private static void setTaskDetailBackgroundColor(Task tempTask, GridPane taskDetail) {
		if (tempTask.isOverDate() && tempTask.isComplete() == false) {
			taskDetail.setStyle("-fx-background-color: linear-gradient(to right, #FF0000 20%, #FFFFFF 80%);");
		} else {
			taskDetail.setStyle("-fx-background-color: linear-gradient(to right, #00FFFF 20%, #FFFFFF 80%);");
		}
	}

	private static void arrangeTextForDeadlineTask(GridPane taskDetail, Text index, Text eventTitle, Text emptyLine,
			Text endDate, Text rank) {
		setConstraints(endDate, 1, 1);
		setConstraints(rank, 1, 2);
		setConstraints(emptyLine, 0, 3);
		taskDetail.getChildren().addAll(index, eventTitle, endDate, rank, emptyLine);
	}

	private static void arrangeTextForRecurringTask(GridPane taskDetail, Text index, Text eventTitle, Text emptyLine,
			Text endDate, Text rank, Text repeatCycle) {
		setConstraints(endDate, 1, 1);
		setConstraints(repeatCycle, 1, 2);
		setConstraints(rank, 1, 3);
		setConstraints(emptyLine, 0, 4);
		taskDetail.getChildren().addAll(index, eventTitle, endDate, repeatCycle, rank, emptyLine);
	}

	private static void arrangeTextForTimelineTask(GridPane taskDetail, Text index, Text eventTitle, Text emptyLine,
			Text startDate, Text endDate, Text rank) {
		setConstraints(startDate, 1, 1);
		setConstraints(endDate, 1, 2);
		setConstraints(rank, 1, 3);
		setConstraints(emptyLine, 0, 4);
		taskDetail.getChildren().addAll(index, eventTitle, startDate, endDate, rank, emptyLine);
	}

	private static void arrangeTextForBlockingTask(GridPane taskDetail, Text index, Text eventTitle, Text emptyLine,
			Text startDate, Text endDate, Text rank, Text blocker) {
		setConstraints(startDate, 1, 1);
		setConstraints(blocker, 2, 0);
		setConstraints(endDate, 1, 2);
		setConstraints(rank, 1, 3);
		setConstraints(emptyLine, 0, 4);
		taskDetail.getChildren().addAll(index, eventTitle, blocker, startDate, endDate, rank, emptyLine);
	}

	private static void arrangeTextForRecurringTimelineTask(GridPane taskDetail, Text index, Text eventTitle,
			Text emptyLine, Text startDate, Text endDate, Text rank, Text repeatCycle) {
		setConstraints(startDate, 1, 1);
		setConstraints(endDate, 1, 2);
		setConstraints(repeatCycle, 1, 3);
		setConstraints(rank, 1, 4);
		setConstraints(emptyLine, 0, 5);
		taskDetail.getChildren().addAll(index, eventTitle, startDate, endDate, repeatCycle, rank, emptyLine);
	}

	private static String getRankingText(Integer importance) {
		String rankDetail;

		if (importance == 1) {
			rankDetail = "Very Important";
		} else if (importance == 2) {
			rankDetail = "Important";
		} else {
			rankDetail = "Not so Important";
		}

		return rankDetail;
	}

	public static void displayEmpty() {
		Text emptyMessage;

		emptyMessage = new Text("No content to display");

		taskList.getChildren().clear();
		taskList.getChildren().add(emptyMessage);
	}

	
	/**
	 * Display list sorted in alphabetical order. 
	 * @param list
	 */
	public static void displayListAlpha(ArrayList<Task> list) {
		Task tempTask = new Task();
		GridPane taskDetail;
		String currentHeader = list.get(0).getTitle().substring(0, 1);
		Text headerText = new Text(currentHeader);
		
		assert list != null;

		taskList.getChildren().clear();

		generateFirstLetterHeader(headerText);

		for (int i = 0; i < list.size(); i++) {
			tempTask = list.get(i);
			if (list.get(i).getTitle().substring(0, 1).equals(currentHeader) == false) {
				currentHeader = generateNextLetterHeader(tempTask);
			}

			taskDetail = generateTaskInformation(tempTask);
			taskList.getChildren().add(taskDetail);
		}
	}

	private static GridPane generateTaskInformation(Task tempTask) {
		GridPane taskDetail;
		if (isDateNull(tempTask)) {
			taskDetail = createFloatingTaskDetail(tempTask);
		} else {
			taskDetail = createTaskDetailAlpha(tempTask);
		}
		return taskDetail;
	}

	private static String generateNextLetterHeader(Task tempTask) {
		String currentHeader;
		Text headerText;
		HBox header;
		currentHeader = tempTask.getTitle().substring(0, 1);
		headerText = new Text(currentHeader);

		headerText.setFont(Font.font("Georgia", 20));

		header = generateHearder(headerText);
		header.setStyle("-fx-background-color: linear-gradient(to right, #FFFF66 0%, #FFFFFF 80%);");
		taskList.getChildren().add(header);
		return currentHeader;
	}

	private static void generateFirstLetterHeader(Text headerText) {
		headerText.setFont(Font.font("Georgia", 20));
		HBox header = generateHearder(headerText);
		header.setStyle("-fx-background-color: linear-gradient(to right, #FFFF66 0%, #FFFFFF 80%);");
		taskList.getChildren().add(header);
	}

	private static GridPane createTaskDetailAlpha(Task tempTask) {
		GridPane taskDetail = new GridPane();
		boolean showDates = false;
		boolean showRepeat = false;
		boolean showBlock = false;
		Text index = new Text(tempTask.getIndex().toString() + ". ");
		Text eventTitle = new Text("Title: " + tempTask.getEventTitle());
		Text emptyLine = new Text("");
		Text startDate = null;
		Text endDate = null;
		Text repeatCycle = null;
		Text rank;
		Text blocker = null;

		index.setFont(Font.font(18));

		if (tempTask.getStartDate() != null) {
			startDate = new Text("Start Date: " + tempTask.getStartDate());
			endDate = new Text("End Date: " + tempTask.getEndDate());
			showDates = true;
		}

		if (tempTask.isBlocking()) {
			blocker = new Text("     (Block Set)");
			showBlock = true;
		}

		if (showDates == false) {
			if (tempTask.getEndDate() != null) {
				endDate = new Text("End Date: " + tempTask.getEndDate());
			}
		}

		if (tempTask.getRepeat()) {
			showRepeat = true;
			repeatCycle = new Text("Repeat for each: " + tempTask.getRepeatCycle() + " " + tempTask.getRepeatType());
		}

		rank = new Text(getRankingText(tempTask.getImportance()));

		setConstraints(index, 0, 0);
		setConstraints(eventTitle, 1, 0);
		if (showDates && showRepeat) {
			arrangeTextForRecurringTimelineTask(taskDetail, index, eventTitle, emptyLine, startDate, endDate,
					rank, repeatCycle);
		} else if (showDates && showBlock) {
			arrangeTextForBlockingTask(taskDetail, index, eventTitle, emptyLine, startDate, endDate, rank, blocker);
		} else if (showDates) {
			arrangeTextForTimelineTask(taskDetail, index, eventTitle, emptyLine, startDate, endDate, rank);
		} else if (showRepeat) {
			arrangeTextForRecurringTask(taskDetail, index, eventTitle, emptyLine, endDate, repeatCycle, rank);
		} else {
			arrangeTextForDeadlineTask(taskDetail, index, eventTitle, emptyLine, endDate, rank);
		}

		if (tempTask.isOverDate()) {
			taskDetail.setStyle("-fx-background-color: linear-gradient(to right, #FF0000 20%, #FFFFFF 80%);");
		} else {
			taskDetail.setStyle("-fx-background-color: linear-gradient(to right, #00FFFF 20%, #FFFFFF 80%);");
		}

		return taskDetail;
	}

    /**
     * displays the list sorted by priority level. 
     * @param list
     */
	
	public static void displayListImpt(ArrayList<Task> list) {
		Task tempTask = new Task();
		GridPane taskDetail;
		String currentHeader = getRankingText(list.get(0).getImportance());
		Text headerText = new Text(currentHeader);
		
		assert list != null;

		taskList.getChildren().clear();
		
		generateFirstRankHeader(headerText);

		for (int i = 0; i < list.size(); i++) {
			tempTask = list.get(i);
			if (getRankingText(tempTask.getImportance()).equals(currentHeader) == false) {
				currentHeader = generateNextRankHeader(tempTask);
			}

			taskDetail = generateTaskInformationImpt(tempTask);
			taskList.getChildren().add(taskDetail);
		}
	}
	
	
	private static GridPane generateTaskInformationImpt(Task tempTask) {
		GridPane taskDetail;
		if (isDateNull(tempTask)) {
			taskDetail = createFloatingTaskDetailImpt(tempTask);
		} else {
			taskDetail = createTaskDetailImpt(tempTask);
		}
		return taskDetail;
	}

	private static String generateNextRankHeader(Task tempTask) {
		String currentHeader;
		Text headerText;
		HBox header;
		currentHeader = getRankingText(tempTask.getImportance());
		headerText = new Text(currentHeader);

		headerText.setFont(Font.font("Georgia", 20));
		header = generateHearder(headerText);
		header.setStyle("-fx-background-color: linear-gradient(to right, #FFFF66 0%, #FFFFFF 80%);");
		taskList.getChildren().add(header);
		return currentHeader;
	}

	private static void generateFirstRankHeader(Text headerText) {
		headerText.setFont(Font.font("Georgia", 20));
		HBox header = generateHearder(headerText);
		header.setStyle("-fx-background-color: linear-gradient(to right, #FFFF66 0%, #FFFFFF 80%);");
		taskList.getChildren().add(header);
	}
	
	private static GridPane createFloatingTaskDetailImpt(Task tempTask) {
		GridPane taskDetail = new GridPane();
		Text index = new Text(tempTask.getIndex().toString() + ". ");
		Text eventTitle = new Text("Title: " + tempTask.getEventTitle());
		Text emptyLine = new Text("");

		index.setFont(Font.font(18));

		arrangeTextForFloatingTask(index, eventTitle, emptyLine);

		taskDetail.getChildren().addAll(index, eventTitle, emptyLine);

		taskDetail.setStyle("-fx-background-color: linear-gradient(to right, #FFCCFF 20%, #FFFFFF 80%);");

		return taskDetail;
	}

	private static void arrangeTextForFloatingTask(Text index, Text eventTitle, Text emptyLine) {
		setConstraints(index, 0, 0);
		setConstraints(eventTitle, 1, 0);
		setConstraints(emptyLine, 0, 2);
	}

	private static GridPane createTaskDetailImpt(Task tempTask) {
		GridPane taskDetail = new GridPane();
		boolean showDates = false;
		boolean showRepeat = false;
		boolean showBlock = false;
		Text index = new Text(tempTask.getIndex().toString() + ". ");
		Text eventTitle = new Text("Title: " + tempTask.getEventTitle());
		Text emptyLine = new Text("");
		Text startDate = null;
		Text endDate = null;
		Text repeatCycle = null;
		Text blocker = null;

		index.setFont(Font.font(18));

		if (tempTask.getStartDate() != null) {
			startDate = new Text("Start Date: " + tempTask.getStartDate());
			endDate = new Text("End Date: " + tempTask.getEndDate());
			showDates = true;
		}

		if (showDates == false) {
			endDate = new Text("End Date: " + tempTask.getEndDate());
		}

		if (tempTask.isBlocking()) {
			blocker = new Text("     (Block Set)");
			showBlock = true;
		}

		if (tempTask.getRepeat()) {
			showRepeat = true;
			repeatCycle = new Text("Repeat for each: " + tempTask.getRepeatCycle() + " " + tempTask.getRepeatType());
		}

		setConstraints(index, 0, 0);
		setConstraints(eventTitle, 1, 0);
		if (showDates && showRepeat) {
			arrangeTextForRecurringTimelineTask(taskDetail, index, eventTitle, emptyLine, startDate, endDate, repeatCycle);
		} else if (showDates && showBlock) {
			arrangeTextForBlockingTask(taskDetail, index, eventTitle, emptyLine, startDate, endDate, blocker);
		} else if (showDates) {
			arrangeTextForTimelineTask(taskDetail, index, eventTitle, emptyLine, startDate, endDate);
		} else if (showRepeat) {
			arrangeTextForRecurringTask(taskDetail, index, eventTitle, emptyLine, endDate, repeatCycle);
		} else {
			arrangeTextForDeadlineTask(taskDetail, index, eventTitle, emptyLine, endDate);
		}

		if (tempTask.isOverDate()) {
			taskDetail.setStyle("-fx-background-color: linear-gradient(to right, #FF0000 20%, #FFFFFF 80%);");
		} else {
			taskDetail.setStyle("-fx-background-color: linear-gradient(to right, #00FFFF 20%, #FFFFFF 80%);");
		}

		return taskDetail;
	}

	private static void arrangeTextForDeadlineTask(GridPane taskDetail, Text index, Text eventTitle, Text emptyLine,
			Text endDate) {
		setConstraints(endDate, 1, 1);
		setConstraints(emptyLine, 0, 2);
		taskDetail.getChildren().addAll(index, eventTitle, endDate, emptyLine);
	}

	private static void arrangeTextForRecurringTask(GridPane taskDetail, Text index, Text eventTitle, Text emptyLine,
			Text endDate, Text repeatCycle) {
		setConstraints(endDate, 1, 1);
		setConstraints(repeatCycle, 1, 2);
		setConstraints(emptyLine, 0, 4);
		taskDetail.getChildren().addAll(index, eventTitle, endDate, repeatCycle, emptyLine);
	}

	private static void arrangeTextForTimelineTask(GridPane taskDetail, Text index, Text eventTitle, Text emptyLine,
			Text startDate, Text endDate) {
		setConstraints(startDate, 1, 1);
		setConstraints(endDate, 1, 2);
		setConstraints(emptyLine, 0, 3);
		taskDetail.getChildren().addAll(index, eventTitle, endDate, emptyLine);
		
	}

	private static void arrangeTextForBlockingTask(GridPane taskDetail, Text index, Text eventTitle, Text emptyLine,
			Text startDate, Text endDate, Text blocker) {
		setConstraints(startDate, 1, 1);
		setConstraints(blocker, 2, 0);
		setConstraints(endDate, 1, 2);
		setConstraints(emptyLine, 0, 3);
		taskDetail.getChildren().addAll(index, eventTitle, blocker, startDate, endDate, emptyLine);
	}

	private static void arrangeTextForRecurringTimelineTask(GridPane taskDetail, Text index, Text eventTitle,
			Text emptyLine, Text startDate, Text endDate, Text repeatCycle) {
		setConstraints(startDate, 1, 1);
		setConstraints(endDate, 1, 2);
		setConstraints(repeatCycle, 1, 3);
		setConstraints(emptyLine, 0, 4);
		taskDetail.getChildren().addAll(index, eventTitle, startDate, endDate, repeatCycle, emptyLine);
	}

	private static boolean isDateNull(Task tempTask) {
		return tempTask.getEndDate() == null;
	}
	
	/**
	 * displays the completed task list on the screen 
	 * @param list
	 */
	public static void displayListComplete(ArrayList<Task> list) {
		Task tempTask = new Task();
		String currentHeader = "Complete";
		taskList.getChildren().clear();

		if (list.isEmpty()) {
			displayEmpty();
			return;
		}

		generateCompleteHeader(currentHeader);

		for (int i = 0; i < list.size(); i++) {
			tempTask = list.get(i);

			GridPane taskDetail = new GridPane();

			taskDetail = generateCompletedTaskInformation(tempTask);
			taskList.getChildren().add(taskDetail);
		}
	}

	private static void generateCompleteHeader(String currentHeader) {
		Text headerText;
		headerText = new Text(currentHeader);
		headerText.setFont(Font.font("Georgia", 20));
		HBox header = generateHearder(headerText);
		header.setStyle("-fx-background-color: linear-gradient(to right, #FFFF66 0%, #FFFFFF 80%);");
		taskList.getChildren().add(header);
	}

	private static GridPane generateCompletedTaskInformation(Task tempTask) {
		GridPane taskDetail;
		if (tempTask.getEndDate() == null) {
			taskDetail = createFloatingTaskDetail(tempTask);
		} else {
			taskDetail = createTaskDetail(tempTask);
		}
		return taskDetail;
	}

	public static ScrollPane getScreen() {
		return Screen;
	}
	
	
}
```
###### src\listItUI\TopBar.java
``` java
package listItUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

/**
 * This class helps constructs the toolbar at the top of our GUI by creating our ListIt icon, 
 * as well as a close button which helps close ListIt with the use of a mouse. 
 * 
 * @version 0.5
 */

public class TopBar extends GridPane implements EventHandler<ActionEvent>{
	
	private Text listItLabel;
	private Button closeButton;
	
	public TopBar() {
		
		setupSoftwareLabel();
		
		setupCloseButton();
		
		setConstraints(listItLabel, 1, 0);
		setConstraints(closeButton, 2, 0);
		
		ColumnConstraints col0Constraints = new ColumnConstraints();
		col0Constraints.setPercentWidth(2);
		ColumnConstraints col1Constraints = new ColumnConstraints();
		col1Constraints.setPercentWidth(88);
		ColumnConstraints col2Constraints = new ColumnConstraints();
		col2Constraints.setPercentWidth(10);
		
		getColumnConstraints().addAll(col0Constraints, col1Constraints, col2Constraints);
		getChildren().addAll(listItLabel, closeButton);
	}

	private void setupSoftwareLabel() {
		listItLabel = new Text("List It");
		listItLabel.setFont(Font.font("Tonto", FontPosture.ITALIC, 30));
		listItLabel.setStyle("-fx-fill: linear-gradient(#0000FF 10%, #FFFFFF 30%, #0000FF 50%, #FFFFFF 70%, #0000FF 90%);"
				+ "-fx-stroke: black;");
	}

	private void setupCloseButton() {
		closeButton = new Button();
		closeButton.setOnAction(this);
		
		Image closeIcon = new Image(getClass().getResourceAsStream("icon1.png"));
		ImageView iconView = new ImageView(closeIcon);
		iconView.setFitHeight(40);
		iconView.setFitWidth(40);
		closeButton.setMaxSize(40, 40);
		closeButton.setGraphic(iconView);
	}

	@Override
	public void handle(ActionEvent event) {
		if (event.getSource() == closeButton) {
			System.exit(0);
		}
	}
}
```
###### src\listItUI\UIMain.java
``` java
=======

>>>>>>> origin/master
package listItUI;

import java.util.ArrayList;

/**
 * This class helps form the user interface of ListIt. 
 * it contains the basic settings for creating the entire interface of our application. 
 * It involves the creation of the different panels. 
 * 
 * @version 0.5
 */

import fileModifier.FileModifier;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UIMain extends Application {
	
	InputTextPane inputBox = new InputTextPane();
	OutputScreenPane screenBox = new OutputScreenPane();
	FeedbackPane feedbackBox = new FeedbackPane();
	TopBar topBar = new TopBar();
	private double xOffset = 0;
    private double yOffset = 0;
	static FileModifier modifier = FileModifier.getInstance();

	public static void main(String[] args) {
		launch(args);
		modifier.display(modifier.getContentList());
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("ListIt");
		primaryStage.initStyle(StageStyle.UNDECORATED);
		
		GridPane layout = new GridPane();
		
		componentLayoutSetup(layout);
		
		windowDragSetup(primaryStage, layout);
		
		Scene scene = new Scene(layout, 630, 600);
		scene.getStylesheets().add("listItUI/ListItUITheme.css");
		
		OutputScreenPane.getScreen().setFocusTraversable(true);
		inputBox.getTextField().requestFocus();
		
		primaryStage.setScene(scene);
		primaryStage.show();
		modifier.display(modifier.getContentList());
	}
    /**
     * this allows the ListIt window to be dragged around.  
     * @param primaryStage
     * @param layout
     */
	private void windowDragSetup(Stage primaryStage, GridPane layout) {
		layout.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        layout.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });
	}

	private void componentLayoutSetup(GridPane layout) {
		GridPane.setConstraints(topBar, 0, 0);
		GridPane.setConstraints(screenBox, 0, 1);
		GridPane.setConstraints(feedbackBox, 0, 2);
		GridPane.setConstraints(inputBox, 0, 3);
		
		layout.getChildren().addAll(topBar, screenBox, feedbackBox, inputBox);
	}
	
	
	/**
	 * this helps construct the help window and pops it up on the screen 
	 * @param commands
	 * @param methods
	 */
	public static void popUpHelp(ArrayList<String> commands, ArrayList<String> methods) {
		Stage helpStage = new Stage();
		int i=0, j=0;
		
		assert commands != null;
		assert methods != null;
		
		helpStage.setTitle("Cheat Sheet");
		
		ScrollPane helpLayout = new ScrollPane();
		
		GridPane helpSheet = new GridPane();
		
		ColumnConstraints col0Constraints = new ColumnConstraints();
		col0Constraints.setPercentWidth(40);
		ColumnConstraints col1Constraints = new ColumnConstraints();
		col1Constraints.setPercentWidth(60);
		
		helpSheet.getColumnConstraints().addAll(col0Constraints, col1Constraints);
		
		for(i=0; i<commands.size(); i++) {
			Text command = new Text(commands.get(i));
			Text method = new Text(methods.get(j));
			
			GridPane.setConstraints(command, 0, i);
			GridPane.setConstraints(method, 1, j);
			
			helpSheet.getChildren().addAll(command, method);
			j++;
		}
		
		helpLayout.setContent(helpSheet);
		
		Scene helpScene = new Scene(helpLayout, 1000, 600);
		helpStage.setScene(helpScene);
		helpStage.show();
	}
}
```
###### src\Test\UnitTest.java
``` java
	@BeforeClass
	public static void setUpApplication() throws InterruptedException {
		// Initialize Java FX
		System.out.printf("About to launch FX App ListIt\n");
		Thread t = new Thread("JavaFX Init Thread") {
			public void run() {
				Application.launch(UIMain.class, new String[0]);
			}
		};
		t.setDaemon(true);
		t.start();
		System.out.printf("FX App ListIt thread started\n");
		Thread.sleep(500);
	}

	@Rule
	public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

```
###### src\Test\UnitTest.java
``` java
	@Test
	public void testClear() {
		clearExpectedActual();
		DeleteLogic.clearFile();
		compareResults("test clear", expected, actual);
	}

```
###### src\Test\UnitTest.java
``` java
	@Test
	public void testEdit2() {
		clearExpectedActual();
		Task task1 = new Task("EE2020 Oscilloscope project", "03112015");
		addEvent(task1, "add EE2020 Oscilloscope project by 03112015");
		EditLogic.editEvent("edit 1 by date 11142015");
		actual = modifier.getContentList();
		compareResults("test if invalid date for edit works", expected, actual);
	}

	@Test
	// edit importance testing with correct input
	public void testEdit3() {
		clearExpectedActual();
		AddLogic.addEventWithImportance("add go for manicure and pedicure rank 3");
		EditLogic.editEvent("edit 1 by impt 2");
		actual = modifier.getContentList();
		getExpectedforEditImpt();
		compareResults("test if edit by impt works", expected, actual);
	}

	@Test
	// edit importance with wrong input
	public void testEdit4() {
		clearExpectedActual();
		EditLogic.editEvent("edit 2 by impt 5");
		actual = modifier.getContentList();
		compareResults("test if invalid importance level for edit works", expected, actual);
	}

	@Test
	// edit title correct input
	public void testEdit5() {
		clearExpectedActual();
		getExpectedforEditTitle();
		AddLogic.addEventWithDeadline("add 0P2 presentation by 12112015");
		EditLogic.editEvent("edit 1 by title Oral presentation 2");
		actual = modifier.getContentList();
		compareResults("test if edit by title works", expected, actual);
	}

	// edit title with wrong input
	public void testEdit6() {
		clearExpectedActual();
		AddLogic.addEventWithDeadline("add 0P2 presentation by 12112015");
		EditLogic.editEvent("edit 2 by title Oral presentation 2 ");
		actual = modifier.getContentList();
		compareResults("test if edit invalid number works", expected, actual);
	}
 
```
###### src\Test\UnitTest.java
``` java
	@Test
	public void testSort1() {
		clearExpectedActual();
		Task task1 = new Task("EE2020 Oscilloscope project", "03112015");
		addEvent(task1, "add EE2020 Oscilloscope project on 03112015");
		Task task2 = new Task("OP2 presentation", "06112015");
		addEvent(task2, "add OP2 presentation by 06112015");
		actual = modifier.getContentList();
		compareResults("test if sort works", expected, actual);
	}
	
	@Test
	public void testSort2() {
		clearExpectedActual();
		Task task1 = new Task("go to school", "08112015");
		Task task2 = new Task("go home", "10112015");
		Task task3 = new Task("have lunch", "14112015");
		expected.add(task1);
		expected.add(task2);
		expected.add(task3);
		
		AddLogic.addEventWithDeadline("add have lunch by 14112015");
		AddLogic.addEventWithDeadline("add go home by 10112015");
		AddLogic.addEventWithDeadline("add go to school by 08112015");
		
		actual = modifier.getContentList();
		compareResults("test if sort works", expected, actual);
	}
	
	@Test
	public void testSort3() {
		clearExpectedActual();
		Task task1 = new Task("go to school", "08112015");
		Task task2 = new Task("play game", "10112015 0800", "10112015 1000", true);
		Task task3 = new Task("have lunch");
		expected.add(task1);
		expected.add(task2);
		expected.add(task3);
		
		AddLogic.addEventDefault("add have lunch");
		AddLogic.addEventWithTimeline("add play game on 10112015 from 0800 to 1000");
		AddLogic.addEventWithDeadline("add go to school by 08112015");
		
		actual = modifier.getContentList();
		compareResults("test if sort works", expected, actual);
	}
}
```
