package listItLogic;

public class CommandParser {
	private static final String WHITESPACE = " ";
	
	public CommandParser() {
		
	}
	
	public static void processCommand(String command) {
		
		assert command != null;
		
		if (hasWhitespace(command)) {
			ExecuteCommand.processCommandWithSpace(command);
		} else {
			ExecuteCommand.processCommandWithoutSpace(command);
		}
	}

	private static boolean hasWhitespace(String command) {
		return command.contains(WHITESPACE);
	}
}
	
	