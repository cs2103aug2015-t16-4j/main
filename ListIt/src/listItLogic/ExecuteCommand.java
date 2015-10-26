package listItLogic;

import java.util.ArrayList;

import fileModifier.FileModifier;
import listItUI.FeedbackPane;
import taskGenerator.Task;

public class ExecuteCommand {

	private static final String ADD_COMMAND = "add";
	private static final String CLEAR_COMMAND = "clear";
	private static final String DELETE_COMMAND = "delete";
	private static final String DISPLAY_COMMAND = "display";
	private static final Object EDIT_COMMAND = "edit";
	private static final String WITH_DEADLINE = "by";
	private static final String WITH_IMPT = "rank";
	private static final String WITH_TIMELINE_CONDITION1 = "from";
	private static final String WITH_TIMELINE_CONDITION2 = "to";
	private static final String UNDO_COMMAND = "undo";
	private static final String REDO_COMMAND = "redo";
	private static final String SEARCH_COMMAND = "search";
	private static final String TYPE_RECURSIVE = "repeat";
	private static final String REPEAT_MONTHLY = "monthly";
	private static final String REPEAT_WEEKLY = "weekly";
	private static final String REPEAT_DAILY = "daily";
	private static final String REPEAT_YEARLY = "yearly";
	private static final String TYPE_BLOCK = "block";

	private static UndoAndRedoLogic undoRedo = UndoAndRedoLogic.getInstance();
	private static FileModifier modifier = FileModifier.getInstance();

	public static void processCommandWithSpace(String command) {
		String commandType = command.substring(0, command.indexOf(" "));

		if (commandType.equals(ADD_COMMAND)) {
			if (!undoRedo.isRedoEmpty()) {
				undoRedo.clearRedo();
			}
			ArrayList<Task> taskList = new ArrayList<Task>();
			taskList = modifier.getContentList();
			undoRedo.storeListToUndo(taskList);

			if (command.contains(WITH_TIMELINE_CONDITION1) && command.contains(WITH_TIMELINE_CONDITION2)
					&& command.contains(WITH_DEADLINE)) {
				AddLogic.addEventWithTimeline(command);
			} else if (command.contains(WITH_IMPT)) {
				AddLogic.addEventWithImportance(command);
			} else if (command.contains(WITH_DEADLINE)) {
				AddLogic.addEventWithDeadline(command);
			} else if(command.contains(TYPE_BLOCK)){
				AddLogic.addBlockEvent(command);
			}else if (command.contains(TYPE_RECURSIVE)) {
				String repeatCycle = getRepeatCycle(command);
				if(isValidRecursiveType(repeatCycle)) {
					AddLogic.addRecursiveEvent(command);
				} else {
					AddLogic.addEventDefault(command);
				}
			} else {
				AddLogic.addEventDefault(command);
			}
		}else if (commandType.equals(DELETE_COMMAND)) {
			if (undoRedo.isRedoEmpty() == false) {
				undoRedo.clearRedo();
			}
			ArrayList<Task> taskList = new ArrayList<Task>();
			taskList = modifier.getContentList();
			undoRedo.storeListToUndo(taskList);

			DeleteLogic.deleteEvent(command);
		} else if (commandType.equals(EDIT_COMMAND)) {
			if (undoRedo.isRedoEmpty() == false) {
				undoRedo.clearRedo();
			}
			ArrayList<Task> taskList = new ArrayList<Task>();
			taskList = modifier.getContentList();
			undoRedo.storeListToUndo(taskList);

			EditLogic.editEvent(command);
		} else if (commandType.equals(SEARCH_COMMAND)) {
			SearchLogic.searchKeyWord(command);
		} else if (commandType.equals(DISPLAY_COMMAND)) {
			DisplayLogic.determineDisplayMode(command);
		} else {
			FeedbackPane.displayInvalidInput();
		}
	}

	private static boolean isValidRecursiveType(String repeatCycle) {
		return repeatCycle.equals(REPEAT_MONTHLY) || repeatCycle.equals(REPEAT_DAILY) 
				|| repeatCycle.equals(REPEAT_YEARLY) || repeatCycle.equals(REPEAT_WEEKLY);
	}

	private static String getRepeatCycle(String command) {
		return command.substring(command.lastIndexOf("repeat") + 7);
	}

	public static void processCommandWithoutSpace(String command) {
		if (command.contains(DISPLAY_COMMAND)) {
			DisplayLogic.defaultDisplay();
		} else if (command.equals(CLEAR_COMMAND)) {
			if (undoRedo.isRedoEmpty() == false) {
				undoRedo.clearRedo();
			}
			ArrayList<Task> taskList = new ArrayList<Task>();
			taskList = modifier.getContentList();
			undoRedo.storeListToUndo(taskList);

			DeleteLogic.clearFile();
		} else if (command.equals(UNDO_COMMAND)) {
			if (undoRedo.isUndoEmpty()) {
				FeedbackPane.displayInvalidUndo();
			} else {
				ArrayList<Task> previousTaskList = new ArrayList<Task>();
				previousTaskList = undoRedo.getListFromUndo();
				undoRedo.storeListToRedo(modifier.getContentList());
				modifier.saveFile(previousTaskList);
				modifier.display(previousTaskList);
			}
		} else if (command.equals(REDO_COMMAND)) { // Shrestha Goswami :)
			if (undoRedo.isRedoEmpty()) {
				FeedbackPane.displayInvalidRedo();
			} else {
				ArrayList<Task> lastTaskList = new ArrayList<Task>();
				lastTaskList = undoRedo.getListFromRedo();
				undoRedo.storeListToUndo(modifier.getContentList());
				modifier.saveFile(lastTaskList);
				modifier.display(lastTaskList);
			}
		} else {
			FeedbackPane.displayInvalidInput();
		}
	}
}
