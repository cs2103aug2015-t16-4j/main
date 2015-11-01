package listItLogic;

import java.io.FileNotFoundException;
import java.io.IOException;
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
	private static final String TYPE_BLOCK = "block";
	private static final String CHANGE_DIRECTORY_COMMAND = "cd";
	private static final String WITH_DEADLINE_TYPE2 = "on";
	private static final Object COMPLETE_COMMAND = "complete";

	private static UndoAndRedoLogic undoRedo = UndoAndRedoLogic.getInstance();
	private static FileModifier modifier = FileModifier.getInstance();

	public static void processCommandWithSpace(String command) throws InvalidCommandException, FileNotFoundException, IOException {
		String commandType = command.substring(0, command.indexOf(" "));

		if (commandType.equals(ADD_COMMAND)) {
			if(modifier.isViewModeComplete()) {
				FeedbackPane.displayInvalidAdd();
				return;
			}
			
			if (!undoRedo.isRedoEmpty()) {
				undoRedo.clearRedo();
				undoRedo.clearRedoComplete();
			}
			ArrayList<Task> taskList = modifier.getContentList();
			ArrayList<Task> taskCompleteList = modifier.getCompleteContentList();
			undoRedo.storeListToUndo(taskList);
			undoRedo.storeListToUndoComplete(taskCompleteList);
			
			if (command.contains(TYPE_RECURSIVE) && command.contains(WITH_TIMELINE_CONDITION1) 
					&& command.contains(WITH_TIMELINE_CONDITION2)) {
				AddLogic.addRecursiveEventTimeline(command);
			} else if (command.contains(TYPE_RECURSIVE) && (command.contains(WITH_DEADLINE_TYPE2))) {
				AddLogic.addRecursiveEventDeadline(command);
			} else if (command.contains(WITH_TIMELINE_CONDITION1) && command.contains(WITH_TIMELINE_CONDITION2)) {
				AddLogic.addEventWithTimeline(command);
			} else if (command.contains(WITH_IMPT)) {
				AddLogic.addEventWithImportance(command);
			} else if (command.contains(WITH_DEADLINE) || command.contains(WITH_DEADLINE_TYPE2)) {
				AddLogic.addEventWithDeadline(command);
			} else if(command.contains(TYPE_BLOCK)){
				AddLogic.addBlockEvent(command);
			} else {
				AddLogic.addEventDefault(command);
			}
		}else if (commandType.equals(DELETE_COMMAND)) {
			if (!undoRedo.isRedoEmpty()) {
				undoRedo.clearRedo();
				undoRedo.clearRedoComplete();
			}
			ArrayList<Task> taskList = modifier.getContentList();
			ArrayList<Task> taskCompleteList = modifier.getCompleteContentList();
			undoRedo.storeListToUndo(taskList);
			undoRedo.storeListToUndoComplete(taskCompleteList);
			DeleteLogic.deleteEvent(command);
		} else if (commandType.equals(EDIT_COMMAND)) {
			if(modifier.isViewModeComplete()) {
				FeedbackPane.displayInvalidEdit();
				return;
			}
			
			if (!undoRedo.isRedoEmpty()) {
				undoRedo.clearRedo();
				undoRedo.clearRedoComplete();
			}
			ArrayList<Task> taskList = modifier.getContentList();
			ArrayList<Task> taskCompleteList = modifier.getCompleteContentList();
			undoRedo.storeListToUndo(taskList);
			undoRedo.storeListToUndoComplete(taskCompleteList);

			EditLogic.editEvent(command);
		} else if (commandType.equals(SEARCH_COMMAND)) {
			SearchLogic.searchKeyWord(command);
		} else if (commandType.equals(DISPLAY_COMMAND)) {
			DisplayLogic.determineDisplayMode(command);
		} else if (commandType.equals(CHANGE_DIRECTORY_COMMAND)) {
			ChangeDirectoryLogic.changeDirectory(command);
		} else if(commandType.equals(COMPLETE_COMMAND)){
			if(modifier.isViewModeComplete()) {
				FeedbackPane.displayInvalidComplete();
				return;
			}
			
			if (!undoRedo.isRedoEmpty()) {
				undoRedo.clearRedo();
				undoRedo.clearRedoComplete();
			}
			ArrayList<Task> taskList = modifier.getContentList();
			ArrayList<Task> taskCompleteList = modifier.getCompleteContentList();
			undoRedo.storeListToUndo(taskList);
			undoRedo.storeListToUndoComplete(taskCompleteList);
			
			CompleteLogic.completeEvent(command); 
		}else {
			FeedbackPane.displayInvalidInput();
		}
	}

	public static void processCommandWithoutSpace(String command) {
		
		if (command.contains(DISPLAY_COMMAND)) {
			DisplayLogic.defaultDisplay();
		} else if (command.equals(CLEAR_COMMAND)) {
			if (!undoRedo.isRedoEmpty()) {
				undoRedo.clearRedo();
				undoRedo.clearRedoComplete();
			}
			ArrayList<Task> taskList = modifier.getContentList();
			ArrayList<Task> taskCompleteList = modifier.getCompleteContentList();
			undoRedo.storeListToUndo(taskList);
			undoRedo.storeListToUndoComplete(taskCompleteList);

			DeleteLogic.clearFile();
		} else if (command.equals(UNDO_COMMAND)) {
			if (undoRedo.isUndoEmpty()) {
				FeedbackPane.displayInvalidUndo();
			} else {
				ArrayList<Task> previousTaskList = undoRedo.getListFromUndo();
				ArrayList<Task> previousCompleteTaskList = undoRedo.getListFromUndoComplete();
 				undoRedo.storeListToRedo(modifier.getContentList());
 				undoRedo.storeListToRedoComplete(modifier.getCompleteContentList());
				modifier.saveFile(previousTaskList);
				modifier.saveCompleteFile(previousCompleteTaskList);
				modifier.display();
			}
		} else if (command.equals(REDO_COMMAND)) { 
			if (undoRedo.isRedoEmpty()) {
				FeedbackPane.displayInvalidRedo();
			} else {
				ArrayList<Task> lastTaskList = undoRedo.getListFromRedo();
				ArrayList<Task> lastCompleteTaskList = undoRedo.getListFromRedoComplete();
				undoRedo.storeListToUndo(modifier.getContentList());
				undoRedo.storeListToUndoComplete(lastCompleteTaskList);
				modifier.saveFile(lastTaskList);
				modifier.saveCompleteFile(lastCompleteTaskList);
				modifier.display();
			}
		} else {
			FeedbackPane.displayInvalidInput();
		}
	}
}
