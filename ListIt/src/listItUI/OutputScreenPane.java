package listItUI;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

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
	
	public static void displayList(Text title, Text date) {
		GridPane task = new GridPane();
		Label t = new Label("Title: ");
		Label d = new Label("date: ");
		
		setConstraints(t, 0, 0);
		setConstraints(d, 0, 1);
		setConstraints(title, 1, 0);
		setConstraints(date, 1, 1);
		
		task.getChildren().addAll(t, d, title, date);
		taskList.getChildren().add(task);
	}
	
	
	
	
}
