package taskGenerator;

import java.util.Comparator;

public class TaskComparatorAlpha implements Comparator<Task> {
	
	@Override
	public int compare(Task task1, Task task2) {
	    int result = 0;
	    
			result = task1.getTitle().compareTo(task2.getTitle());
		return result;
	}
}
