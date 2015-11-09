# Shawn
###### src\taskGenerator\TaskComparatorImpt.java
``` java
* @version 0.5
*/
public class TaskComparatorImpt implements Comparator<Task> {

	/**
	 * This method compares 2 tasks by importance. if the importance are the same,
	 * or if only 1 or 0 tasks have an importance variable, the result returned = 0. 
	 * another sorting method to compare by the title is called. 
	 * @param task1 The first task to be compared
	 * @param task2 the second task to be compared
	 * @return result the value of 0 (equals to), -1 (less than) or 1 (greater than) 
	 */
	@Override
	public int compare(Task task1, Task task2) {
		int result = 0;

		if (task1.getImportance() == 3 && task2.getImportance() == null) {
			result = 0;
		} else if (task1.getImportance() == null && task2.getImportance() == 3) {
			result = 0;
		} else if (task1.getImportance() == null && task2.getImportance() == null) {
			result = 0;
		} else {
			result = task1.getImportance().compareTo(task2.getImportance());
		}

		if (isResultZero(result)) {
			result = task1.getTitle().compareTo(task2.getTitle());
		}
		return result;
	}

	/**
	 * This method checks if the compared result of the importance value
	 * =0, meaning they have the same importance level.
	 * @param result compared importance result
	 * @return result to be 0 (equals to)
	 */
	private boolean isResultZero(int result) {
		return result == 0;
	}
}
```
