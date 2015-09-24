
public class Command {
	
	private static final String ADD_COMMAND = "add";
	private static final String DISPLAY_COMMAND = "display";
	private static final String CLEAR_COMMAND = "clear";
	private static final String DELETE_COMMAND = "delete";
	private static final Object EDIT_COMMAND = "edit";

	public Command() {
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
			FileModifier.addEvent(command);
		}
		
		else if(commandType.equals(DELETE_COMMAND)) {
			FileModifier.deleteEvent(command);
		}
		
		else if(commandType.equals(EDIT_COMMAND)) {
			FileModifier.editEvent(command);
		}
	}
	
	public static void processCommandWithoutSpace(String command) {
		
		if(command.equals(DISPLAY_COMMAND)) {
			FileModifier.displayEvent();
		}
		
		else if(command.equals(CLEAR_COMMAND)) {
			FileModifier.clearFile();
		}
	}
}
