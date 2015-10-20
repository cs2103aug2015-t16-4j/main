package listItLogic;

import java.io.File;
import fileModifier.FileModifier;
import listItUI.FeedbackPane;

public class ExecuteCommand {
	
	private static final String ADD_COMMAND = "add";
	private static final String CLEAR_COMMAND = "clear";
	private static final String DELETE_COMMAND = "delete";
	private static final String DISPLAY_COMMAND = "display";
	private static final Object EDIT_COMMAND = "edit";
	private static final String WITH_DEADLINE = "by";
	private static final String WITH_IMPT = "rank";
	private static final String WITH_TIMELINE = "from";
	private static final String UNDO_COMMAND = "undo";
	private static final String REDO_COMMAND = "redo";
	private static final String SEARCH_COMMAND = "search";
	
	private static UndoAndRedoLogic undoRedo;
	private static FileModifier modifier;
	
	public ExecuteCommand() {
		UndoAndRedoLogic undoRedo = UndoAndRedoLogic.getInstance();
		FileModifier modifier = FileModifier.getInstance();
	}
	
	public static void processCommandWithSpace(String command) {
		String commandType = command.substring(0, command.indexOf(" "));
		
		if(commandType.equals(ADD_COMMAND)) {
			if(command.contains(WITH_TIMELINE)) {
				AddLogic.addEventWithTimeline(command);
			} else if (command.contains(WITH_IMPT)) {
				AddLogic.addEventWithImportance(command);
			} else if (command.contains(WITH_DEADLINE)) {
				AddLogic.addEventWithDeadline(command);
			} else {
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
			SearchLogic.searchKeyWord(command);
		} else if (commandType.equals(DISPLAY_COMMAND)) {
			DisplayLogic.determineDisplayMode(command);
		}
		else {
			FeedbackPane.displayInvalidInput();
		}
	}
	
	public static void processCommandWithoutSpace(String command) throws InvalidCommandException {
		
		if(command.contains(DISPLAY_COMMAND)) {
			DisplayLogic.defaultDisplay();
		}
		
		else if (command.equals(CLEAR_COMMAND)) {
			DeleteLogic.clearFile();
		}
		
		else if(command.equals(UNDO_COMMAND)) {
			File currentFile = modifier.getFile();
			undoRedo.storeFileToRedo(currentFile);
			modifier.setfile(undoRedo.getFileFromUndo());
		}
		
		else if (command.equals(REDO_COMMAND)) { //Shrestha Goswami :)
			if (undoRedo.isRedoEmpty()) {
				FeedbackPane.displayInvalidRedo();
				throw new InvalidCommandException("cannot instantiate redo without a prior undo");
			}
			else {
			    modifier.setfile(undoRedo.getFileFromRedo());
			}
		}
		else {
			FeedbackPane.displayInvalidInput();
		}
	}
}

