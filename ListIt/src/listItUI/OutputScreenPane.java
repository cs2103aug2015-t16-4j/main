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
		
		taskList.getChildren().clear();
		
		for(int i = 0; i<list.size(); i++) {
			tempTask = list.get(i);
			
			GridPane taskDetail = new GridPane();
			taskDetail = createTaskDetail(tempTask);
			
			taskList.getChildren().add(taskDetail);
		}
	}

	private static GridPane createTaskDetail(Task tempTask) {
		GridPane taskDetail = new GridPane();
		Text taskTitle = new Text(tempTask.getTitle());
		Text taskDeadLine = new Text(tempTask.getDate());
		Text taskImportance = new Text(getRankingText(tempTask.getImportance()));
		
		GridPane.setConstraints(taskTitle, 0, 0);
		GridPane.setConstraints(taskDeadLine, 0, 2);
		GridPane.setConstraints(taskImportance, 2, 0);
		
		taskDetail.getChildren().addAll(taskTitle, taskDeadLine, taskImportance);
		
		return null;
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
