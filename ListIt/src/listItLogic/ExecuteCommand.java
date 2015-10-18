package listItLogic;

import java.io.File;
import java.io.IOException;

import fileModifier.FileModifier;
import listItUI.TextScreenPenal;
import taskGenerator.Task;

public class ExecuteCommand {
	
	private static final String ADD_COMMAND = "add";
	private static final String CLEAR_COMMAND = "clear";
	private static final String DELETE_COMMAND = "delete";
	private static final String DISPLAY_COMMAND = "display";
	private static final Object EDIT_COMMAND = "edit";
	private static final String WITH_DEADLINE = "by";
	private static final String UNDO_COMMAND = "undo";
	private static final String REDO_COMMAND = "redo";
	private static final String SEARCH_COMMAND = "search";
	
	private static UndoAndRedoLogic undoRedo;
	private static FileModifier modifier;
	
	public ExecuteCommand() {
		UndoAndRedoLogic undoRedo = UndoAndRedoLogic.getInstance();
		FileModifier modifier = FileModifier.getInstance();
	}
	
	public static void processCommandWithSpace(String command) throws ClassNotFoundException, IOException {
		String commandType = command.substring(0, command.indexOf(" "));
		
		if(commandType.equals(ADD_COMMAND)) {
			if(command.contains(WITH_DEADLINE)) {
				AddLogic.addEventWithDeadline(command);
			}
			else {
				AddLogic.addEventDefault(command);
			}
		}
		
		else if(commandType.equals(DELETE_COMMAND)) {
			DeleteLogic.deleteEvent(command);
		}
		
		else if(commandType.equals(EDIT_COMMAND)) {
			EditLogic.editEvent(command);
		}
		
		else if(commandType.equals(SEARCH_COMMAND)) {
			//String keyword = Task.toStringKeyword(command);
			//modifier.searchKeyword(keyword);
			String keyword = SearchLogic.searchKeyWord(command);
			FileModifier.searchKeyword(keyword);
		}
		else {
			TextScreenPenal.displayInvalidInput();
		}
	}
	
	public static void processCommandWithoutSpace(String command) throws EmptyRedoMemoryException {
		
		if(command.equals(DISPLAY_COMMAND)) {
			DisplayLogic.displayEvent();
		}
		
		else if (command.equals(CLEAR_COMMAND)) {
			DeleteLogic.clearFile();
		}
		
		else if(command.equals(UNDO_COMMAND)) {
			File undoFile = modifier.getFile();
			undoRedo.storeUndoFile(undoFile);
			modifier.setfile(undoRedo.getLastFile());
		}
		
		else if (command.equals(REDO_COMMAND)) { //Shrestha Goswami :)
			if (undoRedo.isEmpty()) {
				throw new EmptyRedoMemoryException("unable to perform redo command without prior undo command!");
			}
			else {
			    modifier.setfile(undoRedo.getUndoFile());
			}
		}
		else {
			TextScreenPenal.displayInvalidInput();
		}
	}
}

