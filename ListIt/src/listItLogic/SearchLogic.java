package listItLogic;

import java.util.ArrayList;
import fileModifier.FileModifier;
import taskGenerator.Task;

public class SearchLogic {
	private static final String SEARCH_DATE = "date";
	private static final String SEARCH_DEFAULT = "default";
	private static final String SEARCH_IMPT = "impt";
	private static final String SEARCH_ALPHA = "alpha";
	
	public static void searchKeyWord(String command) {
		FileModifier modifier = FileModifier.getInstance();
		ArrayList<Task> taskList = new ArrayList<Task>();
		
		String keyword = command.substring(7);
		
		if (keyword.indexOf(SEARCH_DATE) == 0) {
			keyword = getKeyword(keyword);
			taskList = modifier.searchByDate(keyword);
			
			modifier.setViewMode(SEARCH_DEFAULT);
			sortAndDisplaySearchList(modifier, taskList);
			
		} else if (keyword.indexOf(SEARCH_IMPT) == 0) {
			keyword = getKeyword(keyword);
			int imptLevel = convertStringImptLevelToInt(keyword);
			taskList = modifier.searchByImportance(imptLevel);
			
			modifier.setViewMode(SEARCH_IMPT);
			sortAndDisplaySearchList(modifier, taskList);
		} else {
			taskList = modifier.searchKeyword(keyword);
			
			modifier.setViewMode(SEARCH_ALPHA);
			sortAndDisplaySearchList(modifier, taskList);
		}
	}

	private static void sortAndDisplaySearchList(FileModifier modifier, ArrayList<Task> taskList) {
		modifier.sort(taskList);
		modifier.display(taskList);
	}

	private static String getKeyword(String keyword) {
		return keyword.substring(5);
	}

	private static int convertStringImptLevelToInt(String keyword) {
		return Integer.parseInt(keyword);
	}
}
