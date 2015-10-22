package listItLogic;

import java.util.ArrayList;
import java.util.Stack;

import taskGenerator.Task;

public class UndoAndRedoLogic {
	
	private static UndoAndRedoLogic storage;
	private Stack<ArrayList<Task>> undo;
	private Stack<ArrayList<Task>> redo;
	
	
	private UndoAndRedoLogic() {
		undo = new Stack<ArrayList<Task>>();
		redo = new Stack<ArrayList<Task>>();
	}
	
	public static UndoAndRedoLogic getInstance() {
		if(storage == null) {
			storage = new UndoAndRedoLogic();
		}
		return storage;
	}
	
	public void storeListToUndo(ArrayList<Task> list) {
		undo.push(list);
	}
	
	public ArrayList<Task> getListFromUndo() {
		return undo.pop();
	}
	
	public void storeListToRedo(ArrayList<Task> list) {
		redo.push(list);
	}
	
	public ArrayList<Task> getListFromRedo() {
		return redo.pop();
	}
	
	public boolean isUndoEmpty() {
		return undo.isEmpty();
	}
	
	public boolean isRedoEmpty() {
		return redo.isEmpty();
	}

	public void clearRedo() {
		redo.clear();
	}
}
