package listItLogic;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;

public class DeleteLogic {
	
	private static FileModifier modifier = FileModifier.getInstance();
	private static String message = null;

	public static void deleteEvent(String command) {
		int taskIndexToBeDelete = convertStringIndexToInt(command);
		int sizeOfFile = modifier.getContentList().size();
		if((taskIndexToBeDelete-1) < sizeOfFile && taskIndexToBeDelete-1 >= 0) {
			modifier.removeTask(taskIndexToBeDelete - 1);
		} else {
			FeedbackPane.displayInvalidIndexToDelete();
			message = "Index is out of bounds";
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
	}

}
