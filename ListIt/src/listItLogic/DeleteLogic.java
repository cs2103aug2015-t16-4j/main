package listItLogic;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;

public class DeleteLogic {
	
	private static FileModifier modifier = FileModifier.getInstance();
	private static String message = null;
	private static final String MESSAGE_OUT_OF_BOUNDS = "Index is out of bounds.\n";
	private static final String DELETE_VALID = "The task has been successfully"
			                                    + " deleted.\n";
	private static final String CLEAR_VALID = "The file has been successfully been"
			                                   + " cleared.\n";


	public static void deleteEvent(String command) {
		int taskIndexToBeDelete = getDeleteIndex(command);
		int sizeOfFile = 0;
		if (modifier.isViewModeComplete()) {
			sizeOfFile  = modifier.getCompleteContentList().size();
		} else {
			sizeOfFile = modifier.getContentList().size();
		}
		
		if (isValidIndex(taskIndexToBeDelete, sizeOfFile)) {
			modifier.removeTask(taskIndexToBeDelete - 1);  
			message =DELETE_VALID; 
			LoggingLogic.logging(message);
		} else {
			FeedbackPane.displayInvalidIndexToDelete();
			message = MESSAGE_OUT_OF_BOUNDS;
			LoggingLogic.logging(message); 
		}
	}

	private static boolean isValidIndex(int taskIndexToBeDelete, int sizeOfFile) {
		return (taskIndexToBeDelete-1) < sizeOfFile && taskIndexToBeDelete-1 >= 0;
	}

	private static int getDeleteIndex(String command) {
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
