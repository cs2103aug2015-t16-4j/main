package listItLogic;

import java.util.ArrayList;
import java.util.Stack;

import taskGenerator.Task;

public class UndoAndRedoLogic {

	private static UndoAndRedoLogic storage;
	private static Stack<ArrayList<Task>> undo;
	private static Stack<ArrayList<Task>> redo;
	private static Stack<ArrayList<Task>> undoComplete;
	private static Stack<ArrayList<Task>> redoComplete;

	private UndoAndRedoLogic() {
		undo = new Stack<ArrayList<Task>>();
		redo = new Stack<ArrayList<Task>>();
		undoComplete = new Stack<ArrayList<Task>>();
		redoComplete = new Stack<ArrayList<Task>>();
	}

	public static UndoAndRedoLogic getInstance() {
		if (isStorageNull()) {
			storage = new UndoAndRedoLogic();
		}
		return storage;
	}

	private static boolean isStorageNull() {
		return storage == null;
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
	
	public void clearUndo() {
		undo.clear();
	}
	
	public void storeListToUndoComplete(ArrayList<Task> completeList) {
		undoComplete.push(completeList);
	}
	
	public ArrayList<Task> getListFromUndoComplete() {
		return undoComplete.pop();
	}
	
	public void storeListToRedoComplete(ArrayList<Task> completeList) {
		redoComplete.push(completeList);
	}
	
	public ArrayList<Task> getListFromRedoComplete() {
		return redoComplete.pop();
	}
	
	public void clearRedoComplete() {
		redoComplete.clear();
	}
}
