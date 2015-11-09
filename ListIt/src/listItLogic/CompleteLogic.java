package listItLogic;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;

/**
 * This class contains methods to move tasks from the usual data file, 
 * into the completed data file, when the user enters "complete". 
 * @author Shrestha
 *
 */
public class CompleteLogic {
	
	private static FileModifier modifier = FileModifier.getInstance();
	private static String message = null;
	private static final String MESSAGE_OUT_OF_BOUNDS = "Index is out of bounds";
	private static final String COMPLETE_SUCCESSFUL = "Task is completed";

	/**
	 * This method gets the task by using the line index, and then moves the task
	 * over to the completed task list. 
	 * @param command string command input by the user with a "complete" keyword.
	 */
	public static void completeEvent(String command) {
		int taskIndexComplete = getCompleteIndex(command);
		int sizeOfFile = getSizeOfFile();
		if(isValidIndex(taskIndexComplete, sizeOfFile)) {
			modifier.completeTask(taskIndexComplete - 1);
			message = COMPLETE_SUCCESSFUL;
			LoggingLogic.logging(message);
			FeedbackPane.displayMessage(message);
			
		} else {
			FeedbackPane.displayInvalidIndexComplete();
			message = MESSAGE_OUT_OF_BOUNDS;
			LoggingLogic.logging(message);
		}
	}

	/**
	 * Checks if the index entered is valid (in the range of the task list)
	 * @param taskIndexComplete completed task list index
	 * @param sizeOfFile size of the task list to get the index range
	 * @return true if index is valid, else returns false
	 */
	private static boolean isValidIndex(int taskIndexComplete, int sizeOfFile) {
		return (taskIndexComplete-1) < sizeOfFile && taskIndexComplete-1 >= 0;
	}

	private static int getSizeOfFile() {
		return modifier.getContentList().size();
	}

	private static int getCompleteIndex(String command) {
		return Integer.parseInt(command.substring(9));
	}
	
	public static String getMessage() {
		return message;
	}
}
