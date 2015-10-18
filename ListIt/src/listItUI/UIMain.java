package listItUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class UIMain extends Application{
	
	InputTextPane inputBox;
	OutputScreenPane screenBox;
	FeedbackPane FeedbackBox;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("ListIt");
		
		inputBox = new InputTextPane();
		screenBox = new OutputScreenPane();
		FeedbackBox = new FeedbackPane();
		
		
		BorderPane layout = new BorderPane();
		
		layout.setTop(screenBox);
		layout.setCenter(FeedbackBox);
		layout.setBottom(inputBox);
		
		Scene scene = new Scene(layout, 520, 600);
		scene.getStylesheets().add("listItUI/ListItUITheme.css");
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
