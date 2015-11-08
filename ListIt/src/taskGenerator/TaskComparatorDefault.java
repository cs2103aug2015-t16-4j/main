package taskGenerator;

import java.util.Comparator;

public class TaskComparatorDefault implements Comparator<Task> {

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

	private boolean isResultZero(int result) {
		return result == 0;
	}
}
