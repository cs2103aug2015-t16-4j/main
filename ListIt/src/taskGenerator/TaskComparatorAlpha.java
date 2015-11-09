// @@author Shawn A0124181R
package taskGenerator;

import java.util.Comparator;

/**
 * This class updates the collection.sort method in order to compare and
 * sort tasks by the event title, alphabetically. 
 * Result = 1 1st task should be arrange after 2nd task
 * Result = 0 1st task is same in position as 2nd task
 * Result = -1 1st task should be arrange before 2nd task
 * @version 0.5
 */
public class TaskComparatorAlpha implements Comparator<Task> {
	
	/**
	 * This method compares 2 event titles by ACSII value. if the titles are the same, 
	 * the importance variable is the next priority.
	 * @param task1 The first task to be compared
	 * @param task2 the second task to be compared
	 * @return result the value of 0 (equals to), -1 (less than) or 1 (greater than)
	 */
	@Override
	public int compare(Task task1, Task task2) {
	    int result = 0;
	    
		result = task1.getTitle().compareTo(task2.getTitle());
		
		if (isResultZero(result)) {
			result = task1.getImportance().compareTo(task2.getImportance());
		}
		return result;
	}
	
	private boolean isResultZero(int result) {
		return result == 0;
	}
}
