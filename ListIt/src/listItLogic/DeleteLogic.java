package listItLogic;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;

public class DeleteLogic {
	
	private static FileModifier modifier = FileModifier.getInstance();
	private static String message = null;
	private static final String MESSAGE_OUT_OF_BOUNDS = "Index is out of bounds";
	private static final String DELETE_VALID = "The task has been successfully deleted";
	private static final String CLEAR_VALID = "The file has been successfully been cleared";


	public static void deleteEvent(String command) {
		int taskIndexToBeDelete = convertStringIndexToInt(command);
		int sizeOfFile = 0;
		if(modifier.isViewModeComplete()) {
			sizeOfFile  = modifier.getCompleteContentList().size();
		} else {
			sizeOfFile = modifier.getContentList().size();
		}
		if((taskIndexToBeDelete-1) < sizeOfFile && taskIndexToBeDelete-1 >= 0) {
			modifier.removeTask(taskIndexToBeDelete - 1);  
			message =DELETE_VALID; 
			LoggingLogic.logging(message);
		} else {
			FeedbackPane.displayInvalidIndexToDelete();
			message = MESSAGE_OUT_OF_BOUNDS;
			LoggingLogic.logging(message); 
		}
	}

	private static int convertStringIndexToInt(String command) {
		return Integer.parseInt(command.substring(7));
	}
	
	public static String getMessage() {
		return message;
	}

	public static void clearFile() {
		modifier.clearAll();
		message = CLEAR_VALID; 
		LoggingLogic.logging(message);
	}

}
