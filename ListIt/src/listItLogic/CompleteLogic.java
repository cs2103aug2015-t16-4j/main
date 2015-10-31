package listItLogic;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;

public class CompleteLogic {
	
	private static FileModifier modifier = FileModifier.getInstance();
	private static String message = null;
	private static final String MESSAGE_OUT_OF_BOUNDS = "Index is out of bounds";
	

	public static void completeEvent(String command) {
		int taskIndexComplete = convertStringIndexToInt(command);
		int sizeOfFile = modifier.getContentList().size();
		if((taskIndexComplete-1) < sizeOfFile && taskIndexComplete-1 >= 0) {
			modifier.completeTask(taskIndexComplete - 1);
		} else {
			FeedbackPane.displayInvalidIndexComplete();
			message = MESSAGE_OUT_OF_BOUNDS;
		}
	}

	private static int convertStringIndexToInt(String command) {
		return Integer.parseInt(command.substring(9));
	}
	
	public static String getMessage() {
		return message;
	}

}
