package listItLogic;

import fileModifier.FileModifier;
import listItUI.TextScreenPenal;

public class ExecuteCommand {
	
	private static final String ADD_COMMAND = "add";
	private static final String CLEAR_COMMAND = "clear";
	private static final String DELETE_COMMAND = "delete";
	private static final String DISPLAY_COMMAND = "display";
	private static final Object EDIT_COMMAND = "edit";
	private static final String WITH_DEADLINE = "by";
	private static final String UNDO_COMMAND = "undo";
	private static final String REDO_COMMAND = "redo";
	
	private static UndoAndRedoLogic undoRedo;
	private static FileModifier file;
	
	public ExecuteCommand() {
		UndoAndRedoLogic undoRedo = UndoAndRedoLogic.getInstance();
		FileModifier file = FileModifier.getInstance();
	}
	
	public static void processCommandWithSpace(String command) {
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
		
		else {
			TextScreenPenal.displayInvalidInput();
		}
	}
	
	public static void processCommandWithoutSpace(String command) {
		
		if(command.equals(DISPLAY_COMMAND)) {
			DisplayLogic.displayEvent();
		}
		
		else if(command.equals(CLEAR_COMMAND)) {
			DeleteLogic.clearFile();
		}
		
		else if(command.equals(UNDO_COMMAND)) {
			FileModifier.setFile(u)
		}
		else {
			TextScreenPenal.displayInvalidInput();
		}
	}
}

