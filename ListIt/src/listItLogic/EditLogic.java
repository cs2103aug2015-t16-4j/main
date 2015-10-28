package listItLogic;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;

public class EditLogic {

	private static FileModifier modifier = FileModifier.getInstance();
	private static final String COMMAND_TITLE = "by title";
	private static final String COMMAND_IMPORTANCE = "by impt";
	private static final String COMMAND_DEADLINE = "by date";
	private static final String COMMAND_TIMELINE = "by time";
	private static final String COMMAND_TO = "to";
	private static final String COMMAND_FROM = "from";

	public static void editEvent(String command) {
		int IndexToBeEdit = convertStringIndexToInt(command);

		if (isEditByDate(command)) {
			String newDate = getNewDate(command);
			if (AddLogic.isValidDate(newDate)) {
				modifier.editEndDate(IndexToBeEdit-1, newDate);
			} else {
				FeedbackPane.displayInvalidDate();
			}
		} else if (isEditByTitle(command)) {
			String newTitle = getNewTitle(command);
			modifier.editTitle(IndexToBeEdit - 1, newTitle);
		} else if (isEditByImportance(command)) {
			String newImportance = getNewImportanceLevel(command);
			modifier.editImportance(IndexToBeEdit - 1, newImportance);
		} else if (isEditByTimeline(command)) {
			String newStartDate = getNewStartDate(command);
			String newEndDate = getNewEndDate(command);
			modifier.editTimeline(IndexToBeEdit - 1, newStartDate, newEndDate);
		} 
	}

	private static String getNewEndDate(String command) {
		return command.substring(command.indexOf(COMMAND_TO) + 3);
	}

	private static String getNewStartDate(String command) {
		return command.substring(command.indexOf(COMMAND_FROM) + 5, command.indexOf(COMMAND_TO) - 1);
	}

	private static String getNewImportanceLevel(String command) {
		return command.substring(command.indexOf(COMMAND_IMPORTANCE) + 8);
	}

	private static String getNewTitle(String command) {
		return command.substring(command.indexOf(COMMAND_TITLE) + 9);
	}

	private static String getNewDate(String command) {
		return command.substring(command.indexOf(COMMAND_DEADLINE) + 8);
	}

	private static boolean isEditByTimeline(String command) {
		return command.contains(COMMAND_TIMELINE);
	}

	private static boolean isEditByImportance(String command) {
		return command.contains(COMMAND_IMPORTANCE);
	}

	private static boolean isEditByTitle(String command) {
		return command.contains(COMMAND_TITLE);
	}

	private static boolean isEditByDate(String command) {
		return command.contains(COMMAND_DEADLINE);
	}

	private static int convertStringIndexToInt(String command) {
		return Integer.parseInt(command.substring(5, 6));
	}
}
