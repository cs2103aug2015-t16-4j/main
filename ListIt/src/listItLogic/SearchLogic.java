package listItLogic;

import java.util.ArrayList;
import java.util.Collections;

import fileModifier.FileModifier;
import taskGenerator.Task;
import taskGenerator.TaskComparatorAlpha;
import taskGenerator.TaskComparatorDefault;
import taskGenerator.TaskComparatorImpt;

public class SearchLogic {
	
	public static void searchKeyWord(String command) {
		FileModifier modifier = FileModifier.getInstance();
		ArrayList<Task> taskList = new ArrayList<Task>();
		
		String keyword = command.substring(7);
		
		if (keyword.indexOf("date") == 0) {
			keyword = keyword.substring(5);
			taskList = modifier.searchByDate(keyword);
			
			modifier.setViewMode("default");
			modifier.sort(taskList);
			
			modifier.display(taskList);
			
		} 
		else if (keyword.indexOf("impt") == 0) {
			keyword = keyword.substring(5);
			int imptLevel = Integer.parseInt(keyword);
			taskList = modifier.searchByImportance(imptLevel);
			
			modifier.setViewMode("impt");
			modifier.sort(taskList);
			
			modifier.display(taskList);
		} 
		else {
			taskList = modifier.searchKeyword(keyword);
			
			modifier.setViewMode("alpha");
			modifier.sort(taskList);

			modifier.display(taskList);
		}
	}
	
}
