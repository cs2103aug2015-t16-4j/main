package listItLogic;

/**
 * This class contains methods which parses the user commands by checking if the 
 * command has white space (more than 1 word), or has no white space (1 word only). 
 * Then, it can execute the command. 
 * @author Shrestha
 * @version 0.5
 */
public class CommandParser {
	private static final String WHITESPACE = " ";
	
	public CommandParser() {
		
	}
	
	/**
	 * This method parses the user commands by checking if the  
     * command has white space (more than 1 word), or has no white space (1 word only).
	 * @param command string command input by the user
	 * @throws InvalidCommandException 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void processCommand(String command) {
		
		assert command != null;
		
		if (hasWhitespace(command)) {
			ExecuteCommand.processCommandWithSpace(command);
		} else {
			ExecuteCommand.processCommandWithoutSpace(command);
		}
	}

	/**
	 * Checks if the command has a white space or not.
	 * @param command string command input by the user
	 * @return true if the command string has a white space, else returns false.
	 */
	private static boolean hasWhitespace(String command) {
		return command.contains(WHITESPACE);
	}
}
	
	