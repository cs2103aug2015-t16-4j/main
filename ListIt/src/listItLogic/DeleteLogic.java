package listItLogic;

import fileModifier.FileModifier;

public class DeleteLogic {
	
	private static FileModifier modifier = FileModifier.getInstance();

	public static void deleteEvent(String command) throws InvalidCommandException {
		int taskIndexToBeDelete = convertStringIndexToInt(command);
		int sizeOfFile = modifier.getContentList().size();
		if((taskIndexToBeDelete-1) < sizeOfFile) {
			modifier.removeTask(taskIndexToBeDelete - 1);
		} else {
			throw new InvalidCommandException("Index is out of bounds");
		}
	}

	private static int convertStringIndexToInt(String command) {
		return Integer.parseInt(command.substring(7));
	}

	public static void clearFile() {
		modifier.clearAll();
	}

}
