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
		for (int i = 0; i < taskList.size(); i++) {
			tempTask = taskList.get(i);
			if (!isEndDateNull(tempTask)) {
				if (isOverDate(tempTask, systemTime)) {
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

	private static boolean isOverDate(Task tempTask, Date systemTime) {
		return systemTime.compareTo(tempTask.getEndDateInDateType()) > 0;
	}

	private static boolean isEndDateNull(Task tempTask) {
		return tempTask.getEndDate() == null;
	}
	
	public static boolean blockedDateCheck(Task taskForCheck) {
		boolean result = true;
		if (isEndDateNull(taskForCheck) && !isStartDateNull(taskForCheck)) {
			ArrayList<Task> taskList = modifier.getContentList();
			Task tempTask = new Task();
			for (int i = 0; i < taskList.size(); i++) {
				tempTask = taskList.get(i);
				if (tempTask.isBlocking()) {
					if (isBlockDatesValid(taskForCheck, tempTask)) {
						result = false;
						break;
					}
				}
			}
		}
		return result;
	}

	private static boolean isBlockDatesValid(Task taskForCheck, Task tempTask) {
		return taskForCheck.getEndDateInDateType().compareTo(tempTask.getEndDateInDateType()) <= 0 
			   && taskForCheck.getEndDateInDateType().compareTo(tempTask.getStartDateInDateType()) >= 0;
	}

	private static boolean isStartDateNull(Task taskForCheck) {
		return taskForCheck.getStartDate() == null;
	}
}
