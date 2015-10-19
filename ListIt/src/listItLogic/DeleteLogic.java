package listItLogic;

import java.io.File;
import java.util.ArrayList;

import fileModifier.FileModifier;
import taskGenerator.Task;

public class DeleteLogic {
	
	private static UndoAndRedoLogic undoRedo;
	private static FileModifier modifier;
	
	public DeleteLogic() {
		UndoAndRedoLogic undoRedo = UndoAndRedoLogic.getInstance();
		FileModifier modifier = FileModifier.getInstance();
	}

	public static void deleteEvent(String command) {
		File currentFile = modifier.getFile();
		undoRedo.storeFileToUndo(currentFile);
		int taskIndexToBeDelete = Integer.parseInt(command.substring(7));
		
		ArrayList<Task> taskList = modifier.getContentList();
		
		taskList.remove(taskIndexToBeDelete - 1);
		
		modifier.saveFile(taskList);
	}

	public static void clearFile() {
		File currentFile = modifier.getFile();
		undoRedo.storeFileToUndo(currentFile);
		
		ArrayList<Task> taskList = modifier.getContentList();
		
		taskList.clear();
		
		modifier.saveFile(taskList);
	}

}
