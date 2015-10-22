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
		
		String keyword = command.substring(15);
		
		ArrayList<Task> taskList = modifier.searchKeyword(keyword);
		
		Collections.sort(taskList, new TaskComparatorDefault());
		
		modifier.display(taskList);
	}
	
}
