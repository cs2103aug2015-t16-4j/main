package taskGenerator;

import java.util.Comparator;

/**
 * This class updates the collection.sort method in order to compare and
* sort tasks by the default algorithm of sorting our tasks, which is by comparing 
* the date of the tasks. The earlier the date, it will be sorted first. 
* Result = 1 if the 1st task has an earlier date
* Result = 0 if both tasks have the same date
* Result = -1 if the 2nd task has an earlier date
* @author Shawn
* @version 0.5
*/
public class TaskComparatorDefault implements Comparator<Task> {

	/**
	 * This method compares 2 tasks by date. If both tasks do not have a date object,
	 * or if both tasks have the same date, another method to compare by title is called. 
	 * @param task1 The first task to be compared
	 * @param task2 the second task to be compared
	 * @return result the value of 0 (equals to), -1 (less than) or 1 (greater than) 
	 */
	@Override
	public int compare(Task task1, Task task2) {
		int result = 0;
		
		if (task1.getEndDate() != null && task2.getEndDate() == null) {
			return -1;
		} else if (task1.getEndDate() == null && task2.getEndDate() != null) {
			return 1;
		} else if (task1.getEndDate() == null && task2.getEndDate() == null) {
			return 0;
		} else {
			result = task1.getDateInDate().compareTo(task2.getDateInDate());
			
			if (isResultZero(result)) {
				result = task1.getTitle().compareTo(task2.getTitle());
			}
		}
		return result;
	}


	/**
	 * This method checks if the compared result of the date objects of the
	 * 2 tasks = 0, meaning that the dates are of the same value.
	 * @param result compared date result
	 * @return result to be 0 (equals to)
	 */
	private boolean isResultZero(int result) {
		return result == 0;
	}
}
