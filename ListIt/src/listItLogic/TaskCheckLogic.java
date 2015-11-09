// @@author Shi Hao A0129916W
package listItLogic;

import java.util.ArrayList;
import java.util.Date;

import fileModifier.FileModifier;
import taskGenerator.Task;

/**
 * This class contains all the methods that check if the command date input
 * entered by the user is of a valid date input, and also compares the task date
 * to the actual calendar date.
 * @version 0.5
 */
public class TaskCheckLogic {
	static FileModifier modifier = FileModifier.getInstance();

	public TaskCheckLogic() {

	}

	/**
	 * Checks if the date variable of the task is over the actual calendar date.
	 * @param taskList selected task list
	 */
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

	/**
	 * Checks the block tasks in the list, to see if there is a time line overlap
	 * between the newTask and blockingTask
	 * 
	 * @param taskForCheck
	 *            task in a block input
	 * @return true if above holds, else returns false.
	 */
	public static boolean blockedDateCheck(Task taskForCheck) {
		boolean result = true;
		if (!isEndDateNull(taskForCheck) && !isStartDateNull(taskForCheck)) {
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
		if (taskForCheck.getStartDateInDateType().compareTo(tempTask.getStartDateInDateType()) == -1
				&& taskForCheck.getEndDateInDateType().compareTo(tempTask.getEndDateInDateType()) == 1) {
			return true;
		} else if (taskForCheck.getEndDateInDateType().compareTo(tempTask.getStartDateInDateType()) == 1
				&& taskForCheck.getEndDateInDateType().compareTo(tempTask.getEndDateInDateType()) == -1) {
			return true;
		} else if (taskForCheck.getStartDateInDateType().compareTo(tempTask.getStartDateInDateType()) == 1 
				&& taskForCheck.getStartDateInDateType().compareTo(tempTask.getEndDateInDateType()) == -1) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isStartDateNull(Task taskForCheck) {
		return taskForCheck.getStartDate() == null;
	}
}
