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

	private Text displayHeader;
	private static VBox taskList;
	private static ScrollPane Screen;

	public OutputScreenPane() {
		setPadding(new Insets(10, 10, 10, 10));

		displayHeader = new Text("Display:");
		displayHeader.setFont(Font.font("Verdana", FontPosture.ITALIC, 20));
		displayHeader.setStyle("-fx-fill: linear-gradient(#0033CC 30%, #0029A3 60%, #001A66 90%);");

		taskList = new VBox();
		taskList.setStyle("-fx-background-color: #FFFFFF;");

		taskList.setPrefSize(420, 450);

		Screen = new ScrollPane();
		Screen.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

		Screen.setContent(taskList);

		setConstraints(displayHeader, 0, 0);
		setConstraints(Screen, 0, 1);

		getChildren().addAll(displayHeader, Screen);
	}

	public static void displayList(ArrayList<Task> list) {
		Task tempTask = new Task();
		boolean isFloatingState = false;
		String currentHeader = null;
		Text headerText = null;

		taskList.getChildren().clear();

		if (list.isEmpty()) {
			displayEmpty();
			return;
		}

		if (list.get(0).getEndDate() != null) {
			currentHeader = list.get(0).getEndDateWithoutTime();
			headerText = new Text(currentHeader);
			headerText.setFont(Font.font("Georgia", 20));
			HBox header = generateHearder(headerText);
			header.setStyle("-fx-background-color: linear-gradient(to right, #FFFF66 0%, #FFFFFF 80%);");
			taskList.getChildren().add(header);
		}

		for (int i = 0; i < list.size(); i++) {
			tempTask = list.get(i);
			if (tempTask.getEndDate() != null && isFloatingState == false) {
				if (!tempTask.getEndDateWithoutTime().equals(currentHeader)) {
					currentHeader = tempTask.getEndDateWithoutTime();
					headerText = new Text(currentHeader);
					headerText.setFont(Font.font("Georgia", 20));
					HBox header = generateHearder(headerText);
					header.setStyle("-fx-background-color: linear-gradient(to right, #FFFF66 0%, #FFFFFF 80%);");
					taskList.getChildren().add(header);
				}
			} else if (isDateNull(tempTask) && isFloatingState == false) {
				headerText = new Text("Floating");
				headerText.setFont(Font.font("Georgia", 20));
				HBox header = generateHearder(headerText);
				header.setStyle("-fx-background-color: linear-gradient(to right, #FFFF66 0%, #FFFFFF 80%);");
				taskList.getChildren().add(header);
				isFloatingState = true;
			}

			GridPane taskDetail = new GridPane();

			if (isFloatingState) {
				taskDetail = createFloatingTaskDetail(tempTask);
			} else {
				taskDetail = createTaskDetail(tempTask);
			}
			taskList.getChildren().add(taskDetail);
		}
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

		setConstraints(index, 0, 0);
		setConstraints(eventTitle, 1, 0);
		setConstraints(rank, 1, 1);
		setConstraints(emptyLine, 0, 2);

		taskDetail.getChildren().addAll(index, eventTitle, rank, emptyLine);

		taskDetail.setStyle("-fx-background-color: linear-gradient(to right, #FFCCFF 20%, #FFFFFF 80%);");

		return taskDetail;
	}

	private static GridPane createTaskDetail(Task tempTask) {
		GridPane taskDetail = new GridPane();
		boolean showDates = false;
		boolean showRepeat = false;
		Text index = new Text(tempTask.getIndex().toString() + ". ");
		Text eventTitle = new Text("Title: " + tempTask.getEventTitle());
		Text emptyLine = new Text("");
		Text startDate = null;
		Text endDate = null;
		Text rank;
		Text repeatCycle = null;

		index.setFont(Font.font(18));

		if (tempTask.getStartDate() != null) {
			startDate = new Text("Start Date: " + tempTask.getStartDate());
			endDate = new Text("End Date: " + tempTask.getEndDate());
			showDates = true;
		} else {
			endDate = new Text("End Date: " + tempTask.getEndDate());
		}

		rank = new Text(getRankingText(tempTask.getImportance()));

		if (tempTask.getRepeat()) {
			showRepeat = true;
			repeatCycle = new Text("Repeat for each: " + tempTask.getRepeatCycle() + " " + tempTask.getRepeatType());
		}

		setConstraints(index, 0, 0);
		setConstraints(eventTitle, 1, 0);
		if (showDates && showRepeat) {
			setConstraints(startDate, 1, 1);
			setConstraints(endDate, 1, 2);
			setConstraints(repeatCycle, 1, 3);
			setConstraints(rank, 1, 4);
			setConstraints(emptyLine, 0, 5);
			taskDetail.getChildren().addAll(index, eventTitle, startDate, endDate, repeatCycle, rank, emptyLine);
		} else if (showDates) {
			setConstraints(startDate, 1, 1);
			setConstraints(endDate, 1, 2);
			setConstraints(rank, 1, 3);
			setConstraints(emptyLine, 0, 4);
			taskDetail.getChildren().addAll(index, eventTitle, startDate, endDate, rank, emptyLine);
		} else if (showRepeat) {
			setConstraints(endDate, 1, 1);
			setConstraints(repeatCycle, 1, 2);
			setConstraints(rank, 1, 3);
			setConstraints(emptyLine, 0, 4);
			taskDetail.getChildren().addAll(index, eventTitle, endDate, repeatCycle, rank, emptyLine);
		} else {
			setConstraints(endDate, 1, 1);
			setConstraints(rank, 1, 2);
			setConstraints(emptyLine, 0, 3);
			taskDetail.getChildren().addAll(index, eventTitle, endDate, rank, emptyLine);
		}

		if (tempTask.isOverDate() && tempTask.isComplete() == false) {
			taskDetail.setStyle("-fx-background-color: linear-gradient(to right, #FF0000 20%, #FFFFFF 80%);");
		} else {
			taskDetail.setStyle("-fx-background-color: linear-gradient(to right, #00FFFF 20%, #FFFFFF 80%);");
		}

		return taskDetail;
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

		taskList.getChildren().clear();

		headerText.setFont(Font.font("Georgia", 20));

		HBox header = generateHearder(headerText);
		header.setStyle("-fx-background-color: linear-gradient(to right, #FFFF66 0%, #FFFFFF 80%);");
		taskList.getChildren().add(header);

		for (int i = 0; i < list.size(); i++) {
			tempTask = list.get(i);
			if (list.get(i).getTitle().substring(0, 1).equals(currentHeader) == false) {
				currentHeader = tempTask.getTitle().substring(0, 1);
				headerText = new Text(currentHeader);

				headerText.setFont(Font.font("Georgia", 20));

				header = generateHearder(headerText);
				header.setStyle("-fx-background-color: linear-gradient(to right, #FFFF66 0%, #FFFFFF 80%);");
				taskList.getChildren().add(header);
			}

			if (isDateNull(tempTask)) {
				taskDetail = createFloatingTaskDetail(tempTask);
			} else {
				taskDetail = createTaskDetailAlpha(tempTask);
			}
			taskList.getChildren().add(taskDetail);
		}
	}

	private static GridPane createTaskDetailAlpha(Task tempTask) {
		GridPane taskDetail = new GridPane();
		boolean showDates = false;
		boolean showRepeat = false;
		Text index = new Text(tempTask.getIndex().toString() + ". ");
		Text eventTitle = new Text("Title: " + tempTask.getEventTitle());
		Text emptyLine = new Text("");
		Text startDate = null;
		Text endDate = null;
		Text repeatCycle = null;
		Text rank;

		index.setFont(Font.font(18));

		if (tempTask.getStartDate() != null) {
			startDate = new Text("Start Date: " + tempTask.getStartDate());
			endDate = new Text("End Date: " + tempTask.getEndDate());
			showDates = true;
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
			setConstraints(startDate, 1, 1);
			setConstraints(endDate, 1, 2);
			setConstraints(repeatCycle, 1, 3);
			setConstraints(rank, 1, 4);
			setConstraints(emptyLine, 0, 5);
			taskDetail.getChildren().addAll(index, eventTitle, startDate, endDate, repeatCycle, rank, emptyLine);
		} else if (showDates) {
			setConstraints(startDate, 1, 1);
			setConstraints(endDate, 1, 2);
			setConstraints(rank, 1, 3);
			setConstraints(emptyLine, 0, 4);
			taskDetail.getChildren().addAll(index, eventTitle, startDate, endDate, rank, emptyLine);
		} else if (showRepeat) {
			setConstraints(endDate, 1, 1);
			setConstraints(repeatCycle, 1, 2);
			setConstraints(rank, 1, 3);
			setConstraints(emptyLine, 0, 4);
			taskDetail.getChildren().addAll(index, eventTitle, endDate, repeatCycle, rank, emptyLine);
		} else {
			setConstraints(endDate, 1, 1);
			setConstraints(rank, 1, 2);
			setConstraints(emptyLine, 0, 3);
			taskDetail.getChildren().addAll(index, eventTitle, endDate, rank, emptyLine);
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

		headerText.setFont(Font.font("Georgia", 20));

		taskList.getChildren().clear();

		HBox header = generateHearder(headerText);
		header.setStyle("-fx-background-color: linear-gradient(to right, #FFFF66 0%, #FFFFFF 80%);");
		taskList.getChildren().add(header);

		for (int i = 0; i < list.size(); i++) {
			tempTask = list.get(i);
			if (getRankingText(tempTask.getImportance()).equals(currentHeader) == false) {
				currentHeader = getRankingText(tempTask.getImportance());
				headerText = new Text(currentHeader);

				headerText.setFont(Font.font("Georgia", 20));
				header = generateHearder(headerText);
				header.setStyle("-fx-background-color: linear-gradient(to right, #FFFF66 0%, #FFFFFF 80%);");
				taskList.getChildren().add(header);
			}

			if (isDateNull(tempTask)) {
				taskDetail = createFloatingTaskDetailImpt(tempTask);
			} else {
				taskDetail = createTaskDetailImpt(tempTask);
			}
			taskList.getChildren().add(taskDetail);
		}
	}

	private static GridPane createFloatingTaskDetailImpt(Task tempTask) {
		GridPane taskDetail = new GridPane();
		Text index = new Text(tempTask.getIndex().toString() + ". ");
		Text eventTitle = new Text("Title: " + tempTask.getEventTitle());
		Text emptyLine = new Text("");

		index.setFont(Font.font(18));

		setConstraints(index, 0, 0);
		setConstraints(eventTitle, 1, 0);
		setConstraints(emptyLine, 0, 2);

		taskDetail.getChildren().addAll(index, eventTitle, emptyLine);

		taskDetail.setStyle("-fx-background-color: linear-gradient(to right, #FFCCFF 20%, #FFFFFF 80%);");

		return taskDetail;
	}

	private static GridPane createTaskDetailImpt(Task tempTask) {
		GridPane taskDetail = new GridPane();
		boolean showDates = false;
		boolean showRepeat = false;
		Text index = new Text(tempTask.getIndex().toString() + ". ");
		Text eventTitle = new Text("Title: " + tempTask.getEventTitle());
		Text emptyLine = new Text("");
		Text startDate = null;
		Text endDate = null;
		Text repeatCycle = null;

		index.setFont(Font.font(18));

		if (tempTask.getStartDate() != null) {
			startDate = new Text("Start Date: " + tempTask.getStartDate());
			endDate = new Text("End Date: " + tempTask.getEndDate());
			showDates = true;
		}

		if (showDates == false) {
			endDate = new Text("End Date: " + tempTask.getEndDate());
		}

		if (tempTask.getRepeat()) {
			showRepeat = true;
			repeatCycle = new Text("Repeat for each: " + tempTask.getRepeatCycle() + " " + tempTask.getRepeatType());
		}

		setConstraints(index, 0, 0);
		setConstraints(eventTitle, 1, 0);
		if (showDates && showRepeat) {
			setConstraints(startDate, 1, 1);
			setConstraints(endDate, 1, 2);
			setConstraints(repeatCycle, 1, 3);
			setConstraints(emptyLine, 0, 4);
			taskDetail.getChildren().addAll(index, eventTitle, startDate, endDate, repeatCycle, emptyLine);
		} else if (showDates) {
			setConstraints(startDate, 1, 1);
			setConstraints(endDate, 1, 2);
			setConstraints(emptyLine, 0, 3);
			taskDetail.getChildren().addAll(index, eventTitle, startDate, endDate, emptyLine);
		} else if (showRepeat) {
			setConstraints(endDate, 1, 1);
			setConstraints(repeatCycle, 1, 2);
			setConstraints(emptyLine, 0, 3);
			taskDetail.getChildren().addAll(index, eventTitle, endDate, repeatCycle, emptyLine);
		} else {
			setConstraints(endDate, 1, 1);
			setConstraints(emptyLine, 0, 2);
			taskDetail.getChildren().addAll(index, eventTitle, endDate, emptyLine);
		}

		if (tempTask.isOverDate()) {
			taskDetail.setStyle("-fx-background-color: linear-gradient(to right, #FF0000 20%, #FFFFFF 80%);");
		} else {
			taskDetail.setStyle("-fx-background-color: linear-gradient(to right, #00FFFF 20%, #FFFFFF 80%);");
		}

		return taskDetail;
	}

	private static boolean isDateNull(Task tempTask) {
		return tempTask.getEndDate() == null;
	}

	public static void displayListComplete(ArrayList<Task> list) {
		Task tempTask = new Task();
		String currentHeader = "Complete";
		Text headerText = null;

		taskList.getChildren().clear();

		if (list.isEmpty()) {
			displayEmpty();
			return;
		}

		headerText = new Text(currentHeader);
		headerText.setFont(Font.font("Georgia", 20));
		HBox header = generateHearder(headerText);
		header.setStyle("-fx-background-color: linear-gradient(to right, #FFFF66 0%, #FFFFFF 80%);");
		taskList.getChildren().add(header);

		for (int i = 0; i < list.size(); i++) {
			tempTask = list.get(i);

			GridPane taskDetail = new GridPane();

			if (tempTask.getEndDate() == null) {
				taskDetail = createFloatingTaskDetail(tempTask);
			} else {
				taskDetail = createTaskDetail(tempTask);
			}
			taskList.getChildren().add(taskDetail);
		}
	}
}
