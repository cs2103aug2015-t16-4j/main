package listItLogic;

import java.util.ArrayList;
import java.util.Date;

import fileModifier.FileModifier;
import taskGenerator.Task;

public class TaskCheckLogic {
	static FileModifier modifier = FileModifier.getInstance();
	
	public TaskCheckLogic() {
		
	}
	
	public static void overDateCheck(ArrayList<Task> taskList) {
		Task tempTask = new Task();
		Date systemTime = new Date();
		for(int i = 0; i < taskList.size(); i++) {
			tempTask = taskList.get(i);
			if(tempTask.getEndDate() != null) {
				if(systemTime.compareTo(tempTask.getEndDateInDateType()) > 0) {
					tempTask.setOverDate();
					taskList.set(i, tempTask);
				} else {
					tempTask.setNotOverDate();
					taskList.set(i, tempTask);
				}
			} else {
				break;
			}
		}
		
		modifier.saveFile(taskList);
	}
}
