package listItUI;

import java.util.ArrayList;

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
}
