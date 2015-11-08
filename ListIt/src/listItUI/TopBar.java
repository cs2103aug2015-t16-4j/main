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
