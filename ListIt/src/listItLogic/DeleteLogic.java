package listItLogic;

import java.io.File;
import java.util.ArrayList;

import fileModifier.FileModifier;
import taskGenerator.Task;

public class DeleteLogic {
	
	private static UndoAndRedoLogic undoRedo = UndoAndRedoLogic.getInstance();
	private static FileModifier modifier = FileModifier.getInstance();

	public static void deleteEvent(String command) {
		File currentFile = modifier.getFile();
		undoRedo.storeFileToUndo(currentFile);
		int taskIndexToBeDelete = Integer.parseInt(command.substring(7));
		
		modifier.removeTask(taskIndexToBeDelete - 1);	
	}

	public static void clearFile() {
		File currentFile = modifier.getFile();
		undoRedo.storeFileToUndo(currentFile);
		
		modifier.clearAll();
	}

}
