package listItUI;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
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
		
		if(list.isEmpty()) {
			displayEmpty();
			return;
		}
		
		if (list.get(0).getDate() != null) {
			currentHeader = list.get(0).getDate();
			headerText = new Text(currentHeader);

			headerText.setFont(Font.font("Georgia", 20));
			taskList.getChildren().add(headerText);
		}
		
		for(int i = 0; i<list.size(); i++) {
			tempTask = list.get(i);
			if (tempTask.getDate() != null && isFloatingState == false) {
				if (!tempTask.getDate().equals(currentHeader)) {
					currentHeader = tempTask.getDate();
					headerText = new Text(currentHeader);
					headerText.setFont(Font.font("Georgia", 20));
					taskList.getChildren().add(headerText);
				}
			} else if (isDateNull(tempTask) && isFloatingState == false){
				headerText = new Text("Floating");
				headerText.setFont(Font.font("Georgia", 20));
				taskList.getChildren().add(headerText);
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

		return taskDetail;
	}

	private static GridPane createTaskDetail(Task tempTask) {
		GridPane taskDetail = new GridPane();
		Text index = new Text(tempTask.getIndex().toString() + ". ");
		Text eventTitle = new Text("Title: " + tempTask.getEventTitle());
		Text emptyLine = new Text("");
		Text timeLine;
		Text rank;
		
		index.setFont(Font.font(18));
		
		if (tempTask.getStartTime() != null) {
			timeLine = new Text("Time: " + tempTask.getStartTime() + " to " + tempTask.getEndTime());
		} else {
			timeLine = new Text("Time: -NA");
		}
		
		rank = new Text(getRankingText(tempTask.getImportance()));
		
		setConstraints(index, 0, 0);
		setConstraints(eventTitle, 1, 0);
		setConstraints(timeLine, 1, 1);
		setConstraints(rank, 1, 2);
		setConstraints(emptyLine, 0, 3);
		
		taskDetail.getChildren().addAll(index, eventTitle, timeLine, rank, emptyLine);
		
		return taskDetail;
	}

	private static String getRankingText(Integer importance) {
		String rankDetail;
		
		if (importance == 1) {
			rankDetail = "Very Important";
		} else if(importance == 2) {
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
		
		taskList.getChildren().add(headerText);
		
		for (int i = 0; i < list.size(); i++) {
			tempTask = list.get(i);
			//Shi hao can you please refractor the conditional statement to another method
			//it must be of type boolean and the name must positive so you will  put !method
			if (list.get(i).getTitle().substring(0, 1).equals(currentHeader) == false) {
				currentHeader = tempTask.getTitle().substring(0, 1);
				headerText = new Text(currentHeader);
				
				headerText.setFont(Font.font("Georgia", 20));
				
				taskList.getChildren().add(headerText);
			}
			
			if (isDateNull(tempTask)) {
				taskDetail = createFloatingTaskDetail(tempTask);
			} else {
				taskDetail = createTaskDetail(tempTask);
			}
			taskList.getChildren().add(taskDetail);
		}
	}

	public static void displayListImpt(ArrayList<Task> list) {
		Task tempTask = new Task();
		GridPane taskDetail;
		String currentHeader = getRankingText(list.get(0).getImportance());
		Text headerText = new Text(currentHeader);
		
		headerText.setFont(Font.font("Georgia", 20));
		
		taskList.getChildren().clear();
		
		taskList.getChildren().add(headerText);
		
		for (int i = 0; i < list.size(); i++) {
			tempTask = list.get(i);
			//Shi Hao can you please refractor the statement in the bracket to a method
			//it must be of type boolean and the name must positive so you will  put !method
			if (getRankingText(tempTask.getImportance()).equals(currentHeader) == false) {
				currentHeader = getRankingText(tempTask.getImportance());
				headerText = new Text(currentHeader);
				
				headerText.setFont(Font.font("Georgia", 20));
				taskList.getChildren().add(headerText);
			}
			
			if (isDateNull(tempTask)) {
				taskDetail = createFloatingTaskDetail(tempTask);
			} else {
				taskDetail = createTaskDetail(tempTask);
			}
			taskList.getChildren().add(taskDetail);
		}
	}

	private static boolean isDateNull(Task tempTask) {
		return tempTask.getDate() == null;
	}
}
