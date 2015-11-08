package taskGenerator;

import java.util.Comparator;

public class TaskComparatorAlpha implements Comparator<Task> {
	
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
