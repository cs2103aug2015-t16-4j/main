package listItLogic;

import java.util.ArrayList;
import fileModifier.FileModifier;
import listItUI.FeedbackPane;
import taskGenerator.Task;

public class SearchLogic {
	private static final String SEARCH_DATE = "date";
	private static final String SEARCH_DEFAULT = "default";
	private static final String SEARCH_IMPT = "impt";
	private static final String SEARCH_ALPHA = "alpha";
	private static final int RANK_ONE = 1;
	private static final int RANK_TWO = 2;
	private static final int RANK_THREE = 3;
	private static final String INVALID_IMPT ="Invalid Importance level, "
			                                   + "there are only 3 types: "
			                                   + "1 , 2 or 3.\n"; 
	private static final String NO_SEARCH ="No content to display.\n"; 
	private static final String SEARCH_IMPORTANCE_VALID="Search by importance level"
			                                             + " works.\n"; 
	private static final String SEARCH_DEFAULT_VALID = "Default search level works.\n"; 
	private static final String SEARCH_ALPHA_VALID = "Alpha search level works.\n"; 
	private static ArrayList<Task> taskList = new ArrayList<Task>();
	private static String message = "null"; 

	public static void searchKeyWord(String command) {
		FileModifier modifier = FileModifier.getInstance();
		
		String keyword = command.substring(7);
		
		if (isSearchByDate(keyword)) {
			keyword = getKeyword(keyword);
			taskList = modifier.searchByDate(keyword);
			if(isTaskListEmpty(taskList)){
				message = NO_SEARCH; 
				LoggingLogic.logging(message);
			}
			modifier.setViewMode(SEARCH_DEFAULT);
			sortAndDisplaySearchList(modifier, taskList);
			message = SEARCH_DEFAULT_VALID; 
			LoggingLogic.logging(message); 
			
		} else if (isSearchByImportance(keyword)) {
			keyword = getKeyword(keyword);
			int imptLevel = getImportanceLevel(keyword);
			
			if(isVeryImportant(imptLevel) || isImportant(imptLevel) 
			   || isNotImportant(imptLevel)){
				taskList = modifier.searchByImportance(imptLevel);
				modifier.setViewMode(SEARCH_IMPT);
				sortAndDisplaySearchList(modifier, taskList);
				message = SEARCH_IMPORTANCE_VALID; 
				LoggingLogic.logging(message);
			}else { 
				FeedbackPane.displayInvalidIndexImptLevel();
				message = INVALID_IMPT; 
				LoggingLogic.logging(message);
			}
		}else {
			taskList = modifier.searchKeyword(keyword);
			if(isTaskListEmpty(taskList)){
				message = NO_SEARCH; 
				LoggingLogic.logging(message);
			}
			modifier.setViewMode(SEARCH_ALPHA);
			sortAndDisplaySearchList(modifier, taskList);
			message = SEARCH_ALPHA_VALID;
			LoggingLogic.logging(message);
		}
	}

	private static boolean isNotImportant(int imptLevel) {
		return imptLevel == RANK_THREE;
	}

	private static boolean isImportant(int imptLevel) {
		return imptLevel == RANK_TWO;
	}

	private static boolean isVeryImportant(int imptLevel) {
		return imptLevel == RANK_ONE;
	}

	private static boolean isSearchByImportance(String keyword) {
		return keyword.indexOf(SEARCH_IMPT) == 0;
	}

	private static boolean isSearchByDate(String keyword) {
		return keyword.indexOf(SEARCH_DATE) == 0;
	}

	private static boolean isTaskListEmpty(ArrayList<Task> taskList2) {
		if(taskList.isEmpty()) {
			return true; 
		}
		return false; 
	}

	private static void sortAndDisplaySearchList(FileModifier modifier,
			                                     ArrayList<Task> taskList) {
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
