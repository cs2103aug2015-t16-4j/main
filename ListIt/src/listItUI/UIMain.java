package listItUI;

import fileModifier.FileModifier;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UIMain extends Application {
	
	InputTextPane inputBox;
	OutputScreenPane screenBox;
	FeedbackPane FeedbackBox;
	TopBar topBar;
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
		
		inputBox = new InputTextPane();
		screenBox = new OutputScreenPane();
		FeedbackBox = new FeedbackPane();
		topBar = new TopBar();
		
		
		BorderPane layout = new BorderPane();
		
		layout.setTop(topBar);
		layout.setLeft(screenBox);
		layout.setCenter(FeedbackBox);
		layout.setBottom(inputBox);
		
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
