// @@author Shi Hao A0129916W

package listItUI;

import java.util.ArrayList;

/**
 * This class helps form the user interface of ListIt. 
 * it contains the basic settings for creating the entire interface of our application. 
 * It involves the creation of the different panels. 
 * 
 * @version 0.5
 */

import fileModifier.FileModifier;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
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
		
		componentLayoutSetup(layout);
		
		windowDragSetup(primaryStage, layout);
		
		Scene scene = new Scene(layout, 630, 600);
		scene.getStylesheets().add("listItUI/ListItUITheme.css");
		
		OutputScreenPane.getScreen().setFocusTraversable(true);
		inputBox.getTextField().requestFocus();
		
		primaryStage.setScene(scene);
		primaryStage.show();
		modifier.display(modifier.getContentList());
	}
    /**
     * this allows the ListIt window to be dragged around.  
     * @param primaryStage
     * @param layout
     */
	private void windowDragSetup(Stage primaryStage, GridPane layout) {
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
	}

	private void componentLayoutSetup(GridPane layout) {
		GridPane.setConstraints(topBar, 0, 0);
		GridPane.setConstraints(screenBox, 0, 1);
		GridPane.setConstraints(feedbackBox, 0, 2);
		GridPane.setConstraints(inputBox, 0, 3);
		
		layout.getChildren().addAll(topBar, screenBox, feedbackBox, inputBox);
	}
	
	
	/**
	 * this helps construct the help window and pops it up on the screen 
	 * @param commands
	 * @param methods
	 */
	public static void popUpHelp(ArrayList<String> commands, ArrayList<String> methods) {
		Stage helpStage = new Stage();
		int i=0, j=0;
		
		assert commands != null;
		assert methods != null;
		
		helpStage.setTitle("Cheat Sheet");
		
		ScrollPane helpLayout = new ScrollPane();
		
		GridPane helpSheet = new GridPane();
		
		ColumnConstraints col0Constraints = new ColumnConstraints();
		col0Constraints.setPercentWidth(40);
		ColumnConstraints col1Constraints = new ColumnConstraints();
		col1Constraints.setPercentWidth(60);
		
		helpSheet.getColumnConstraints().addAll(col0Constraints, col1Constraints);
		
		for(i=0; i<commands.size(); i++) {
			Text command = new Text(commands.get(i));
			Text method = new Text(methods.get(j));
			
			GridPane.setConstraints(command, 0, i);
			GridPane.setConstraints(method, 1, j);
			
			helpSheet.getChildren().addAll(command, method);
			j++;
		}
		
		helpLayout.setContent(helpSheet);
		
		Scene helpScene = new Scene(helpLayout, 1000, 600);
		helpStage.setScene(helpScene);
		helpStage.show();
	}
}
