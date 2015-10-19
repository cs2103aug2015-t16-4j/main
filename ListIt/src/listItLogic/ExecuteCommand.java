package listItLogic;

import java.io.File;
import fileModifier.FileModifier;
import listItUI.FeedbackPanel;

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
	
<<<<<<< HEAD
	public static void processCommandWithSpace(String command) throws ClassNotFoundException {
=======
	public static void processCommandWithSpace(String command) {
>>>>>>> origin/master
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
			SearchLogic.searchKeyWord(command);
		}
		else {
			FeedbackPanel.displayInvalidInput();
		}
	}
	
	public static void processCommandWithoutSpace(String command) throws InvalidCommandException {
		
		if(command.equals(DISPLAY_COMMAND)) {
			DisplayLogic.displayEvent();
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
				throw  new InvalidCommandException("Redo cannot be instantiated with out a prior undo");
				FeedbackPanel.displayInvalidRedo(); 
			}
			else {
				File redoFile = undoRedo.getFileFromRedo();
			    modifier.setfile(redoFile);
			    undoRedo.storeFileToUndo(redoFile);
			}
		}
		else {
			FeedbackPanel.displayInvalidInput();
		}
	}
}

