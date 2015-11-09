// @@author Shawn A0124181R
package taskGenerator;

import java.util.Comparator;

/**
 * This class updates the collection.sort method in order to compare and
* sort tasks by the default algorithm of sorting our tasks, which is by comparing 
* the date of the tasks. The earlier the date, it will be sorted first. 
* Result = 1 1st task should be arrange after 2nd task
* Result = 0 1st task is same in position as 2nd task
* Result = -1 1st task should be arrange before 2nd task
* @version 0.5
*/
public class TaskComparatorDefault implements Comparator<Task> {

	/**
	 * This method compares 2 tasks by date. If both tasks do not have a date object,
	 * or if both tasks have the same date, then they are compare base on alphabetical
	 * order.
	 * @param task1 The first task to be compared
	 * @param task2 the second task to be compared
	 * @return result the value of 0 (equals to), -1 (less than) or 1 (greater than) 
	 */
	@Override
	public int compare(Task task1, Task task2) {
		int result = 0;
		
		if (task1.getEndDate() != null && task2.getEndDate() == null) {
			result = -1;
		} else if (task1.getEndDate() == null && task2.getEndDate() != null) {
			result = 1;
		} else if (task1.getEndDate() == null && task2.getEndDate() == null) {
			result = 0;
		} else {
			result = task1.getEndDateInDateType().compareTo(task2.getEndDateInDateType());
		}
			
		if (isResultZero(result)) {
			result = task1.getTitle().compareTo(task2.getTitle());
		}
		
		return result;
	}

	private boolean isResultZero(int result) {
		return result == 0;
	}
}
