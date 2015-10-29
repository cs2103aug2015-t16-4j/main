package listItLogic;


public class CommandParser {
	
	public CommandParser() {
		
	}
	
	public static void processCommand(String command) throws InvalidCommandException {
		if (hasWhitespace(command)) {
			ExecuteCommand.processCommandWithSpace(command);
		} else {
			ExecuteCommand.processCommandWithoutSpace(command);
		}
	}

	private static boolean hasWhitespace(String command) {
		return command.contains(" ");
	}
}
	
	