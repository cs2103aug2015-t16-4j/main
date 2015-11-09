package listItLogic;

import java.util.ArrayList;
import fileModifier.FileModifier;
import listItUI.FeedbackPane;
import taskGenerator.Task;

/**
 * This class contains methods to search for the user input keyword, to find that keyword
 * in our task list. Keyword can be in the form of importance variable, date variable or 
 * event title. 
 * @author Shrestha
 * @version 0.5
 */
public class SearchLogic {
	private static final String SEARCH_DATE = "date";
	private static final String SEARCH_DEFAULT = "default";
	private static final String SEARCH_IMPT = "impt";
	private static final String SEARCH_ALPHA = "alpha";
	private static final int IMPORTANCE_LEVEL_ONE = 1;
	private static final int IMPORTANCE_LEVEL_TWO = 2;
	private static final int IMPORTANCE_LEVEL_THREE = 3;
	private static final String INVALID_IMPT = "Invalid Importance level, " + "there are only 3 types: "
			+ "1 , 2 or 3.\n";
	private static final String NO_SEARCH = "No content to display.\n";
	private static final String SEARCH_IMPORTANCE_VALID = "Search by importance level" + " works.\n";
	private static final String SEARCH_DEFAULT_VALID = "Default search level works.\n";
	private static final String SEARCH_ALPHA_VALID = "Alpha search level works.\n";
	private static ArrayList<Task> taskList = new ArrayList<Task>();
	private static String message = "null";

	/**
	 * This method determines the type of search keyword the user has input, and
	 * checks if the user has input it in the correct format. After which, it sorts
	 * and displays the searched task list to the user. 
	 * @param command String command input by the user with a "search" keyword.
	 */
	public static void searchKeyWord(String command) {
		FileModifier modifier = FileModifier.getInstance();

		String keyword = command.substring(7);

		if (isSearchByDate(keyword)) {
			keyword = getKeyword(keyword);
			taskList = modifier.searchByDate(keyword);
			if (isTaskListEmpty(taskList)) {
				message = NO_SEARCH;
				LoggingLogic.logging(message);
				FeedbackPane.displayMessage(message);
			} else {
				modifier.setViewMode(SEARCH_DEFAULT);
				sortAndDisplaySearchList(modifier, taskList);
				message = SEARCH_DEFAULT_VALID;
				LoggingLogic.logging(message);
				FeedbackPane.displayMessage(message);
			}

		} else if (isSearchByImportance(keyword)) {
			keyword = getKeyword(keyword);
			int imptLevel = getImportanceLevel(keyword);

			if (isVeryImportant(imptLevel) || isImportant(imptLevel) || isNotImportant(imptLevel)) {
				taskList = modifier.searchByImportance(imptLevel);
				modifier.setViewMode(SEARCH_IMPT);
				sortAndDisplaySearchList(modifier, taskList);
				message = SEARCH_IMPORTANCE_VALID;
				LoggingLogic.logging(message);
				FeedbackPane.displayMessage(message);
			} else {
				FeedbackPane.displayInvalidIndexImptLevel();
				message = INVALID_IMPT;
				taskList.clear();
				LoggingLogic.logging(message);
			}
		} else {
			taskList = modifier.searchKeyword(keyword);
			if (isTaskListEmpty(taskList)) {
				message = NO_SEARCH;
				LoggingLogic.logging(message);
				FeedbackPane.displayMessage(message);
			} else {
				modifier.setViewMode(SEARCH_ALPHA);
				sortAndDisplaySearchList(modifier, taskList);
				message = SEARCH_ALPHA_VALID;
				LoggingLogic.logging(message);
				FeedbackPane.displayMessage(message);
			}
		}
	}

	/**
	 * Checks if the importance level is rank 3
	 * @param imptLevel importance level
	 * @return true if importance level is rank 3
	 */
	private static boolean isNotImportant(int imptLevel) {
		return imptLevel == IMPORTANCE_LEVEL_THREE;
	}

	/**
	 * Checks if the importance level is rank 2
	 * @param imptLevel importance level
	 * @return true if importance level is rank 2
	 */
	private static boolean isImportant(int imptLevel) {
		return imptLevel == IMPORTANCE_LEVEL_TWO;
	}

	/**
	 * Checks if the importance level is rank 1
	 * @param imptLevel importance level
	 * @return true if importance level is rank 1
	 */
	private static boolean isVeryImportant(int imptLevel) {
		return imptLevel == IMPORTANCE_LEVEL_ONE;
	}

	/**
	 * Checks if the keyword in the command is "impt"
	 * @param keyword
	 * @return true if the keyword is "impt"
	 */
	private static boolean isSearchByImportance(String keyword) {
		return keyword.indexOf(SEARCH_IMPT) == 0;
	}

	/**
	 * Checks if the keyword in the command is "date"
	 * @param keyword
	 * @return true if the keyword is "date"
	 */
	private static boolean isSearchByDate(String keyword) {
		return keyword.indexOf(SEARCH_DATE) == 0;
	}

	/**
	 * Checks if the search task list is an empty task list or not.
	 * @param taskList2 the task list that has the search keyword.
	 * @return true if task list is empty, else returns false.
	 */
	private static boolean isTaskListEmpty(ArrayList<Task> taskList2) {
		if (taskList.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Sorts and displays the search task list.
	 * @param modifier the modifier variable in our FileModifier class
	 * @param taskList task list that has the search keyword
	 */
	private static void sortAndDisplaySearchList(FileModifier modifier, ArrayList<Task> taskList) {
		modifier.sort(taskList);
		modifier.display(taskList);
	}

	private static String getKeyword(String keyword) {
		return keyword.substring(5);
	}

	private static int getImportanceLevel(String keyword) {
		return Integer.parseInt(keyword);
	}

	public static ArrayList<Task> getTaskList() {
		return taskList;
	}

	public static String getMessage() {
		return message;
	}
}
