package listItUI;

import fileModifier.FileModifier;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
		
		GridPane.setConstraints(topBar, 0, 0);
		GridPane.setConstraints(screenBox, 0, 1);
		GridPane.setConstraints(feedbackBox, 0, 2);
		GridPane.setConstraints(inputBox, 0, 3);
		
		layout.getChildren().addAll(topBar, screenBox, feedbackBox, inputBox);
		
		//Used to move undecorated window
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
		
		Scene scene = new Scene(layout, 630, 600);
		scene.getStylesheets().add("listItUI/ListItUITheme.css");
		
		primaryStage.setScene(scene);
		primaryStage.show();
		modifier.display(modifier.getContentList());
	}
}
