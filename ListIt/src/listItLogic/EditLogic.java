package listItLogic;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;

public class EditLogic {

	private static FileModifier modifier;

	public EditLogic() {
		FileModifier modifier = FileModifier.getInstance();
	}

	public static void editEvent(String command) {
		int IndexToBeEdit = Integer.parseInt(command.substring(5, 6));

		if (command.contains("by date")) {
			String newDate = command.substring(command.indexOf("by date") + 8);

			if (AddLogic.checkValidDate(newDate)) {
				modifier.editDate(IndexToBeEdit - 1, newDate);
			}

			else {
				FeedbackPane.displayInvalidDate();
			}
		} else if (command.contains("by title")) {
			String newTitle = command.substring(command.indexOf("by title") + 9);

			modifier.editTitle(IndexToBeEdit - 1, newTitle);
		} else if (command.contains("by importance")) {
			String newImportance = command.substring(command.indexOf("rank") + 5);

			modifier.editTitle(IndexToBeEdit - 1, newImportance);
		} else if (command.contains("by time")) {
			String newStartTime = command.substring(command.indexOf("from") + 5, command.indexOf("to") - 1);
			String newEndTime = command.substring(command.indexOf("to") + 3);

			modifier.editTime(IndexToBeEdit - 1, newStartTime, newEndTime);
		}
	}
}
