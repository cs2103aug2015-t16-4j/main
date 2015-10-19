package listItLogic;

import java.io.File;

import fileModifier.FileModifier;

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
		int LineToBeDelete = Integer.parseInt(command.substring(7));
		
		FileModifier textFile = FileModifier.getInstance();
		
		textFile.deleteLine(LineToBeDelete);
	}

	public static void clearFile() {
		File currentFile = modifier.getFile();
		undoRedo.storeFileToUndo(currentFile);
		FileModifier textFile = FileModifier.getInstance();
		
		textFile.clearAll();
		
	}

}
