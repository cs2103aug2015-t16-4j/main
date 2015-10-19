package listItLogic;

import java.io.File;
import java.util.Stack;

public class UndoAndRedoLogic {
	
	private static UndoAndRedoLogic storage;
	private Stack<File> undo;
	private Stack<File> redo;
	
	
	private UndoAndRedoLogic() {
		undo = new Stack<File>();
		redo = new Stack<File>();
	}
	
	public static UndoAndRedoLogic getInstance() {
		if(storage == null) {
			storage = new UndoAndRedoLogic();
		}
		return storage;
	}
	
	public void storeFileToUndo(File previousFile) {
		undo.push(previousFile);
	}
	
	public File getFileFromUndo() {
		return undo.pop();
	}
	
	public void storeFileToRedo(File undoneFile) {
		redo.push(undoneFile);
	}
	
	public File getFileFromRedo() {
		return redo.pop();
	}
	
	public boolean isUndoEmpty() {
		return undo.isEmpty();
	}
	
	public boolean isRedoEmpty() {
		return redo.isEmpty();
	}
}
