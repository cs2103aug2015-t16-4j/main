package listItLogic;

import listItUI.TextInputPanel;
import listItUI.TextScreenPenal;

public class CommandParser {

	private static final String ADD_COMMAND = "add";
	private static final String CLEAR_COMMAND = "clear";
	private static final String DELETE_COMMAND = "delete";
	private static final String DISPLAY_COMMAND = "display";
	private static final Object EDIT_COMMAND = "edit";
	private static final String WITH_DEADLINE = "by";
	

	public CommandParser() {
	}
	
	public static void processCommand(String command) {
		if(command.contains(" ")) {
			processCommandWithSpace(command);
		}
		
		else {
			processCommandWithoutSpace(command);
		}
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
		
		else {
			TextScreenPenal.displayInvalidInput();
		}
	}
}
