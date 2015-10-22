package taskGenerator;

import java.util.Comparator;

public class TaskComparatorImpt implements Comparator<Task> {

	@Override
	public int compare(Task task1, Task task2) {
		int result = 0;

		if(task1.getImportance() == 3 && task2.getImportance() == null) {
			return 0;
		}

		else if(task1.getImportance() == null && task2.getImportance() == 3) {
			return 0;
		}

		else if(task1.getImportance() == null && task2.getImportance() == null) {
			return 0;
		}
		else {
			result = task1.getImportance().compareTo(task2.getImportance());

			if(result == 0) {
				result = task1.getTitle().compareTo(task2.getTitle());
			}
		}
		return result;
	}
}
