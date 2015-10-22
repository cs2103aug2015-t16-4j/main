package listItLogic;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import fileModifier.FileModifier;
import taskGenerator.Task;
import taskGenerator.TaskComparatorDefault;

public class SearchLogic {
	
	public static void searchKeyWord(String command) {
		FileModifier modifier = FileModifier.getInstance();
		ArrayList<Task> taskList = new ArrayList<Task>();
		
		String keyword = command.substring(7);
		
		if (keyword.indexOf("date") == 0) {
			keyword = keyword.substring(5);
			taskList = modifier.searchByDate(keyword);
		} else if (keyword.indexOf("impt") == 0) {
			keyword = keyword.substring(5);
			int imptLevel = Integer.parseInt(keyword);
			taskList = modifier.searchByImportance(imptLevel);
		} else {
			taskList = modifier.searchKeyword(keyword);
		}
		
		Collections.sort(taskList, new TaskComparatorDefault());
		modifier.display(taskList);
	}
	
}
