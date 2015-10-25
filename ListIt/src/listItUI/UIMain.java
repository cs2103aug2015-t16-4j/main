package listItUI;

import fileModifier.FileModifier;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class UIMain extends Application{
	
	InputTextPane inputBox;
	OutputScreenPane screenBox;
	FeedbackPane FeedbackBox;
	TopBar topBar;
	static FileModifier modifier = FileModifier.getInstance();

	public static void main(String[] args) {
		launch(args);
		modifier.display(modifier.getContentList());
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("ListIt");
		
		inputBox = new InputTextPane();
		screenBox = new OutputScreenPane();
		FeedbackBox = new FeedbackPane();
		topBar = new TopBar();
		
		
		BorderPane layout = new BorderPane();
		
		layout.setTop(topBar);
		layout.setLeft(screenBox);
		layout.setCenter(FeedbackBox);
		layout.setBottom(inputBox);
		
		Scene scene = new Scene(layout, 630, 600);
		scene.getStylesheets().add("listItUI/ListItUITheme.css");
		
		primaryStage.setScene(scene);
		primaryStage.show();
		modifier.display(modifier.getContentList());
	}
}
