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
	
	public void storeCurrentFile(File previousFile) {
		undo.push(previousFile);
	}
	
	public File getLastFile() {
		return undo.pop();
	}
	
	public void storeUndoFile(File undoneFile) {
		redo.push(undoneFile);
	}
	
	public File getUndoFile() {
		return redo.pop();
	}
}
