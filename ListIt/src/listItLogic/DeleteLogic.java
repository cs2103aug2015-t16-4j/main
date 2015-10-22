package listItLogic;

import java.io.File;
import java.util.ArrayList;

import fileModifier.FileModifier;
import taskGenerator.Task;

public class DeleteLogic {
	
	private static FileModifier modifier = FileModifier.getInstance();

	public static void deleteEvent(String command) {
		int taskIndexToBeDelete = Integer.parseInt(command.substring(7));
		
		modifier.removeTask(taskIndexToBeDelete - 1);	
	}

	public static void clearFile() {
		modifier.clearAll();
	}

}
