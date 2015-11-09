// @@author Shawn A0124181R
package listItLogic;

import java.util.ArrayList;
import fileModifier.FileModifier;
import taskGenerator.Task;
import taskGenerator.TaskComparatorAlpha;
import taskGenerator.TaskComparatorDefault;
import taskGenerator.TaskComparatorImpt;

import java.util.Collections;

/**
 * This class contains methods which displays the output(created tasks) as a
 * task list to the user. The task lists will be sorted according to how the
 * user wants the tasks to be displayed
 * @version 0.5
 */
public class DisplayLogic {

	private static final String DISPLAY_ALPHA = "display alpha";
	private static final String DISPLAY_IMPT = "display impt";
	private static final Object DISPLAY_COMPLETE = "display complete";
	private static final String COMMAND_ALPHA = "alpha";
	private static final String COMMAND_IMPT = "impt";
	private static final String COMMAND_DEFAULT = "default";
	private static final String COMMAND_COMPLETE = "complete";
	private static final String MESSAGE_SUCCESS_ALPHA = "succesfully sorted alphabetically";
	private static final String MESSAGE_SUCCESS_DATE = "succesfully sorted by date";
	private static final String MESSAGE_SUCCESS_IMPT = "succesfully sorted by importance level";
	
	private static ArrayList<Task> list = new ArrayList<Task>();
	static FileModifier modifier = FileModifier.getInstance();

	/**
	 * This method finds out the display mode which the user wants the tasks to be 
	 * displayed by, then displays it to the user.
	 * @param command display mode keyword (impt, alpha, complete, default)
	 */
	public static void determineDisplayMode(String command) {
		if (isDisplayAlphabetically(command)) {
			displayByAlpha();
		} else if (isDisplayByImportance(command)) {
			displayByImportance();
		} else if (isDisplayByComplete(command)) {
			displayByComplete();
		} else {
			defaultDisplay();
		}
	}

	/**
	 * Checks if the command entered has the keyword "impt"
	 * @param command String command entered by the user with the keyword "display"
	 * @return true if command contains "impt", else returns false.
	 */
	private static boolean isDisplayByImportance(String command) {
		return command.equals(DISPLAY_IMPT);
	}

	/**
	 * Checks if the command entered has the keyword "alpha"
	 * @param command String command entered by the user with the keyword "display"
	 * @return true if command contains "alpha", else returns false.
	 */
	private static boolean isDisplayAlphabetically(String command) {
		return command.equals(DISPLAY_ALPHA);
	}
	
	/**
	 * Checks if the command entered has the keyword "complete"
	 * @param command String command entered by the user with the keyword "display"
	 * @return true if command contains "complete", else returns false.
	 */
	private static boolean isDisplayByComplete(String command) {
		return command.equals(DISPLAY_COMPLETE);
	}

	/**
	 * Gets the task list, and sorts the list alphabetically.
	 */
	private static void displayByAlpha() {
		list = modifier.getContentList();
		assert list != null;
		modifier.setViewMode(COMMAND_ALPHA);
		updateFile();
		LoggingLogic.logging(MESSAGE_SUCCESS_ALPHA);
	}

	/**
	 * Gets the task list, and sorts the list by importance.
	 */
	private static void displayByImportance() {
		list = modifier.getContentList();
		assert list != null;
		modifier.setViewMode(COMMAND_IMPT);
		updateFile();
		LoggingLogic.logging(MESSAGE_SUCCESS_IMPT);
	}
	
	/**
	 * Gets the completed task list and displays it as it is.
	 * Tasks are sorted on what is completed first
	 */
	private static void displayByComplete() {
		list = modifier.getCompleteContentList();
		assert list != null;
		modifier.setViewMode(COMMAND_COMPLETE);
		updateFileComplete();
	}

	/**
	 * Gets the task list and displays it by our default sorting.
	 */
	public static void defaultDisplay() {
		list = modifier.getContentList();
		assert list != null;
		modifier.setViewMode(COMMAND_DEFAULT);
		updateFile();
		LoggingLogic.logging(MESSAGE_SUCCESS_DATE);
	}
	
	/**
	 * Updates the data file of task list so that the task list is sorted 
	 * and then saved. After which, it is displayed to the user.
	 */
	private static void updateFile() {
		modifier.sort(list);
		modifier.updateIndex(list);
		TaskCheckLogic.overDateCheck(list);
		modifier.saveFile(list);
		modifier.display(list);
	}

	/**
	 * Updates the completed data file of task lists so that the list is
	 * saved. After which, it is displayed to the user.
	 */
	private static void updateFileComplete() {
		modifier.updateIndex(list);
		modifier.saveCompleteFile(list);
		modifier.display(list);
	}
}
