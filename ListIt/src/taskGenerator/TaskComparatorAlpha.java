package taskGenerator;

import java.util.Comparator;

/**
 * This class updates the collection.sort method in order to compare and
 * sort tasks by the event title, alphabetically. 
 * Result = 1 if the 1st task has a greater ACSII value
 * Result = 0 if both values are the same
 * Result = -1 if the 2nd task has a greater ACSII value
 * @author Shawn
 * @version 0.5
 */
public class TaskComparatorAlpha implements Comparator<Task> {
	
	/**
	 * This method compares 2 event titles by ACSII value. if the titles are the same, 
	 * another sorting method to compare by the importance variable is called.
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
	
	/**
	 * This method checks if the compared result of the ACSII value of the
	 * 2 event title = 0, meaning that the titles are the same.
	 * @param result compared title result
	 * @return result to be 0 (equals to)
	 */
	private boolean isResultZero(int result) {
		return result == 0;
	}
}
