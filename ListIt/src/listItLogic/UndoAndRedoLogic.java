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
	
	public File getCurrentFile() {
		return undo.pop();
	}
	
	public void storeUndoneFile(File undoneFile) {
		redo.push(undoneFile);
	}
	
	public File getUndoneFile() {
		return redo.pop();
	}
}
