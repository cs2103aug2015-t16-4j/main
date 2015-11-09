// @@author Shrestha A0130280X
package listItLogic;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;

/**
 * This class contains methods which deletes a particular task from the task list.
 * @version 0.5
 */
public class DeleteLogic {
	
	private static FileModifier modifier = FileModifier.getInstance();
	private static final String DELETE_VALID = "The task has been successfully"
                                                + " deleted.\n";

	/**
	 * This method finds a task from the list by getting the line index of the task,
	 * after which, it removes the task from the liat.
	 * @param command String command entered by the user with a "delete" keyword. 
	 */
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
			FeedbackPane.displayMessage(DELETE_VALID);
		} else {
			FeedbackPane.displayInvalidIndexToDelete();
		}
	}

	/**
	 * Checks if the index entered is valid (in the range of the task list)
	 * @param taskIndexToBeDelete line index of the task to be deleted
	 * @param sizeOfFile size of the task list to get the index range
	 * @return true if index is valid, else returns false
	 */
	private static boolean isValidIndex(int taskIndexToBeDelete, int sizeOfFile) {
		return (taskIndexToBeDelete-1) < sizeOfFile && taskIndexToBeDelete-1 >= 0;
	}

	private static int getDeleteIndex(String command) {
		return Integer.parseInt(command.substring(7));
	}

	/**
	 * This method clears the entire data file when "clear" is entered. It does not
	 * need the line index.
	 */
	public static void clearFile() {
		modifier.clearAll(); 
	}

}
