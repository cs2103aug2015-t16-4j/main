package listItLogic;

import fileModifier.FileModifier;

public class DeleteLogic {
	
	private static FileModifier modifier = FileModifier.getInstance();

	public static void deleteEvent(String command) {
		int taskIndexToBeDelete = convertStringIndexToInt(command);
		modifier.removeTask(taskIndexToBeDelete - 1);	
	}

	private static int convertStringIndexToInt(String command) {
		return Integer.parseInt(command.substring(7));
	}

	public static void clearFile() {
		modifier.clearAll();
	}

}
