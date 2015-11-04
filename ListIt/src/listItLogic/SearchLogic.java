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
	private static final String invalidImpt ="Invalid Importance level , there are only 3 types : 1 ,2 3.\n"; 
	private static final String NO_SEARCH ="No content to display"; 

	private static ArrayList<Task> taskList = new ArrayList<Task>();
	private static String message = "null"; 

	public static void searchKeyWord(String command) {
		FileModifier modifier = FileModifier.getInstance();
		
		String keyword = command.substring(7);
		
		if (keyword.indexOf(SEARCH_DATE) == 0) {
			keyword = getKeyword(keyword);
			taskList = modifier.searchByDate(keyword);
			if(taskList.isEmpty()){
				message=NO_SEARCH; 
			}
			modifier.setViewMode(SEARCH_DEFAULT);
			sortAndDisplaySearchList(modifier, taskList);
			
		} else if (keyword.indexOf(SEARCH_IMPT) == 0) {
			keyword = getKeyword(keyword);
			int imptLevel = convertStringImptLevelToInt(keyword);
			
			if(imptLevel == 1 || imptLevel == 2 || imptLevel==3){
				taskList = modifier.searchByImportance(imptLevel);
				modifier.setViewMode(SEARCH_IMPT);
				sortAndDisplaySearchList(modifier, taskList);
			}else { 
				FeedbackPane.displayInvalidIndexToSearch();
				message = invalidImpt; 
			}
		}else {
			taskList = modifier.searchKeyword(keyword);
			if(taskList.isEmpty()){
				message=NO_SEARCH; 
			}
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
	
	public static ArrayList<Task> getTaskList() {
		return taskList;
	}
	
	public static String getMessage() {
		return message;
	}
}
