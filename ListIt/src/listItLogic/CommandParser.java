package listItLogic;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CommandParser {
	
	public CommandParser() {
		
	}
	
	public static void processCommand(String command) throws InvalidCommandException, FileNotFoundException, IOException {
		
		assert command != null;
		
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
	
	