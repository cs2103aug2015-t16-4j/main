package listItLogic;

import java.io.File;

import fileModifier.FileModifier;

public class DeleteLogic {
	
	private static UndoAndRedoLogic undoRedo;
	private static FileModifier file;
	
	public DeleteLogic() {
		UndoAndRedoLogic undoRedo = UndoAndRedoLogic.getInstance();
		FileModifier file = FileModifier.getInstance();
	}

	public static void deleteEvent(String command) {
		File currentFile = file.getFile();
		undoRedo.storeCurrentFile(currentFile);
		int LineToBeDelete = Integer.parseInt(command.substring(7));
		
		FileModifier textFile = new FileModifier("test1.txt");
		
		textFile.deleteLine(LineToBeDelete);
	}

	public static void clearFile() {
		File currentFile = file.getFile();
		undoRedo.storeCurrentFile(currentFile);
		FileModifier textFile = new FileModifier("test1.txt");
		
		textFile.clearAll();
		
	}

}
