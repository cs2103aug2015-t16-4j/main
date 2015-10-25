package listItUI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;

public class TopBar extends GridPane{
	
	private Text listItLabel;
	private Button closeButton;
	private Button minimizeButton;
	
	public TopBar() {
		
		listItLabel = new Text("List It");
		listItLabel.setFont(Font.font("Tonto", FontPosture.ITALIC, 30));
		listItLabel.setStyle("-fx-fill: linear-gradient(#0000FF 10%, #FFFFFF 30%, #0000FF 50%, #FFFFFF 70%, #0000FF 90%);"
				+ "-fx-stroke: black;");
		
		closeButton = new Button("Close");
		minimizeButton = new Button("Hide");
		
		setConstraints(listItLabel, 0, 0);
		setConstraints(closeButton, 1, 0);
		setConstraints(minimizeButton, 2, 0);
		
		getChildren().addAll(listItLabel, closeButton, minimizeButton);
	}
}