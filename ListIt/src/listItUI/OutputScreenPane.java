package listItUI;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import taskGenerator.Task;

public class OutputScreenPane extends GridPane {
	
	private Text displayHeader;
	private static VBox taskList;
	
	public OutputScreenPane() {
		setPadding(new Insets(10, 10, 10, 10));
		
		displayHeader = new Text("Display:");
		displayHeader.setFont(Font.font("Verdana", FontPosture.ITALIC, 20));
		displayHeader.setStyle("-fx-fill: linear-gradient(#0033CC 30%, #0029A3 60%, #001A66 90%);");
		
		taskList = new VBox();
		taskList.setStyle("-fx-background-color: #FFFFFF;");
		
		taskList.setPrefSize(500, 400);
		
		setConstraints(displayHeader, 0, 0);
		setConstraints(taskList, 0, 1);
		
		getChildren().addAll(displayHeader, taskList);
	}
	
	public static void displayList(ArrayList<Task> list) {
		Task tempTask = new Task();
		String currentHeader = list.get(0).getDate();
		Text headerText = new Text(currentHeader);
		boolean isFloatingState = false;
		
		headerText.setFont(Font.font("Georgia", 20));
		
		taskList.getChildren().clear();
		taskList.getChildren().add(headerText);
		
		for(int i = 0; i<list.size(); i++) {
			tempTask = list.get(i);
			if(tempTask.getDate() != null && isFloatingState == false) {
				if(!tempTask.getDate().equals(currentHeader)) {
					currentHeader = tempTask.getDate();
					headerText = new Text(currentHeader);
					headerText.setFont(Font.font("Georgia", 20));
					taskList.getChildren().add(headerText);
				}
			}
			else if(tempTask.getDate() == null && isFloatingState == false){
				headerText = new Text("Floating");
				headerText.setFont(Font.font("Georgia", 20));
				taskList.getChildren().add(headerText);
				isFloatingState = true;
			}
			
			GridPane taskDetail = new GridPane();
			
			if(isFloatingState) {
				taskDetail = createTaskDetail(tempTask);
			}
			else {
				taskDetail = createFloatingTaskDetail(tempTask);
			}
			taskList.getChildren().add(taskDetail);
		}
	}

	private static GridPane createFloatingTaskDetail(Task tempTask) {
		GridPane taskDetail = new GridPane();
		Text eventTitle = new Text("Title: " + tempTask.getEventTitle());
		Text rank = new Text(getRankingText(tempTask.getImportance()));
		
		setConstraints(eventTitle, 0, 0);
		setConstraints(rank, 0, 1);
		
		taskDetail.getChildren().addAll(eventTitle, rank);

		return null;
	}

	private static GridPane createTaskDetail(Task tempTask) {
		GridPane taskDetail = new GridPane();
		Text eventTitle = new Text("Title: " + tempTask.getEventTitle());
		Text timeLine;
		Text rank;
		
		if(tempTask.getStartTime() != null) {
			timeLine = new Text("Time: " + tempTask.getStartTime() + " to " + tempTask.getEndTime());
		}
		else {
			timeLine = new Text("Time: -NA");
		}
		
		rank = new Text(getRankingText(tempTask.getImportance()));
		
		setConstraints(eventTitle, 0, 0);
		setConstraints(timeLine, 0, 1);
		setConstraints(rank, 0, 2);
		
		taskDetail.getChildren().addAll(eventTitle, timeLine, rank);
		
		return taskDetail;
	}

	private static String getRankingText(Integer importance) {
		String rankDetail;
		
		if(importance == 1) {
			rankDetail = "Very Important";
		}
		else if(importance == 2) {
			rankDetail = "Important";
		}
		else {
			rankDetail = "Not so Important";
		}
		
		return rankDetail;
	}
	
	
	
	
}
